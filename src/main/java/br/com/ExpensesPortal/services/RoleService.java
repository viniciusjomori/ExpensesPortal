package br.com.ExpensesPortal.services;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ExpensesPortal.entities.RoleEntity;
import br.com.ExpensesPortal.repositories.RoleRepository;

@Service
public class RoleService {
    
    @Autowired
    private RoleRepository roleRepository;

    public RoleEntity findById(UUID id) {
        Optional<RoleEntity> role = roleRepository.findById(id);
        return role
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    public Collection<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

}
