package br.com.ExpensesPortal.listeners;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import br.com.ExpensesPortal.entities.ExpenseEntity;
import br.com.ExpensesPortal.enums.ExpenseStatus;
import br.com.ExpensesPortal.integration.ERP.services.ErpExpenseService;
import jakarta.persistence.PreUpdate;

@Component
@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class ExpenseListener {
    
    private static ErpExpenseService erpExpenseService;

    @Autowired
    public void init(ErpExpenseService erpExpenseService) {
        ExpenseListener.erpExpenseService = erpExpenseService;
    }

    @PreUpdate
    public void onUpdate(ExpenseEntity expense) {
        if(expense.getActive() && expense.getExpenseStatus().equals(ExpenseStatus.WAITING_PAYMENT))
            erpExpenseService.requestToRegisterExpense(expense);
    }
}