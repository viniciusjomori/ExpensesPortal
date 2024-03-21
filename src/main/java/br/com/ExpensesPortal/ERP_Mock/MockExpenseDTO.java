package br.com.ExpensesPortal.ERP_Mock;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.ExpensesPortal.enums.ExpenseStatus;
import br.com.ExpensesPortal.enums.UnitType;

public record MockExpenseDTO(
    UUID idPortal,
    UUID ordererPortalId,
    UUID approverPortalId,
    String productDesc,
    UnitType unitType,
    Double unitQnt,
    Double valuePerUnit,
    LocalDateTime approvalDate,
    ExpenseStatus expenseStatus
) {
    
}
