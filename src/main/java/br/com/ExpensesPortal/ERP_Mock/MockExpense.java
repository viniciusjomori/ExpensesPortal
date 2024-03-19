package br.com.ExpensesPortal.ERP_Mock;

import br.com.ExpensesPortal.entities.BaseEntity;
import br.com.ExpensesPortal.enums.ExpenseStatus;
import br.com.ExpensesPortal.enums.UnitType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mock_erp_expenses")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MockExpense extends BaseEntity {

    @Column(unique = true, updatable = false, nullable = false)
    private UUID idPortal;

    @ManyToOne
    @JoinColumn(name = "orderer_id", nullable = false)
    private MockPerson mockOrderer;
    
    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false)
    private MockPerson mockApprover;
    
    @Column(nullable = false)
    private String productDesc;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitType unitType;
    
    @Column(nullable = false)
    private Double unitQnt;
    
    @Column(nullable = false)
    private Double valuePerUnit;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private LocalDateTime approvalDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseStatus expenseStatus;
}
