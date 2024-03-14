
package br.com.ExpensesPortal.repositories;

import br.com.ExpensesPortal.entities.TokenEntity;
import br.com.ExpensesPortal.entities.UserEntity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {

    @EntityGraph(attributePaths = {"user.role.access"})
    Optional<TokenEntity> findByToken(String token);

    @Query("SELECT t FROM TokenEntity t WHERE t.user = :user AND t.active = true")
    Collection<TokenEntity> findActiveByUser(@Param("user") UserEntity user);
}
