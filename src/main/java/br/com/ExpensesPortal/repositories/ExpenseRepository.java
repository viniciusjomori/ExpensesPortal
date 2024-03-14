
package br.com.ExpensesPortal.repositories;

import br.com.ExpensesPortal.entities.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {

}
