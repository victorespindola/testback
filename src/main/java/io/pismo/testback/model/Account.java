/**
 * 
 */
package io.pismo.testback.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author victormartins
 *
 */
@Entity
@Table(name = "accounts")
public class Account {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false, length = 50)
	private String document;

	public Account() {
		super();
	}

	public Account(Long id, String document) {
		super();
		this.id = id;
		this.document = document;
	}
	
	public Account(String document) {
		super();
		this.document = document;
	}

	public Long getId() {
		return id;
	}

	public String getDocument() {
		return document;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(13,31).append(document)
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
		Account other = (Account) obj;
		return new EqualsBuilder().append(this.document, other.document)
								  .isEquals();
	}

	@Override
	public String toString() {
		return this.document;
	}
}