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

	public AccountResponse(Long accountId, String documentNumber) {
		super();
		this.accountId = accountId;
		this.documentNumber = documentNumber;
	}
	
	public AccountResponse(Account account) {
		this(account.getId(), String.valueOf(account.getDocument()));
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