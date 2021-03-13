/**
 * 
 */
package io.pismo.testback.api.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.pismo.testback.api.requests.account.NewAccountRequest;
import io.pismo.testback.model.Account;
import io.pismo.testback.repositories.AccountsRepository;

/**
 * @author victormartins
 *
 */
@WebMvcTest(AccountsController.class)
public class AccountsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	AccountsRepository accountsRepository;
	
	@Test
	public void shouldReturnAccountWithPassedId() throws Exception {
		Long accountId = 1L;
		String document = "12345678900";
		
		Optional<Account> accountWithId_1 = Optional.of(new Account(accountId, document));
		when(accountsRepository.findById(accountId)).thenReturn(accountWithId_1);
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/accounts/%d",accountId)))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(content().json("{\"account_id\": 1, \"document_number\": \"12345678900\"}"))
					.andReturn();
		
		verify(accountsRepository, times(1)).findById(accountId);
	}
	
	@Test
	public void shouldReturnNotFoundWhenAccountWithPassedIdHasNotBeenFound() throws Exception {
		Long accountId = 5L;
		
		when(accountsRepository.findById(accountId)).thenReturn(Optional.empty());
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/accounts/%d",accountId)))
					.andExpect(status().isNotFound())
					.andDo(print())
					.andReturn();
		
		verify(accountsRepository, times(1)).findById(accountId);
	}
	
	@Test
	public void shouldCreateANewAccount() throws Exception {
		Long accountId = 1L;
		String document = "12345678900";
		
		Account accountToSave = new Account(document);
		Account accountWithId_1 = new Account(accountId, document);
		when(accountsRepository.save(accountToSave)).thenReturn(accountWithId_1);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("/api/accounts"))
												   .contentType(MediaType.APPLICATION_JSON)
												   .content(toJson(new NewAccountRequest(document))))													
					.andExpect(status().isCreated())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(content().json("{\"account_id\": 1, \"document_number\": \"12345678900\"}"))
					.andReturn();
		
		verify(accountsRepository, times(1)).save(accountToSave);
	}
	
	@Test
	public void shouldNotCreateANewAccountWithNoDocumentNumber() throws Exception {
		String document = null;
		
		this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("/api/accounts"))
												   .contentType(MediaType.APPLICATION_JSON)
												   .content(toJson(new NewAccountRequest(document))))													
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andReturn();
		
		verify(accountsRepository, times(0)).save(Mockito.any());
	}
	
	private String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}
}