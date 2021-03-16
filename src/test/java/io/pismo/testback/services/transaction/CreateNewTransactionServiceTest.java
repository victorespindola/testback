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

import io.pismo.testback.api.requests.transaction.NewTransactionRequest;
import io.pismo.testback.exceptions.DependencyNotFoundException;
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
public class CreateNewTransactionServiceTest {

	@Test
	public void shouldCreateANewTransaction() throws Exception {
		long accountId = 1L;
		long operationTypeId = 1L;
		double amount = 123.45;
		
		Account account = mock(Account.class);
		AccountsRepository accountsRepository = mock(AccountsRepository.class);		
		when(accountsRepository.findById(accountId)).thenReturn(Optional.of(account));
		
		Operation operation = mock(Operation.class);
		OperationsRepository operationsRepository = mock(OperationsRepository.class);		
		when(operationsRepository.findById(operationTypeId)).thenReturn(Optional.of(operation));
		
		TransactionsRepository transactionsRepository = mock(TransactionsRepository.class);
		
		NewTransactionRequest request = new NewTransactionRequest(accountId, operationTypeId, amount);
		new CreateNewTransactionService(transactionsRepository, accountsRepository, operationsRepository).create(request);
		
		verify(accountsRepository, times(1)).findById(accountId);
		verify(operationsRepository, times(1)).findById(operationTypeId);
		verify(transactionsRepository, times(1)).save(new Transaction(operation, account, amount));
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
}