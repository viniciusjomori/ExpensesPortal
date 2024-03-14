package br.com.ExpensesPortal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ExpensesPortal.entities.AccessEntity;
import br.com.ExpensesPortal.enums.AccessName;
import br.com.ExpensesPortal.repositories.AccessRepository;

@Service
public class AccessService {
    
    @Autowired
    private AccessRepository accessRepository;

    public AccessEntity findByAccessName(AccessName name) {
        return accessRepository.findByAccessName(name)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Access not found"));
    }

}
