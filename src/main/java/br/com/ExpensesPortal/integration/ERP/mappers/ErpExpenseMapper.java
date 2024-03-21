package br.com.ExpensesPortal.integration.ERP.mappers;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.ExpensesPortal.entities.ExpenseEntity;
import br.com.ExpensesPortal.integration.ERP.DTOs.ErpExpenseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ErpExpenseMapper {
    
    @Mapping(target = "idPortal", source = "id")
    @Mapping(target = "ordererPortalId", source = "orderer.id")
    @Mapping(target = "approverPortalId", source = "approver.id")
    ErpExpenseDTO toDTO(ExpenseEntity entity);
    Collection<ErpExpenseDTO> toDTO(Collection<ExpenseEntity> users);
}
