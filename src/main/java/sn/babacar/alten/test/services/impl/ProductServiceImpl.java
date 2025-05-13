package sn.babacar.alten.test.services.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.babacar.alten.test.dtos.ProductDTO;
import sn.babacar.alten.test.entities.Product;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.services.mapper.ProductMapper;
import sn.babacar.alten.test.repositories.ProductRepository;
import sn.babacar.alten.test.services.ProductService;
import sn.babacar.alten.test.util.Constants;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public ProductDTO createProduct(ProductDTO productDTO) throws CustomException {
    try {
      if (productDTO.getCode() != null && productRepository.existsByCode(productDTO.getCode())) {
        throw new EntityExistsException("Product with code " + productDTO.getCode() + " already exists");
      }

      Product product = productMapper.toEntity(productDTO);
      Product savedProduct = productRepository.save(product);

      return productMapper.toDto(savedProduct);
    } catch (EntityExistsException e) {
      throw new CustomException(Constants.PRODUCT_POST_FAILURE_ENTITY_EXIST, e);
    } catch (Exception e) {
      throw new CustomException(Constants.PRODUCT_POST_FAILURE, e);
    }
  }

  @Override
  public List<ProductDTO> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return products.stream()
        .map(productMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public ProductDTO getProductById(Long id) throws CustomException {
    try {
      Product product = productRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

      return productMapper.toDto(product);
    } catch (EntityNotFoundException e) {
      throw new CustomException(Constants.PRODUCT_GET_FAILURE_NOT_FOUND, e);
    } catch (Exception e) {
      throw new CustomException(Constants.PRODUCT_GET_FAILURE, e);
    }
  }

  @Override
  public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws CustomException {
    try {
      Product existingProduct = productRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

      if (productDTO.getCode() != null && !productDTO.getCode().equals(existingProduct.getCode())
          && productRepository.existsByCode(productDTO.getCode())) {
        throw new EntityExistsException("Product with code " + productDTO.getCode() + " already exists");
      }

      productMapper.updateProductFromDto(productDTO, existingProduct);

      Product updatedProduct = productRepository.save(existingProduct);
      return productMapper.toDto(updatedProduct);
    } catch (EntityNotFoundException e) {
      throw new CustomException(Constants.PRODUCT_GET_FAILURE_NOT_FOUND, e);
    } catch (EntityExistsException e) {
      throw new CustomException(Constants.PRODUCT_POST_FAILURE_ENTITY_EXIST, e);
    } catch (IllegalArgumentException e) {
      throw new CustomException(Constants.PRODUCT_GET_FAILURE_ILLEGAL_ARGUMENT, e);
    } catch (Exception e) {
      throw new CustomException(Constants.PRODUCT_PUT_FAILURE, e);
    }
  }

  @Override
  public void deleteProduct(Long id) throws CustomException {
    try {
      if (!productRepository.existsById(id)) {
        throw new EntityNotFoundException("Product not found with id: " + id);
      }
      productRepository.deleteById(id);
    } catch (EntityNotFoundException e) {
      throw new CustomException(Constants.PRODUCT_GET_FAILURE_NOT_FOUND, e);
    } catch (Exception e) {
      throw new CustomException("Error deleting product", e);
    }
  }
}