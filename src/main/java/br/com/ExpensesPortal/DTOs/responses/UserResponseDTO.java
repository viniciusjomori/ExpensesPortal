
package br.com.ExpensesPortal.DTOs.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.ExpensesPortal.DTOs.infos.RoleInfo;
import br.com.ExpensesPortal.DTOs.infos.UserInfo;

public record UserResponseDTO(
   UUID id,
   String email,
   String cpf,
   String firstname,
   String lastname,
   LocalDateTime createDate,
   boolean active,
   RoleInfo role,
   UserInfo approver
) {}
