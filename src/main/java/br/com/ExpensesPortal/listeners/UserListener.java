package br.com.ExpensesPortal.listeners;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.integration.ERP.services.ErpUserService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class UserListener {

    private static ErpUserService erpUserService;

    @Autowired
    public void init(ErpUserService erpUserService) {
        UserListener.erpUserService = erpUserService;
    }
    
    @PrePersist
    public void onPersist(UserEntity user) {
        erpUserService.requestToRegisterOrderer(user);
    }

    @PreUpdate
    public void onUpdate(UserEntity user) {
        erpUserService.requestToUpdateOrderer(user);
    }

}