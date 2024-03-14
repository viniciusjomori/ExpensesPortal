package br.com.ExpensesPortal.DTOs.infos;

import java.util.UUID;

public record UserInfo(
    UUID id,
    String firstname,
    String lastname
) {
    
}
