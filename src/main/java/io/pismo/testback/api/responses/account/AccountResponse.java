/**
 * 
 */
package io.pismo.testback.api.responses.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.pismo.testback.model.Account;

/**
 * @author victormartins
 *
 */
public class AccountResponse {
	
	@JsonProperty(value = "account_id")
	private Long accountId;
	
	@JsonProperty(value = "document_number")
	private String documentNumber;
	
	@JsonProperty(value = "limit")
	private Double limit;

	public AccountResponse(Long accountId, String documentNumber, Double limit) {
		super();
		this.accountId = accountId;
		this.documentNumber = documentNumber;
		this.limit = limit;
	}
	
	public AccountResponse(Account account) {
		this(account.getId(), String.valueOf(account.getDocument()), account.getLimit());
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
}