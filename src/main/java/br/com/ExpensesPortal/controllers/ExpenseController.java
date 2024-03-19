package br.com.ExpensesPortal.controllers;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import br.com.ExpensesPortal.DTOs.requests.ExpenseRequestDTO;
import br.com.ExpensesPortal.DTOs.responses.ExpenseResponseDTO;
import br.com.ExpensesPortal.DTOs.responses.ResponseMessage;
import br.com.ExpensesPortal.entities.ExpenseEntity;
import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.enums.ExpenseStatus;
import br.com.ExpensesPortal.enums.UnitType;
import br.com.ExpensesPortal.mappers.ExpenseMapper;
import br.com.ExpensesPortal.services.ExpenseService;
import br.com.ExpensesPortal.services.UserService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/expense")
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseMapper expenseMapper;

    @Autowired
    private ResponseMessage responseMessage;

    @PostMapping
    @PreAuthorize("hasAuthority('REGISTER_EXPENSE')")
    public ResponseEntity<ExpenseResponseDTO> registerExpense(@RequestBody @Valid ExpenseRequestDTO dto) {
        ExpenseEntity expense = expenseService.registerExpense(dto);
        return ResponseEntity.ok(expenseMapper.toResponseDTO(expense));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('REGISTER_EXPENSE')")
    public ResponseEntity<Collection<ExpenseResponseDTO>> getCurrentUserExpenses() {
        UserEntity user = userService.getCurrentUser();
        ExpenseEntity reference = ExpenseEntity.builder()
            .orderer(user)
            .build();
        Collection<ExpenseEntity> expenses = expenseService.findAllByReference(reference);
        return ResponseEntity.ok(expenseMapper.toResponseDTO(expenses));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('REGISTER_EXPENSE')")
    public ResponseEntity<ExpenseResponseDTO> cancelExpense(@PathVariable UUID id) {
        ExpenseEntity expense = expenseService.cancelExpense(id);
        return ResponseEntity.ok(expenseMapper.toResponseDTO(expense));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('APPROVER_EXPENSE')")
    public ResponseEntity<ExpenseResponseDTO> approveExpense(@PathVariable UUID id) {
        ExpenseEntity expense = expenseService.approveExpense(id);
        return ResponseEntity.ok(expenseMapper.toResponseDTO(expense));
    }

    @PutMapping("/{id}/refuse")
    @PreAuthorize("hasAuthority('APPROVER_EXPENSE')")
    public ResponseEntity<ExpenseResponseDTO> refuseExpense(@PathVariable UUID id) {
        ExpenseEntity expense = expenseService.refuseExpense(id);
        return ResponseEntity.ok(expenseMapper.toResponseDTO(expense));
    }

    @GetMapping("/below")
    @PreAuthorize("hasAuthority('APPROVER_EXPENSE')")
    public ResponseEntity<Collection<ExpenseResponseDTO>> findExpensesBelow() {
        UserEntity user = userService.getCurrentUser();
        ExpenseEntity reference = ExpenseEntity.builder()
            .active(true)
            .approver(user)
            .expenseStatus(ExpenseStatus.PENDING)
            .build();
        Collection<ExpenseEntity> expenses = expenseService.findAllByReference(reference);
        return ResponseEntity.ok(expenseMapper.toResponseDTO(expenses));
    }

    @GetMapping("unit")
    @PreAuthorize("hasAuthority('REGISTER_EXPENSE')")
    public ResponseEntity<UnitType[]> findUnitTypes() {
        return ResponseEntity.ok(UnitType.values());
    }

    @PostMapping("{id}/notify_payment")
    @PreAuthorize("hasAuthority('NOTIFY_PAYMENT')")
    public ResponseEntity<ResponseMessage> notifyPayment(@PathVariable UUID id) {
        expenseService.notifyPayment(id);
        responseMessage.setMessage("Expense was payed");
        responseMessage.setHttpStatus(HttpStatus.OK);
        return ResponseEntity.ok(responseMessage);
    }
}