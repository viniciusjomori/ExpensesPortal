
package br.com.ExpensesPortal.mappers;

import br.com.ExpensesPortal.DTOs.requests.ExpenseRequestDTO;
import br.com.ExpensesPortal.DTOs.responses.ExpenseResponseDTO;
import br.com.ExpensesPortal.entities.ExpenseEntity;

import java.util.Collection;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "editDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "approvalDate", ignore = true)
    @Mapping(target = "expenseStatus", ignore = true)
    @Mapping(target = "approver", ignore = true)
    @Mapping(target = "orderer", ignore = true)
    ExpenseEntity toEntity(ExpenseRequestDTO dto);
    
    ExpenseResponseDTO toResponseDTO(ExpenseEntity entity);
    Collection<ExpenseResponseDTO> toResponseDTO(Collection<ExpenseEntity> entities);

}
