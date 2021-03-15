/**
 * 
 */
package io.pismo.testback.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;

import io.pismo.testback.model.operations.Operation;

/**
 * @author victormartins
 *
 */
@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "operation_id")
	private Operation operation;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@Column(nullable = false, precision = 2)
	private Double amount;
	
	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime eventTimestamp;

	
	
	public Transaction() {
		super();
	}

	public Transaction(Operation operation, Account account, Double amount) {
		super();
		this.operation = operation;
		this.account = account;
		this.amount = amount * operation.getFactor();
	}

	public Double getAmount() {
		return amount;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(13, 31).append(this.id)
				  						  .append(this.account)
				  						  .append(this.amount)
				  						  .append(this.operation)
				  						  .append(this.eventTimestamp)
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
		Transaction other = (Transaction) obj;
		return new EqualsBuilder().append(this.id, other.id)
								  .append(this.account, other.account)
								  .append(this.amount, other.amount)
								  .append(this.operation, other.operation)
								  .append(this.eventTimestamp, other.eventTimestamp)
								  .isEquals();
	}

	public Long getOperationId() {
		return Objects.nonNull(operation) ? operation.getId() : null;
	}

	public Long getAccountId() {
		return Objects.nonNull(account) ? account.getId() : null;
	}
	
	
}
