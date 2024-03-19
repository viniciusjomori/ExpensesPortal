package br.com.ExpensesPortal.ERP_Mock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MockPersonRepository extends JpaRepository<MockPerson, UUID> {
    Optional<MockPerson> findByIdPortal(UUID idPortal);
}
