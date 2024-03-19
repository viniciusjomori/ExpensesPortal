package br.com.ExpensesPortal.listeners;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import br.com.ExpensesPortal.entities.RequestEntity;
import br.com.ExpensesPortal.enums.RequestStatus;
import br.com.ExpensesPortal.services.RequestService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class RequestListener {

    private static RequestService requestService;

    @Autowired
    public void init(RequestService requestService) {
        RequestListener.requestService = requestService;
    }

    @PrePersist
    @PreUpdate
    public void processRequest(RequestEntity requestEntity) {
        if(requestEntity.getStatus().equals(RequestStatus.PENDING))
            requestService.processRequestEntity(requestEntity);
    }

}