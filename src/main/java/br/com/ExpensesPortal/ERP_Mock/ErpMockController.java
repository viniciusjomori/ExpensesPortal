package br.com.ExpensesPortal.ERP_Mock;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.ExpensesPortal.DTOs.responses.ResponseMessage;
import jakarta.annotation.security.PermitAll;

import java.util.Random;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/erp")
public class ErpMockController {
    
    @Autowired
    private MockExpenseRepository expenseRepository;

    @Autowired
    private MockPersonRepository personRepository;

    @Autowired
    private ResponseMessage responseMessage;

    @PostMapping("/person")
    @PermitAll
    public ResponseEntity<ResponseMessage> registerPerson(@RequestBody MockPersonDTO dto) {
        if(simulateConnection()) {
            MockPerson person = new MockPerson();
            BeanUtils.copyProperties(dto, person);
            personRepository.save(person);
            responseMessage.setHttpStatus(HttpStatus.OK);
            responseMessage.setMessage("Person registred succesfully");
            return ResponseEntity.ok(responseMessage);
        } else {
            responseMessage.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseMessage.setMessage("Connection error");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/person/{idPortal}")
    @PermitAll
    public ResponseEntity<ResponseMessage> updatePerson(@RequestBody MockPersonDTO newPerson, @PathVariable UUID idPortal) {
        if(simulateConnection()) {
            MockPerson oldPerson = findByidPortal(idPortal);
            BeanUtils.copyProperties(newPerson, oldPerson);
            personRepository.save(oldPerson);

            responseMessage.setHttpStatus(HttpStatus.OK);
            responseMessage.setMessage("Person updated succesfully");
            return ResponseEntity.ok(responseMessage);
        } else {
            responseMessage.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseMessage.setMessage("Connection error");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/expense")
    @PermitAll
    public ResponseEntity<ResponseMessage> registerExpense(@RequestBody MockExpenseDTO dto) {
        if(simulateConnection()) {
            MockPerson approver = findByidPortal(dto.ordererPortalId());
            MockPerson orderer  = findByidPortal(dto.approverPortalId());

            MockExpense expense = new MockExpense();
            BeanUtils.copyProperties(dto, expense);
            expense.setMockApprover(approver);
            expense.setMockOrderer(orderer);
            expenseRepository.save(expense);

            responseMessage.setHttpStatus(HttpStatus.OK);
            responseMessage.setMessage("Expense registred succesfully");
            return ResponseEntity.ok(responseMessage);
        } else {
            responseMessage.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseMessage.setMessage("Connection error");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean simulateConnection() {
        Random rand = new Random();
        return rand.nextInt(100)+1 > 50;
    }

    private MockPerson findByidPortal(UUID idPortal) {
        return personRepository.findByIdPortal(idPortal)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
    }

}
