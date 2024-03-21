package br.com.ExpensesPortal.services;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.ExpensesPortal.DTOs.requests.HttpDTO;
import br.com.ExpensesPortal.entities.BaseEntity;
import br.com.ExpensesPortal.entities.RequestEntity;
import br.com.ExpensesPortal.enums.RequestStatus;
import br.com.ExpensesPortal.enums.RequestType;
import br.com.ExpensesPortal.repositories.RequestRepository;
import br.com.ExpensesPortal.utils.RequestUtil;

@Service
public class RequestService {
    
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private Gson gson;

    public void createRequestEntity(BaseEntity entity, Object body, HttpMethod httpMethod, String url, RequestType requestType) {
        String json = gson.toJson(body);
        RequestEntity requestEntity = RequestEntity.builder()
            .httpMethod(httpMethod)
            .url(url)
            .entity(entity.getId())
            .jsonEntity(json)
            .status(RequestStatus.PENDING)
            .requestType(requestType)
            .build();
        requestRepository.save(requestEntity);
    }

    public void processRequestEntity(RequestEntity requestEntity) {
        requestEntity.setProcessDate(LocalDateTime.now());
    
        HttpDTO response = sendRequest(requestEntity);
    
        updateRequestEntityStatus(requestEntity, response);
    }
    
    private HttpDTO sendRequest(RequestEntity requestEntity) {
        HttpDTO response = null;
        try {
            response = requestUtil.request(
                requestEntity.getUrl(),
                requestEntity.getHttpMethod(),
                requestEntity.getJsonEntity(),
                null
            );
        } catch (IOException e) {}
        return response;
    }
    
    private void updateRequestEntityStatus(RequestEntity requestEntity, HttpDTO response) {
        if (response != null && HttpStatus.OK.equals(response.httpStatus()))
            requestEntity.setStatus(RequestStatus.SUCCESS);
        else
            requestEntity.setStatus(RequestStatus.ERROR);
        
        if (response != null)
            requestEntity.setReturnProcess(response.jsonBody());
    }

}
