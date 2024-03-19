package br.com.ExpensesPortal.ERP_Mock;

import java.util.UUID;

import br.com.ExpensesPortal.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "mock_erp_users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MockPerson extends BaseEntity{

    @Column(unique = true, updatable = false, nullable = false)
    private UUID idPortal;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(unique = true, nullable = false)
    private String cpf;
    
    @Column(nullable = false)
    private String firstname;
    
    @Column(nullable = false)
    private String lastname;

}
