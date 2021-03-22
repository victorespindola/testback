/**
 * 
 */
package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import io.pismo.testback.TestbackApplication;
import io.pismo.testback.api.requests.account.NewAccountRequest;
import io.pismo.testback.api.requests.transaction.NewTransactionRequest;
import io.pismo.testback.api.responses.account.AccountResponse;
import io.pismo.testback.api.responses.transaction.TransactionResponse;
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
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = TestbackApplication.class)
@TestPropertySource("classpath:application_integration.properties")
public class MainFlowIntegrationTest {

	
	private static final String PAGAMENTO = "PAGAMENTO";

	private static final String SAQUE = "SAQUE";

	private static final String COMPRA_PARCELADA = "COMPRA PARCELADA";

	private static final String COMPRA_A_VISTA = "COMPRA A VISTA";

	@LocalServerPort
	Integer port;
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	OperationsRepository operationsRepository;
	
	@Autowired
	AccountsRepository accountsRepository;

	@Autowired
	TransactionsRepository transactionsRepository;
	
	private URI toURI(String uri) {
		return URI.create(String.format("http://localhost:%d/api/%s", port, uri));
	}

	@BeforeEach
	public void setup() {
		if(Objects.isNull(this.operationsRepository.findByDescription(COMPRA_A_VISTA))){
			this.operationsRepository.save(new Credit(COMPRA_A_VISTA));
		}
		
		if(Objects.isNull(this.operationsRepository.findByDescription(COMPRA_PARCELADA))){
			this.operationsRepository.save(new Credit(COMPRA_PARCELADA));
		}
		
		if(Objects.isNull(this.operationsRepository.findByDescription(SAQUE))){
			this.operationsRepository.save(new Credit(SAQUE));
		}
		
		if(Objects.isNull(this.operationsRepository.findByDescription(PAGAMENTO))){
			this.operationsRepository.save(new Debit(PAGAMENTO));
		}
	}
	
	@Test
	public void mainFlow() throws Exception {
		
		String documentNumber = "123456789";
		ResponseEntity<AccountResponse> response = restTemplate.postForEntity(toURI("accounts"), new NewAccountRequest(documentNumber, 500.00), AccountResponse.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		AccountResponse accountCreated = restTemplate.getForObject(toURI(String.format("accounts/%d", response.getBody().getAccountId())), AccountResponse.class);
		assertEquals(documentNumber, accountCreated.getDocumentNumber());
		
		Operation compraAVistaOperation = this.operationsRepository.findByDescription(COMPRA_A_VISTA);
		Operation pagamentoOperation = this.operationsRepository.findByDescription(PAGAMENTO);
		
		ResponseEntity<TransactionResponse> responseNewTransactionCredit = restTemplate.postForEntity(toURI("transactions"), new NewTransactionRequest(response.getBody().getAccountId(), compraAVistaOperation.getId(), 100.00), TransactionResponse.class);
		assertNotNull(responseNewTransactionCredit);
		assertEquals(HttpStatus.CREATED, responseNewTransactionCredit.getStatusCode());
		assertEquals(-100.00, responseNewTransactionCredit.getBody().getAmount());
		
		ResponseEntity<TransactionResponse> responseNewTransactionDebit = restTemplate.postForEntity(toURI("transactions"), new NewTransactionRequest(response.getBody().getAccountId(), pagamentoOperation.getId(), 123.45), TransactionResponse.class);
		assertNotNull(responseNewTransactionDebit);
		assertEquals(HttpStatus.CREATED, responseNewTransactionDebit.getStatusCode());
		assertEquals(123.45, responseNewTransactionDebit.getBody().getAmount());
	}
	
	
	@Test
	public void shouldNotPermitATransactionWithAmountGreaterThenAvailableLimit() throws Exception {
		
		String documentNumber = "123456789";
		Double limit = 100.00;
		
		ResponseEntity<AccountResponse> response = restTemplate.postForEntity(toURI("accounts"), new NewAccountRequest(documentNumber, limit), AccountResponse.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		AccountResponse accountCreated = restTemplate.getForObject(toURI(String.format("accounts/%d", response.getBody().getAccountId())), AccountResponse.class);
		assertEquals(documentNumber, accountCreated.getDocumentNumber());
		
		Operation compraAVistaOperation = this.operationsRepository.findByDescription(COMPRA_A_VISTA);
		
		Double amount = 110.00;
		ResponseEntity<String> responseNewTransactionCredit = restTemplate.postForEntity(toURI("transactions"), new NewTransactionRequest(response.getBody().getAccountId(), compraAVistaOperation.getId(), amount), String.class);
		assertNotNull(responseNewTransactionCredit);
		assertEquals(HttpStatus.BAD_REQUEST, responseNewTransactionCredit.getStatusCode());
		
		Account account = accountsRepository.findById(accountCreated.getAccountId()).get();
		assertEquals(limit, account.getLimit());
	}
	
	
	@Test
	public void shouldUpdateTransactionsWhenANewDebitTransactionIsCreated() throws Exception {
		
		String documentNumber = "123456789";
		Double limit = 100.00;
		
		ResponseEntity<AccountResponse> response = restTemplate.postForEntity(toURI("accounts"), new NewAccountRequest(documentNumber, limit), AccountResponse.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		AccountResponse accountCreated = restTemplate.getForObject(toURI(String.format("accounts/%d", response.getBody().getAccountId())), AccountResponse.class);
		assertEquals(documentNumber, accountCreated.getDocumentNumber());
		
		Operation compraAVistaOperation = this.operationsRepository.findByDescription(COMPRA_A_VISTA);
		Operation pagamento = this.operationsRepository.findByDescription(PAGAMENTO);
		
		restTemplate.postForEntity(toURI("transactions"), new NewTransactionRequest(response.getBody().getAccountId(), compraAVistaOperation.getId(), 50.0), TransactionResponse.class);
		restTemplate.postForEntity(toURI("transactions"), new NewTransactionRequest(response.getBody().getAccountId(), compraAVistaOperation.getId(), 23.5), TransactionResponse.class);
		restTemplate.postForEntity(toURI("transactions"), new NewTransactionRequest(response.getBody().getAccountId(), compraAVistaOperation.getId(), 18.7), TransactionResponse.class);
		
		ResponseEntity<TransactionResponse> responseNewTransactionCredit = restTemplate.postForEntity(toURI("transactions"), new NewTransactionRequest(response.getBody().getAccountId(), pagamento.getId(), 60.0), TransactionResponse.class);
		assertNotNull(responseNewTransactionCredit);
		assertEquals(HttpStatus.CREATED, responseNewTransactionCredit.getStatusCode());
		
		Account account = accountsRepository.findById(accountCreated.getAccountId()).get();
		assertEquals(Double.valueOf(67.8), account.getLimit());
		
		List<Transaction> accounts = this.transactionsRepository.findCreditsByAccountId(accountCreated.getAccountId());
		assertEquals(Double.valueOf(0.0), accounts.get(0).getBalance());
		assertEquals(Double.valueOf(-18.7), accounts.get(2).getBalance());
		assertEquals(Double.valueOf(-13.5), accounts.get(1).getBalance());
	}
}
