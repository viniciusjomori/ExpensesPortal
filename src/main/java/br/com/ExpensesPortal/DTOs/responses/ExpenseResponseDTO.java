package br.com.ExpensesPortal.DTOs.responses;

import br.com.ExpensesPortal.DTOs.infos.UserInfo;
import br.com.ExpensesPortal.enums.ExpenseStatus;
import br.com.ExpensesPortal.enums.UnitType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponseDTO(
    UUID id,
    UserInfo orderer,
    UserInfo approver,
    String productDesc,
    UnitType unitType,
    double unitQnt,
    double valuePerUnit,
    LocalDateTime createDate,
    LocalDateTime approvalDate,
    ExpenseStatus expenseStatus
) {
    
}
