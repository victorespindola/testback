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
@DiscriminatorValue("D")
public class Debit extends Operation{

	public Debit() {
		super();
	}

	public Debit(Long operationTypeId) {
		super(operationTypeId);
	}

	public Debit(String description) {
		super(description);
	}

	@Override
	public Double getFactor() {
		return 1.0;
	}
}
