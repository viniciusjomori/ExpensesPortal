package br.com.ExpensesPortal.integration.ERP.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import br.com.ExpensesPortal.entities.ExpenseEntity;
import br.com.ExpensesPortal.enums.RequestType;
import br.com.ExpensesPortal.integration.ERP.DTOs.ErpExpenseDTO;
import br.com.ExpensesPortal.integration.ERP.mappers.ErpExpenseMapper;
import br.com.ExpensesPortal.services.RequestService;

@Service
public class ErpExpenseService {
 
    @Autowired
    private RequestService requestService;

    @Autowired
    private ErpExpenseMapper mapper;

    @Value("${application.erp-integration.endpoints.register-expense}")
    private String requestToRegisterExpenseUrl;

    public void requestToRegisterExpense(ExpenseEntity entity) {
        ErpExpenseDTO dto = mapper.toDTO(entity);
        requestService.createRequestEntity(entity, dto, HttpMethod.POST, requestToRegisterExpenseUrl, RequestType.REGISTER_EXPENSE);
    }
}
