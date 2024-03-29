package br.com.ExpensesPortal.ERP_Mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.ExpensesPortal.DTOs.requests.HttpDTO;
import br.com.ExpensesPortal.DTOs.requests.LoginRequestDTO;
import br.com.ExpensesPortal.enums.ExpenseStatus;
import br.com.ExpensesPortal.utils.RequestUtil;

import java.io.IOException;
import java.util.Collection;

@Component
public class PayExpense {

    @Autowired
    private MockExpenseRepository expenseRepository;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private Gson gson;

    private String token;
    
    @Scheduled(fixedRate = (1000 * 60 * 2)) // 2 minutes
    public void payExpense() {
        if(token == null) login();

        Collection<MockExpense> expenses = expenseRepository.findAllByExpenseStatus(ExpenseStatus.WAITING_PAYMENT);
        expenses.forEach(e -> {
            e.setExpenseStatus(ExpenseStatus.PAYED);
            expenseRepository.save(e);
            try {
                HttpDTO httpDTO = requestUtil.request(
                    "http://localhost:8080/expense/"+e.getIdPortal()+"/notify_payment",
                    HttpMethod.POST,
                    null,
                    token
                );
                System.out.println(httpDTO.toString());
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Scheduled(fixedDelay = (1000 * 60 * 10)) // 10 minutes
    public void login() {
        LoginRequestDTO login = new LoginRequestDTO("erp.test@email.com", "password");
        String loginJson = gson.toJson(login);
        try {
            HttpDTO httpResponse = requestUtil.request(
                "http://localhost:8080/auth/login",
                HttpMethod.POST,
                loginJson,
                token
            );
            JsonObject jsonBody = gson.fromJson(httpResponse.jsonBody(), JsonObject.class);
            token = jsonBody.get("token").getAsString();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

}