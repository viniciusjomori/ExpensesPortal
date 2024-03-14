
package br.com.ExpensesPortal.repositories;

import br.com.ExpensesPortal.entities.RoleEntity;
import br.com.ExpensesPortal.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByRoleName(RoleName roleName);
}
