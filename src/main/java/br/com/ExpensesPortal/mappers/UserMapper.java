
package br.com.ExpensesPortal.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.ExpensesPortal.DTOs.infos.RoleInfo;
import br.com.ExpensesPortal.DTOs.infos.UserInfo;
import br.com.ExpensesPortal.DTOs.requests.UserRegisterDTO;
import br.com.ExpensesPortal.DTOs.responses.UserResponseDTO;
import br.com.ExpensesPortal.entities.RoleEntity;
import br.com.ExpensesPortal.entities.UserEntity;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserEntity toEntity(UserRegisterDTO dto);

    @Mapping(source = "role", target = "role", qualifiedByName = "toRoleInfo")
    UserResponseDTO toResponseDTO(UserEntity entity);

    Collection<UserResponseDTO> toResponseDTO(Collection<UserEntity> entites);

    UserInfo toInfo(UserEntity entity);
    Collection<UserInfo> toInfo(Collection<UserEntity> entities);

    @Named("toRoleInfo")
    default RoleInfo toRoleInfo(RoleEntity roleEntity) {
        return RoleMapper.INSTANCE.toInfo(roleEntity);
    }

}