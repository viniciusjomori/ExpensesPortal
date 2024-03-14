package br.com.ExpensesPortal.entities;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.ExpensesPortal.enums.AccessName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "access")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccessEntity extends BaseEntity{
    
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    public AccessName accessName;

    @ManyToMany(mappedBy = "access", fetch = FetchType.LAZY)
    public Collection<RoleEntity> roles;

    public SimpleGrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(this.accessName.toString());
    }

    public Collection<UserEntity> getUsers() {
        return roles.stream()
            .flatMap(role -> role.getUsers().stream())
            .distinct()
            .collect(Collectors.toList());
    }
}
