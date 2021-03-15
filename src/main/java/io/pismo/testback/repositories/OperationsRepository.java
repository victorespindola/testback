/**
 * 
 */
package io.pismo.testback.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.pismo.testback.model.operations.Operation;

/**
 * @author victormartins
 *
 */
@Repository
public interface OperationsRepository extends CrudRepository<Operation, Long> {
	
	Operation findByDescription(String description);
}
