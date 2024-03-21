package br.com.ExpensesPortal.integration.ERP.mappers;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.integration.ERP.DTOs.ErpUserDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ErpUserMapper {
    
    @Mapping(target = "idPortal", source = "id")
    ErpUserDTO toDTO(UserEntity entity);
    Collection<ErpUserDTO> toDTO(Collection<UserEntity> users);

}
