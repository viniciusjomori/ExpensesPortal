package br.com.ExpensesPortal.listeners;

import java.time.LocalDateTime;

import br.com.ExpensesPortal.entities.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class BaseEntityListener {
    
    @PrePersist
    public void onPersist(BaseEntity entity) {
        entity.setCreateDate(LocalDateTime.now());
        entity.setActive(true);
    } 

    @PreUpdate
    public void onUpdate(BaseEntity entity) {
        entity.setEditDate(LocalDateTime.now());
    }
}