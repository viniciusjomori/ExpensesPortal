
package br.com.ExpensesPortal.DTOs.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterDTO(
   @NotBlank String email,
   @NotBlank String password,
   @NotBlank String cpf,
   @NotBlank String firstname,
   @NotBlank String lastname,
   @NotNull UUID roleId,
   UUID approverId
) {}
