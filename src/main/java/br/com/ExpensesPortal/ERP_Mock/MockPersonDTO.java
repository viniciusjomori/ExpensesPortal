package br.com.ExpensesPortal.ERP_Mock;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MockPersonDTO(
    @NotNull UUID idPortal,
    @NotBlank String email,
    @NotBlank String cpf,
    @NotBlank String firstname,
    @NotBlank String lastname,
    boolean active
) {
    
}
