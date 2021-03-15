/**
 * 
 */
package io.pismo.testback.model.operations;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * @author victormartins
 *
 */
@Entity
@Table(name = "operations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Operation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String description;

	public Operation() {
		super();
	}

	public Operation(Long operationTypeId) {
		this.id = operationTypeId;
	}

	public Operation(String description) {
		super();
		this.description = description;
	}
	
	public abstract Double getFactor();

	public Long getId() {
		return id;
	}
}
