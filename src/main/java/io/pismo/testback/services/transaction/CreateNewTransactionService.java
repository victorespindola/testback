/**
 * 
 */
package io.pismo.testback.services.transaction;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pismo.testback.api.requests.transaction.NewTransactionRequest;
import io.pismo.testback.exceptions.DependencyNotFoundException;
import io.pismo.testback.model.Account;
import io.pismo.testback.model.Transaction;
import io.pismo.testback.model.operations.Debit;
import io.pismo.testback.model.operations.Operation;
import io.pismo.testback.repositories.AccountsRepository;
import io.pismo.testback.repositories.OperationsRepository;
import io.pismo.testback.repositories.TransactionsRepository;

/**
 * @author victormartins
 *
 */
@Service
public class CreateNewTransactionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateNewTransactionService.class);
	
	private TransactionsRepository transactionsRepository;
	private AccountsRepository accountsRepository;
	private OperationsRepository operationsRepository;

	public CreateNewTransactionService(TransactionsRepository transactionsRepository, 
									   AccountsRepository accountsRepository,
									   OperationsRepository operationsRepository) {
				this.transactionsRepository = transactionsRepository;
				this.accountsRepository = accountsRepository;
				this.operationsRepository = operationsRepository;
	}

	@Transactional
	public Transaction create(NewTransactionRequest request) {
		LOGGER.info("Creating a new transaction...");
		Optional<Account> account = accountsRepository.findById(request.getAccountId());
		Optional<Operation> operation = operationsRepository.findById(request.getOperationTypeId());
		
		if(account.isPresent()) {
			Account realAccount = account.get();			
			try {
				Operation realOperation = operation.get();
				Double remainingAmount = request.getAmount();
				if(realOperation instanceof Debit) {
					List<Transaction> accounts = this.transactionsRepository.findCreditsByAccountId(realAccount.getId());
					for (Transaction transaction : accounts) {
						remainingAmount = transaction.updateBalance(remainingAmount);
						this.transactionsRepository.save(transaction);
						if(remainingAmount == 0.0) break;
					}
				}				
				
				this.accountsRepository.save(realAccount.updateLimit(request.getAmount() * operation.get().getFactor()));
				return transactionsRepository.save(new Transaction(operation.get(), account.get(), request.getAmount(), remainingAmount));
			}
			catch (NoSuchElementException e) {
				throw new DependencyNotFoundException(e);
			}
		}
		
		return null;
	}
}