/**
 * 
 */
package io.pismo.testback.api.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.pismo.testback.api.requests.transaction.NewTransactionRequest;
import io.pismo.testback.exceptions.DependencyNotFoundException;
import io.pismo.testback.model.Transaction;
import io.pismo.testback.services.transaction.CreateNewTransactionService;

/**
 * @author victormartins
 *
 */
@WebMvcTest(TransactionsController.class)
public class TransactionsControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	CreateNewTransactionService createNewTransactionService;
	
	@Test
	public void shouldCreateANewTransaction() throws Exception {
		
		Transaction transaction = mock(Transaction.class);
		NewTransactionRequest request = new NewTransactionRequest(1L, 4L, 123.45);
		when(createNewTransactionService.create(request)).thenReturn(transaction);
		
		this.mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
												  .content(request.asJson()))
					.andExpect(status().isCreated())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))					
					.andReturn();
		
		verify(this.createNewTransactionService, times(1)).create(request);
	}
	
	@Test
	public void shouldReturnBadRequestWhenCreateANewTransactionWithAInexistentDependency() throws Exception {
		
		NewTransactionRequest request = new NewTransactionRequest(1L, 4L, 123.45);
		when(createNewTransactionService.create(request)).thenThrow(DependencyNotFoundException.class);
		
		this.mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
												  .content(request.asJson()))
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andReturn();
		
		verify(this.createNewTransactionService, times(1)).create(request);
	}
	
	@Test
	public void shouldReturnBadRequestWhenCreateANewTransactionWithANegativeAmount() throws Exception {
		
		NewTransactionRequest request = new NewTransactionRequest(1L, 4L, -123.45);
		
		this.mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
												  .content(request.asJson()))
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andReturn();
		
		verify(this.createNewTransactionService, times(0)).create(request);
	}
	
	@Test
	public void shouldReturnBadRequestWhenCreateANewTransactionWithoutAnOperation() throws Exception {
		
		NewTransactionRequest request = new NewTransactionRequest(1L, null, 123.45);
		
		this.mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
												  .content(request.asJson()))
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andReturn();
		
		verify(this.createNewTransactionService, times(0)).create(request);
	}
	
	@Test
	public void shouldReturnBadRequestWhenCreateANewTransactionWithANegativeOperationId() throws Exception {
		
		NewTransactionRequest request = new NewTransactionRequest(1L, -1L, 123.45);
		
		this.mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
												  .content(request.asJson()))
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andReturn();
		
		verify(this.createNewTransactionService, times(0)).create(request);
	}
	
	@Test
	public void shouldReturnBadRequestWhenCreateANewTransactionWithoutAnAccount() throws Exception {
		
		NewTransactionRequest request = new NewTransactionRequest(null, 1L, 123.45);
		
		this.mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
												  .content(request.asJson()))
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andReturn();
		
		verify(this.createNewTransactionService, times(0)).create(request);
	}
	
	@Test
	public void shouldReturnBadRequestWhenCreateANewTransactionWithANegativeAccountId() throws Exception {
		
		NewTransactionRequest request = new NewTransactionRequest(-1L, 1L, 123.45);
		
		this.mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON_VALUE)
												  .content(request.asJson()))
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andReturn();
		
		verify(this.createNewTransactionService, times(0)).create(request);
	}
}