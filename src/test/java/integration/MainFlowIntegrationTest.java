/**
 * 
 */
package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
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
import io.pismo.testback.model.operations.Credit;
import io.pismo.testback.model.operations.Debit;
import io.pismo.testback.model.operations.Operation;
import io.pismo.testback.repositories.OperationsRepository;

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
		ResponseEntity<AccountResponse> response = restTemplate.postForEntity(toURI("accounts"), new NewAccountRequest(documentNumber), AccountResponse.class);
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
}