package br.com.ExpensesPortal.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.ExpensesPortal.enums.AccessName;
import br.com.ExpensesPortal.security.UserDetailsAdapter;

import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends UserDetailsAdapter {

    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String cpf;
    
    @Column(nullable = false)
    private String firstname;
    
    @Column(nullable = false)
    private String lastname;
    
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;
    
    @OneToMany(mappedBy = "orderer")
    private Collection<ExpenseEntity> expenses;
    
    @ManyToOne
    @JoinColumn(name = "approver_id")
    private UserEntity approver;
    
    @OneToMany(mappedBy = "user")
    private Collection<TokenEntity> tokens;

    public Collection<SimpleGrantedAuthority> getAuthorities() {
       return this.role.getAuthorities();
    }

    public String getUsername() {
        return this.email;
    }

    public boolean hasAuthority(AccessName accessName) {
        return this.role.hasAuthority(accessName);
    }

}