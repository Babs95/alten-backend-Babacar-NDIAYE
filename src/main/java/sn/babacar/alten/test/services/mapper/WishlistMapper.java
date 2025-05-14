package sn.babacar.alten.test.services.mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sn.babacar.alten.test.dtos.WishlistDTO;
import sn.babacar.alten.test.entities.Wishlist;

@Mapper(componentModel = "spring", uses = {ProductMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WishlistMapper {

  @Mapping(target = "userId", source = "user.id")
  WishlistDTO toDto(Wishlist entity);

}
