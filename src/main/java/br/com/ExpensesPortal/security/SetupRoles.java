package br.com.ExpensesPortal.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.ExpensesPortal.entities.AccessEntity;
import br.com.ExpensesPortal.entities.RoleEntity;
import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.enums.AccessName;
import br.com.ExpensesPortal.enums.RoleName;
import br.com.ExpensesPortal.repositories.AccessRepository;
import br.com.ExpensesPortal.repositories.RoleRepository;
import br.com.ExpensesPortal.repositories.UserRepository;
import ch.qos.logback.core.testUtil.RandomUtil;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class SetupRoles implements ApplicationListener<ContextRefreshedEvent>{

    private boolean alreadySetup;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;
        
        Arrays.stream(AccessName.values()).forEach(this::createAccessIfNotExists);
        Arrays.stream(RoleName.values()).forEach(this::createRoleIfNotExists);

        addAccess(RoleName.ROLE_ERP, Arrays.asList(
            AccessName.NOTIFY_PAYMENT
        ));

        addAccess(RoleName.ROLE_ADMIN, Arrays.asList(
            AccessName.REGISTER_EXPENSE, AccessName.APPROVER_EXPENSE, AccessName.MANAGE_USER
        ));

        addAccess(RoleName.ROLE_APPROVER, Arrays.asList(
            AccessName.REGISTER_EXPENSE, AccessName.APPROVER_EXPENSE
        ));

        addAccess(RoleName.ROLE_ORDERER, Arrays.asList(
            AccessName.REGISTER_EXPENSE
        ));

        final String erpEmail = "erp.test@email.com";
        final String adminEmail = "admin.test@email.com";
        final String approverEmail = "approver.test@email.com";
        final String ordererEmail = "orderer.test@email.com";

        createUserIfNotExists(erpEmail, RoleName.ROLE_ERP);
        createUserIfNotExists(adminEmail, RoleName.ROLE_ADMIN);
        createUserIfNotExists(approverEmail, RoleName.ROLE_APPROVER);
        createUserIfNotExists(ordererEmail, RoleName.ROLE_ORDERER);

        addApprover(approverEmail, adminEmail);
        addApprover(ordererEmail, approverEmail);
    
        alreadySetup = true;
        
    }

    private AccessEntity createAccessIfNotExists(AccessName accessName) {
        return accessRepository.findByAccessName(accessName)
            .orElseGet(() -> {
                AccessEntity entity = AccessEntity.builder()
                    .accessName(accessName)
                    .roles(new ArrayList<>())
                    .build();
                return accessRepository.save(entity);
            });
    }
    
    private RoleEntity createRoleIfNotExists(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
            .orElseGet(() -> {
                RoleEntity entity = RoleEntity.builder()
                    .roleName(roleName)
                    .access(new ArrayList<>())
                    .build();
                return roleRepository.save(entity);
            });
    }

    private UserEntity createUserIfNotExists(String email, RoleName roleName) {
        return userRepository.findByEmail(email)
            .orElseGet(() -> {
                RoleEntity role = roleRepository.findByRoleName(roleName).get();
                UserEntity user = UserEntity.builder()
                    .email(email)
                    .password(passwordEncoder.encode("password"))
                    .cpf(String.valueOf(RandomUtil.getPositiveInt()))
                    .firstname(email.split("\\.")[0])
                    .lastname("Test")
                    .role(role)
                    .build();
                return userRepository.save(user);
            });
    }

    private void addAccess(RoleName roleName, Collection<AccessName> accessNames) {
        RoleEntity roleEntity = roleRepository.findByRoleName(roleName).get();
        for(AccessName an : accessNames) {
            AccessEntity accessEntity = accessRepository.findByAccessName(an).get();
            if(!roleEntity.getAccess().contains(accessEntity))
                roleEntity.getAccess().add(accessEntity);
        }
        roleRepository.save(roleEntity);
    }

    private void addApprover(String emailUser, String emailApprover) {
        UserEntity user = userRepository.findByEmail(emailUser)
            .orElseThrow();
        UserEntity approver = userRepository.findByEmail(emailApprover)
            .orElseThrow();
        if(user.getApprover() == null) {
            user.setApprover(approver);
            userRepository.save(user);
        }
    }
    
}
