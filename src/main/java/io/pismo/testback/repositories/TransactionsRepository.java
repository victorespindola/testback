/**
 * 
 */
package io.pismo.testback.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.pismo.testback.model.Transaction;

/**
 * @author victormartins
 *
 */
@Repository
public interface TransactionsRepository extends CrudRepository<Transaction, Long> {

	@Query("from Transaction t where t.account.id = :accountId")
	List<Transaction> findCreditsByAccountId(long accountId);

}
