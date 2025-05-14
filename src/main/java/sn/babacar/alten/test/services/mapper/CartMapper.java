package sn.babacar.alten.test.services.mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sn.babacar.alten.test.dtos.CartDTO;
import sn.babacar.alten.test.dtos.CartItemDTO;
import sn.babacar.alten.test.entities.Cart;
import sn.babacar.alten.test.entities.CartItem;

@Mapper(componentModel = "spring", uses = {ProductMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartMapper {

  @Mapping(target = "userId", source = "user.id")
  CartDTO toDto(Cart entity);

  @Mapping(target = "product", source = "product")
  CartItemDTO toCartItemDto(CartItem entity);
}