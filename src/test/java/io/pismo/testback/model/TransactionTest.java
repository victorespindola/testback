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
}