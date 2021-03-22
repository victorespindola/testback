/**
 * 
 */
package io.pismo.testback.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.pismo.testback.model.operations.Credit;
import io.pismo.testback.model.operations.Debit;
import io.pismo.testback.model.operations.Operation;

/**
 * @author victormartins
 *
 */
public class TransactionTest {

	
	@Test
	public void amountShouldBeNegativeWhenOperationIsCredit() throws Exception  {
		Operation operation = new Credit(1L);
		Account account = new Account(1L, "1234567890");
		assertEquals(-100.00, new Transaction(operation, account, 100.00).getAmount());
	}
	
	@Test
	public void amountShouldBePositiveWhenOperationIsDebit() throws Exception  {
		Operation operation = new Debit(1L);
		Account account = new Account(1L, "1234567890");
		assertEquals(100.00, new Transaction(operation, account, 100.00).getAmount());
	}
	
	@Test
	public void shouldUpdateBalance() throws Exception {
		Account account = new Account(1L, "1234567890");
		Double operationAmount = 60.00;
		Transaction transaction = new Transaction(new Credit(1L), account, 100.00);
		assertEquals(Double.valueOf(0.0), transaction.updateBalance(operationAmount));
		assertEquals(Double.valueOf(-40.0), transaction.getBalance());
		
		Double operationAmount2 = 110.00;
		Transaction transaction2 = new Transaction(new Credit(1L), account, 100.00);
		assertEquals(Double.valueOf(10.0), transaction2.updateBalance(operationAmount2));
		assertEquals(Double.valueOf(0.0), transaction2.getBalance());
		
		Double operationAmount3 = 100.00;
		Transaction transaction3 = new Transaction(new Credit(1L), account, 100.00);
		assertEquals(Double.valueOf(0.0), transaction3.updateBalance(operationAmount3));
		assertEquals(Double.valueOf(0.0), transaction3.getBalance());
	}
}