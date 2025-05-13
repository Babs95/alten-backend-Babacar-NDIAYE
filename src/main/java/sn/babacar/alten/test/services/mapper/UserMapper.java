package sn.babacar.alten.test.services.mapper;

import org.mapstruct.*;
import sn.babacar.alten.test.dtos.UserDTO;
import sn.babacar.alten.test.entities.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  User toEntity(UserDTO dto);

  UserDTO toDto(User entity);
}
