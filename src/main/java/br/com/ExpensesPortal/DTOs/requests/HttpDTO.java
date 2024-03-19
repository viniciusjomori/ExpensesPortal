package br.com.ExpensesPortal.DTOs.requests;

import org.springframework.http.HttpStatusCode;

public record HttpDTO(
    String bearerToken,
    HttpStatusCode httpStatus,
    String jsonBody
    ) {
}
