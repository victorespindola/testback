/**
 * 
 */
package io.pismo.testback.services.transaction;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.pismo.testback.api.requests.transaction.NewTransactionRequest;
import io.pismo.testback.exceptions.DependencyNotFoundException;
import io.pismo.testback.exceptions.TransactionAmountGreaterThanLimitException;
import io.pismo.testback.model.Account;
import io.pismo.testback.model.Transaction;
import io.pismo.testback.model.operations.Credit;
import io.pismo.testback.model.operations.Debit;
import io.pismo.testback.model.operations.Operation;
import io.pismo.testback.repositories.AccountsRepository;
import io.pismo.testback.repositories.OperationsRepository;
import io.pismo.testback.repositories.TransactionsRepository;

/**
 * @author victormartins
 *
 */
public class CreateNewTransactionServiceTest {

	@Test
	public void shouldCreateANewTransaction() throws Exception {
		long accountId = 1L;
		long operationTypeId = 1L;
		double amount = 123.45;
		
		Account account = mock(Account.class);
		when(account.updateLimit(amount)).thenReturn(account);
		
		AccountsRepository accountsRepository = mock(AccountsRepository.class);
		when(accountsRepository.findById(accountId)).thenReturn(Optional.of(account));

		OperationsRepository operationsRepository = mock(OperationsRepository.class);		
		when(operationsRepository.findById(operationTypeId)).thenReturn(Optional.of(new Debit("Pagamento")));
		
		TransactionsRepository transactionsRepository = mock(TransactionsRepository.class);
		
		NewTransactionRequest request = new NewTransactionRequest(accountId, operationTypeId, amount);
		new CreateNewTransactionService(transactionsRepository, accountsRepository, operationsRepository).create(request);
		
		verify(account, times(1)).updateLimit(amount);
		verify(accountsRepository, times(1)).findById(accountId);
		verify(accountsRepository, times(1)).save(account);
		verify(operationsRepository, times(1)).findById(operationTypeId);
		verify(transactionsRepository, times(1)).save(Mockito.any());
	}
	
	@Test
	public void shouldThrowExceptionWhenADependencyIsNotFound() throws Exception {
		assertThrows(DependencyNotFoundException.class, () -> {
			long accountId = 1L;
			long operationTypeId = 1L;
			double amount = 123.45;
			
			Account account = mock(Account.class);
			AccountsRepository accountsRepository = mock(AccountsRepository.class);		
			when(accountsRepository.findById(accountId)).thenReturn(Optional.of(account));
			
			OperationsRepository operationsRepository = mock(OperationsRepository.class);		
			when(operationsRepository.findById(operationTypeId)).thenReturn(Optional.empty());
			
			TransactionsRepository transactionsRepository = mock(TransactionsRepository.class);
			
			NewTransactionRequest request = new NewTransactionRequest(accountId, operationTypeId, amount);
			new CreateNewTransactionService(transactionsRepository, accountsRepository, operationsRepository).create(request);
			fail();
		});
	}
	
	@Test
	public void shouldThrowsExceptionWhenAmountIsGreaterThanAvailableLimit() throws Exception {
		assertThrows(TransactionAmountGreaterThanLimitException.class, () -> {
			long accountId = 1L;
			long operationTypeId = 1L;
			double amount = 123.45;
			
			Account account = new Account(accountId, "123456", 100.00);
			
			AccountsRepository accountsRepository = mock(AccountsRepository.class);		
			when(accountsRepository.findById(accountId)).thenReturn(Optional.of(account));
			
			OperationsRepository operationsRepository = mock(OperationsRepository.class);		
			when(operationsRepository.findById(operationTypeId)).thenReturn(Optional.of(new Credit("SAQUE")));
						
			TransactionsRepository transactionsRepository = mock(TransactionsRepository.class);
			
			NewTransactionRequest request = new NewTransactionRequest(accountId, operationTypeId, amount);
			new CreateNewTransactionService(transactionsRepository, accountsRepository, operationsRepository).create(request);
			fail();
		});
	}
}