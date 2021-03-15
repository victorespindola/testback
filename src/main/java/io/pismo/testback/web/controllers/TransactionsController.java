/**
 * 
 */
package io.pismo.testback.web.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pismo.testback.api.requests.transaction.NewTransactionRequest;
import io.pismo.testback.api.responses.transaction.TransactionResponse;
import io.pismo.testback.services.transaction.CreateNewTransactionService;

/**
 * @author victormartins
 *
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
	
	@Autowired
	private CreateNewTransactionService createNewTransactionService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionResponse> create(@Valid @RequestBody NewTransactionRequest transactionRequest) {
		return new ResponseEntity<TransactionResponse>(new TransactionResponse(this.createNewTransactionService.create(transactionRequest)), HttpStatus.CREATED);
	}
}