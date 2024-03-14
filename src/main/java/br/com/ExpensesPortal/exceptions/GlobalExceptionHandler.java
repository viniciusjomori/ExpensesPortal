package br.com.ExpensesPortal.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.ExpensesPortal.DTOs.responses.ResponseMessage;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ResponseMessage responseMessage;
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseMessage> handleResponseStatusException(ResponseStatusException ex) {
        responseMessage.setMessage(ex.getReason());
        responseMessage.setHttpStatus(ex.getStatusCode());
        return new ResponseEntity<ResponseMessage>(responseMessage, ex.getStatusCode());
    }

}