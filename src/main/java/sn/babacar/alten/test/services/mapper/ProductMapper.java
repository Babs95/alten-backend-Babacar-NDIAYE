package sn.babacar.alten.test.services.mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;

import sn.babacar.alten.test.dtos.ProductDTO;
import sn.babacar.alten.test.entities.Product;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Product toEntity(ProductDTO dto);

  ProductDTO toDto(Product entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateProductFromDto(ProductDTO dto, @MappingTarget Product entity);
}