package io.pismo.testback.services.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
	
	@Test
	public void shouldUpdateTransactionsWhenNewDebitTransactionIsCreated() throws Exception {
	
		long accountId = 1L;
		double amount = 60.0;
		
		Account account = new Account(accountId, "123456", 100.00);
		
		AccountsRepository accountsRepository = mock(AccountsRepository.class);
		when(accountsRepository.findById(accountId)).thenReturn(Optional.of(account));

		Operation operation = new Credit(1L, "Compra a vista");
		Operation pagamento = new Debit(2L, "Pagamento");
		
		OperationsRepository operationsRepository = mock(OperationsRepository.class);
		when(operationsRepository.findById(1L)).thenReturn(Optional.of(operation));
		when(operationsRepository.findById(2L)).thenReturn(Optional.of(pagamento));
		
		TransactionsRepository transactionsRepository = mock(TransactionsRepository.class);
		Transaction transaction1 = new Transaction(operation, account, 50.0);
		Transaction transaction2 = new Transaction(operation, account, 23.5);
		Transaction transaction3 = new Transaction(operation, account, 18.7);
		when(transactionsRepository.findCreditsByAccountId(accountId)).thenReturn(Arrays.asList(transaction1, transaction2, transaction3));
		
		NewTransactionRequest request = new NewTransactionRequest(accountId, 2L, amount);
		new CreateNewTransactionService(transactionsRepository, accountsRepository, operationsRepository).create(request);
		
		assertEquals(Double.valueOf(0.0), transaction1.getBalance());
		assertEquals(Double.valueOf(-13.5), transaction2.getBalance());
		assertEquals(Double.valueOf(-18.7), transaction3.getBalance());
		
		verify(transactionsRepository, times(1)).save(transaction1);
		verify(transactionsRepository, times(1)).save(transaction2);
		verify(transactionsRepository, times(0)).save(transaction3);
		verify(transactionsRepository, times(1)).save(new Transaction(pagamento, account, amount, 0.0));
	}	
}