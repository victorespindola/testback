/**
 * 
 */
package io.pismo.testback.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.pismo.testback.model.Account;

/**
 * @author victormartins
 *
 */
@Repository
public interface AccountsRepository extends CrudRepository<Account, Long> {

}
