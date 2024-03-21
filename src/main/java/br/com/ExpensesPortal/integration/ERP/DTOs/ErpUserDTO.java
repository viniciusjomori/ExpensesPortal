package br.com.ExpensesPortal.integration.ERP.DTOs;

import java.util.UUID;

public record ErpUserDTO(
    UUID idPortal,
    String email,
    String cpf,
    String firstname,
    String lastname,
    boolean active
) {
    
}
