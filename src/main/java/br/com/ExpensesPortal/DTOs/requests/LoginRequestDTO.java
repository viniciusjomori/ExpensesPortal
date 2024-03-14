
package br.com.ExpensesPortal.DTOs.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
   @NotBlank String email,
   @NotBlank String password
) {}
