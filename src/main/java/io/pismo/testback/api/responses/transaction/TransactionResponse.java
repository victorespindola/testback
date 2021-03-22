/**
 * 
 */
package io.pismo.testback.api.responses.transaction;

import io.pismo.testback.model.Transaction;

/**
 * @author victormartins
 *
 */

public class TransactionResponse {
	
	private Long transactionId;
	
	private Long accountId;
	
	private Long operationTypeId;
	
	private Double amount;

	
	
	public TransactionResponse() {
		super();
	}

	public TransactionResponse(Transaction transaction) {
		this.transactionId = transaction.getId();
		this.accountId = transaction.getAccountId();
		this.operationTypeId = transaction.getOperationId();
		this.amount = transaction.getAmount();
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
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
}