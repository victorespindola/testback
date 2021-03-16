/**
 * 
 */
package io.pismo.testback.api.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pismo.testback.api.requests.account.NewAccountRequest;
import io.pismo.testback.api.responses.account.AccountResponse;
import io.pismo.testback.model.Account;
import io.pismo.testback.repositories.AccountsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author victormartins
 *
 */
@RestController
@RequestMapping("/api/accounts")
@Api("Accounts")
public class AccountsController {
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@ApiOperation(value = "Finds accounts by id.")
	@GetMapping("/{id}")	
	public AccountResponse get(@PathVariable Long id) {
		return new AccountResponse(accountsRepository.findById(id).get());
	}
	
	@ApiOperation(value = "Creates new accounts.")
	@PostMapping
	public ResponseEntity<AccountResponse> create(@Valid @RequestBody NewAccountRequest newAccountRequest) {
		Account accountCreated = this.accountsRepository.save(newAccountRequest.asAccount());
		return ResponseEntity.created(URI.create(String.format("/api/accounts/%d", accountCreated.getId()))).body(new AccountResponse(accountCreated));
	}
}