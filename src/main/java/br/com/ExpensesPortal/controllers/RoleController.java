package br.com.ExpensesPortal.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ExpensesPortal.DTOs.infos.RoleInfo;
import br.com.ExpensesPortal.entities.RoleEntity;
import br.com.ExpensesPortal.mappers.RoleMapper;
import br.com.ExpensesPortal.services.RoleService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<Collection<RoleInfo>> findAll() {
        Collection<RoleEntity> roles = roleService.findAll();
        return ResponseEntity.ok(roleMapper.toInfo(roles));
    }

}
