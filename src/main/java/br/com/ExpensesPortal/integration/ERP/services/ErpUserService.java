package br.com.ExpensesPortal.integration.ERP.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.enums.RequestType;
import br.com.ExpensesPortal.integration.ERP.DTOs.ErpUserDTO;
import br.com.ExpensesPortal.integration.ERP.mappers.ErpUserMapper;
import br.com.ExpensesPortal.services.RequestService;

@Service
public class ErpUserService {

    @Autowired
    public RequestService requestService;

    @Autowired
    public ErpUserMapper mapper;

    @Value("${application.erp-integration.endpoints.register-person}")
    private String requestToRegisterPersonUrl;

    @Value("${application.erp-integration.endpoints.update-person}")
    private String requestToUpdatePersonUrl;

    public void requestToRegisterOrderer(UserEntity user) {
        ErpUserDTO dto = mapper.toDTO(user);
        requestService.createRequestEntity(user, dto, HttpMethod.POST, requestToRegisterPersonUrl, RequestType.REGISTER_ORDERER);
    }

    public void requestToUpdateOrderer(UserEntity user) {
        ErpUserDTO dto = mapper.toDTO(user);
        String url = requestToUpdatePersonUrl+user.getId();
        requestService.createRequestEntity(user, dto, HttpMethod.PUT, url, RequestType.EDIT_ORDERER);
    }

}