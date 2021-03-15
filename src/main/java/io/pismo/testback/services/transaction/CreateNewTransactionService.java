/**
 * 
 */
package io.pismo.testback.services.transaction;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.pismo.testback.api.requests.transaction.NewTransactionRequest;
import io.pismo.testback.model.Account;
import io.pismo.testback.model.Transaction;
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

	public Transaction create(NewTransactionRequest request) {
		LOGGER.info("Creating a new transaction...");
		Optional<Account> account = accountsRepository.findById(request.getAccountId());
		Optional<Operation> operation = operationsRepository.findById(request.getOperationTypeId());
		
		return transactionsRepository.save(new Transaction(operation.get(), account.get(), request.getAmount()));
	}
}