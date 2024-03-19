
package br.com.ExpensesPortal.repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ExpensesPortal.entities.RequestEntity;
import br.com.ExpensesPortal.enums.RequestStatus;


public interface RequestRepository extends JpaRepository<RequestEntity, Long> {

    @Query("SELECT r FROM RequestEntity r WHERE r.status = :status AND r.active = true")
    Collection<RequestEntity> findActiveByStatus(@Param("status") RequestStatus status);
}