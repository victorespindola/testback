/**
 * 
 */
package io.pismo.testback.api.requests.transaction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author victormartins
 *
 */
public class NewTransactionRequest {

	@Positive
	@NotNull
	@JsonProperty("account_id")
	private Long accountId;
	
	@Positive
	@NotNull
	@JsonProperty("operation_type_id")
	private Long operationTypeId;

	@NotNull
	@PositiveOrZero
	@JsonProperty("amount")
	private Double amount;
	
	
	public NewTransactionRequest() {
		super();
	}

	public NewTransactionRequest(Long accountId, Long operationTypeId, Double amount) {
		super();
		this.accountId = accountId;
		this.operationTypeId = operationTypeId;
		this.amount = amount;
	}


	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getOperationTypeId() {
		return operationTypeId;
	}

	public void setOperationTypeId(Long operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String asJson() throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(13, 31).append(this.accountId)
										  .append(this.operationTypeId)
										  .append(this.amount)
										  .toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewTransactionRequest other = (NewTransactionRequest) obj;
		return new EqualsBuilder().append(this.accountId, other.accountId)
								  .append(this.operationTypeId, other.operationTypeId)
								  .append(this.amount, other.amount)
								  .isEquals();
	}
	
	
}