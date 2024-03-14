package br.com.ExpensesPortal.entities;

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

import br.com.ExpensesPortal.enums.ExpenseStatus;
import br.com.ExpensesPortal.enums.UnitType;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseEntity extends BaseEntity{
    
    @ManyToOne
    @JoinColumn(name = "orderer_id", nullable = false)
    private UserEntity orderer;
    
    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false)
    private UserEntity approver;
    
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
    
    public double getTotalValue() {
        return unitQnt * valuePerUnit;
    }
}
