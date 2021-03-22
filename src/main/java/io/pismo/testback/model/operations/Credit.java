/**
 * 
 */
package io.pismo.testback.model.operations;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author victormartins
 *
 */
@Entity
@DiscriminatorValue("C")
public class Credit extends Operation{


	public Credit() {
		super();
	}

	public Credit(Long operationTypeId) {
		super(operationTypeId);
	}

	public Credit(String description) {
		super(description);
	}

	public Credit(Long id, String description) {
		super(id, description);
	}

	@Override
	public Double getFactor() {
		return -1.0;
	}
}