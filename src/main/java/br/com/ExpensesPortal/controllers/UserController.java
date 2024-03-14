package br.com.ExpensesPortal.controllers;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ExpensesPortal.DTOs.requests.UserRegisterDTO;
import br.com.ExpensesPortal.DTOs.requests.UserUpdateDTO;
import br.com.ExpensesPortal.DTOs.responses.UserResponseDTO;
import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.enums.AccessName;
import br.com.ExpensesPortal.mappers.UserMapper;
import br.com.ExpensesPortal.services.UserService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<Collection<UserResponseDTO>> findAll() {
        Collection<UserEntity> users = userService.findAll();
        return ResponseEntity.ok(userMapper.toResponseDTO(users));
    }

    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserEntity user = userService.getCurrentUser();
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }

    @GetMapping("/approvers")
    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<Collection<UserResponseDTO>> findAllApprovers() {
        Collection<UserEntity> approvers = userService.findAllByAccess(AccessName.APPROVER_EXPENSE);
        return ResponseEntity.ok(userMapper.toResponseDTO(approvers));
    }

    @GetMapping("/current/orderers")
    @PreAuthorize("hasAuthority('APPROVER_EXPENSE')")
    public ResponseEntity<Collection<UserResponseDTO>> findAllMyOrderers() {
        UserEntity user = userService.getCurrentUser();
        UserEntity reference = UserEntity.builder()
            .approver(user)
            .active(true)
            .build();
        Collection<UserEntity> orderers = userService.findAllByReference(reference);
        return ResponseEntity.ok(userMapper.toResponseDTO(orderers));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRegisterDTO user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserUpdateDTO dto, @PathVariable UUID id) {
        UserEntity user = userService.updateUser(dto, id);
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }
}