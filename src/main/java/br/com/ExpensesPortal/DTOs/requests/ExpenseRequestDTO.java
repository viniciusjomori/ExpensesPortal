
package br.com.ExpensesPortal.DTOs.requests;

import br.com.ExpensesPortal.enums.UnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExpenseRequestDTO(
   @NotBlank String productDesc,
   @NotNull UnitType unitType,
   @NotNull int unitQnt,
   @NotNull double valuePerUnit
) {}
