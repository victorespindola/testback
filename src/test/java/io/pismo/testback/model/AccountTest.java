/**
 * 
 */
package io.pismo.testback.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import io.pismo.testback.exceptions.TransactionAmountGreaterThanLimitException;

/**
 * @author victormartins
 *
 */
public class AccountTest {

	
	@Test
	public void shouldBeTrueWhenAmountIsGreaterThanLimit() throws Exception {
		Double limit = 100.00;
		String document = "123456";
		Double amount = -110.00;
		Long id = 1L;
		
		assertTrue(new Account(id, document, limit).isGreaterThanLimit(amount));
	}
	
	@Test
	public void shouldBeFalseWhenAmountIsLessThanLimit() throws Exception {
		Double limit = 100.00;
		String document = "123456";
		Double amount = 90.00;
		Long id = 1L;
		
		assertFalse(new Account(id, document, limit).isGreaterThanLimit(amount));
	}
	
	@Test
	public void shouldBeFalseWhenAmountIsEqualThanLimit() throws Exception {
		Double limit = 100.00;
		String document = "123456";
		Double amount = 100.00;
		Long id = 1L;
		
		assertFalse(new Account(id, document, limit).isGreaterThanLimit(amount));
	}
	
	@Test
	public void shouldUpdateLimit() throws Exception {
		Account account = new Account(1L, "1234", 100.00);
		assertEquals(70.0, account.updateLimit(-30.0).getLimit());
		assertEquals(90.0, account.updateLimit(20.0).getLimit());
	}
	
	@Test
	public void shouldThrowsAnExceptionWhenAmountIsGreaterThanAvailableLimit() throws Exception {
		assertThrows(TransactionAmountGreaterThanLimitException.class, () -> {
			new Account(1L, "1234", 100.00).updateLimit(-110.0);
			fail();
		});
	}	
}