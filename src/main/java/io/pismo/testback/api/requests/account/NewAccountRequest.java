/**
 * 
 */
package io.pismo.testback.api.requests.account;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.pismo.testback.model.Account;

/**
 * @author victormartins
 *
 */
public class NewAccountRequest {

	@NotBlank(message = "Document number is mandatory.")
	@JsonProperty("document_number")
	private String documentNumber;

	@JsonProperty("limit")
	private Double limit;

	public NewAccountRequest() {
		super();
	}

	public NewAccountRequest(String documentNumber, Double limit) {
		super();
		this.documentNumber = documentNumber;
		this.limit = limit;		
	}

	@Override
	public String toString() {
		return this.documentNumber;
	}

	public Account asAccount() {
		return new Account(documentNumber, limit);
	}
}
