package br.com.ExpensesPortal.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ExpensesPortal.entities.AccessEntity;
import br.com.ExpensesPortal.enums.AccessName;

public interface AccessRepository extends JpaRepository<AccessEntity, UUID> {
    Optional<AccessEntity> findByAccessName(AccessName accessName);
}
