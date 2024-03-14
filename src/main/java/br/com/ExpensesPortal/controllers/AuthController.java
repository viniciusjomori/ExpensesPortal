package br.com.ExpensesPortal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ExpensesPortal.DTOs.requests.LoginRequestDTO;
import br.com.ExpensesPortal.DTOs.responses.ResponseMessage;
import br.com.ExpensesPortal.DTOs.responses.TokenResponseDTO;
import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.services.AuthService;
import br.com.ExpensesPortal.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseMessage responseMessage;

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<TokenResponseDTO> authenticate(@RequestBody @Valid LoginRequestDTO login) {
        return ResponseEntity.ok(authService.authenticate(login));
    }

    @PostMapping("logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessage> logout() {
        UserEntity user = userService.getCurrentUser();
        authService.revokeAllTokens(user);
        responseMessage.setHttpStatus(HttpStatus.OK);
        responseMessage.setMessage("Logout success");
        return ResponseEntity.ok(responseMessage);
    }

}
