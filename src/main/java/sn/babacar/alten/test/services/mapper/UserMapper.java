package sn.babacar.alten.test.services.mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapper;

import sn.babacar.alten.test.entities.User;
import sn.babacar.alten.test.response.TokenResponse;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
  TokenResponse toTokenResponse(User entity, String token);
}
