package br.com.ExpensesPortal.services;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ExpensesPortal.DTOs.requests.ExpenseRequestDTO;
import br.com.ExpensesPortal.entities.ExpenseEntity;
import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.enums.ExpenseStatus;
import br.com.ExpensesPortal.mappers.ExpenseMapper;
import br.com.ExpensesPortal.repositories.ExpenseRepository;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseMapper mapper;

    @Autowired
    private UserService userService;

    public ExpenseEntity findById(UUID id) {
        return expenseRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
    }

    public ExpenseEntity registerExpense(ExpenseRequestDTO dto) {
        ExpenseEntity expense = mapper.toEntity(dto);

        UserEntity orderer = userService.getCurrentUser();

        if(orderer.getApprover() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no appover");
        UserEntity approver = orderer.getApprover();

        expense.setExpenseStatus(ExpenseStatus.PENDING);
        expense.setOrderer(orderer);
        expense.setApprover(approver);

        return expenseRepository.save(expense);
    }

    public ExpenseEntity cancelExpense(UUID id) {
        ExpenseEntity expense = findById(id);
        if(currentUserIsOrderer(expense)) {
            expense.setExpenseStatus(ExpenseStatus.CANCELED);
            return expenseRepository.save(expense);
        }   
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public ExpenseEntity approveExpense(UUID id) {
        ExpenseEntity expense = findById(id);
        if(currentUserIsApprover(expense)) {
            expense.setExpenseStatus(ExpenseStatus.WAITING_PAYMENT);
            expense.setApprovalDate(LocalDateTime.now());
            return expenseRepository.save(expense);
        }   
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public ExpenseEntity refuseExpense(UUID id) {
        ExpenseEntity expense = findById(id);
        if(currentUserIsApprover(expense)) {
            expense.setExpenseStatus(ExpenseStatus.REFUSED);
            return expenseRepository.save(expense);
        }   
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public ExpenseEntity notifyPayment(UUID id) {
        ExpenseEntity expense = findById(id);
        expense.setExpenseStatus(ExpenseStatus.PAYED);
        return expenseRepository.save(expense);
    }

    public Collection<ExpenseEntity> findAllByReference(ExpenseEntity reference) {
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues();
        Example<ExpenseEntity> example = Example.of(reference, matcher);
        return expenseRepository.findAll(example);
    }

    public boolean currentUserIsOrderer(ExpenseEntity expense) {
        return expenseHasCurrentUser(expense, ExpenseEntity::getOrderer);
    }

    public boolean currentUserIsApprover(ExpenseEntity expense) {
        return expenseHasCurrentUser(expense, ExpenseEntity::getApprover);
    }

    private boolean expenseHasCurrentUser(ExpenseEntity expense, Function<ExpenseEntity, UserEntity> expenseFunction) {
        UserEntity user = userService.getCurrentUser();
        return
            expense.getActive() &&
            expense.getExpenseStatus().equals(ExpenseStatus.PENDING) &&
            user.equals(expenseFunction.apply(expense));
    }

}
