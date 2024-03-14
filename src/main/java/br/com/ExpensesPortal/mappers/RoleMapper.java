package br.com.ExpensesPortal.mappers;

import br.com.ExpensesPortal.DTOs.infos.RoleInfo;
import br.com.ExpensesPortal.entities.RoleEntity;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleInfo toInfo(RoleEntity entity);

    Collection<RoleInfo> toInfo(Collection<RoleEntity> entities);
}
