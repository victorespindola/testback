/**
 * 
 */
package io.pismo.testback.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.pismo.testback.model.Transaction;

/**
 * @author victormartins
 *
 */
@Repository
public interface TransactionsRepository extends CrudRepository<Transaction, Long> {

}
