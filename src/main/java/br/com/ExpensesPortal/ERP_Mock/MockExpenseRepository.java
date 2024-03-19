package br.com.ExpensesPortal.ERP_Mock;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ExpensesPortal.enums.ExpenseStatus;

import java.util.UUID;
import java.util.Collection;

public interface MockExpenseRepository extends JpaRepository<MockExpense, UUID>{
    Collection<MockExpense> findAllByExpenseStatus(ExpenseStatus status);
}
