package br.com.ExpensesPortal.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ExpensesPortal.DTOs.requests.UserRegisterDTO;
import br.com.ExpensesPortal.DTOs.requests.UserUpdateDTO;
import br.com.ExpensesPortal.DTOs.responses.UserResponseDTO;
import br.com.ExpensesPortal.entities.AccessEntity;
import br.com.ExpensesPortal.entities.RoleEntity;
import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.enums.AccessName;
import br.com.ExpensesPortal.mappers.UserMapper;
import br.com.ExpensesPortal.repositories.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccessService accessService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper mapper;

    public Collection<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(UUID id) {
        Optional<UserEntity> user = userRepository.findById(id);
        return user
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserEntity) {
            UUID id = ((UserEntity) principal).getId();
            return userRepository.findById(id).get();
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user is authenticated");
    }

    public UserResponseDTO registerUser(UserRegisterDTO dto) {
        UserEntity newUser = mapper.toEntity(dto);
        RoleEntity role = roleService.findById(dto.roleId());
        
        if(dto.approverId() != null){
            UserEntity approver = findApproverById(dto.approverId());
            newUser.setApprover(approver);
        }

        newUser.setRole(role);
        newUser.setPassword(
            passwordEncoder.encode(dto.password())
        );

        newUser = userRepository.save(newUser);

        return mapper.toResponseDTO(newUser);
    }

    public UserEntity updateUser(UserUpdateDTO dto, UUID id) {
        if(dto.approverId().equals(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be your own approver");

        UserEntity user = findById(id);

        UserEntity approver = null;
        if(dto.approverId() != null) {
            approver = findApproverById(dto.approverId());
            // mudar aprovação de todas as despesas
        }

        String password = user.getPassword();
        if(dto.password() != null)
            password = passwordEncoder.encode(dto.password());

        RoleEntity role = roleService.findById(dto.roleId());

        BeanUtils.copyProperties(dto, user);
        
        user.setPassword(password);
        user.setRole(role);
        user.setApprover(approver);
        return userRepository.save(user);
    }
    
    public Collection<UserEntity> findAllByAccess(AccessName name) {
        AccessEntity access = accessService.findByAccessName(name);
        return access.getUsers();
    }

    public UserEntity findApproverById(UUID id) {
        UserEntity user = findById(id);
        if(user.hasAuthority(AccessName.APPROVER_EXPENSE)) return user;
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a appover");
    }

    public Collection<UserEntity> findAllByReference(UserEntity reference) {
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues();
        Example<UserEntity> example = Example.of(reference, matcher);
        return userRepository.findAll(example);
    }
    
}
