package sn.babacar.alten.test.services.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ProductDTO createProduct(ProductDTO productDTO) throws CustomException {
    if (productDTO == null) {
      throw new CustomException(
          new IllegalArgumentException("Product data is required"),
          Constants.PRODUCT_POST_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Creating new product: {}", productDTO);

    try {
      if (productDTO.getCode() != null && productRepository.existsByCode(productDTO.getCode())) {
        throw new CustomException(
            new EntityExistsException("Product with code " + productDTO.getCode() + " already exists"),
            Constants.PRODUCT_POST_FAILURE_ENTITY_EXIST
        );
      }

      Product product = productMapper.toEntity(productDTO);
      Product savedProduct = productRepository.save(product);

      return productMapper.toDto(savedProduct);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.PRODUCT_POST_FAILURE);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductDTO> getAllProducts() throws CustomException {
    try {
      List<Product> products = productRepository.findAll();
      return products.stream()
          .map(productMapper::toDto)
          .toList();
    } catch (Exception e) {
      throw new CustomException(e, Constants.PRODUCT_GET_FAILURE);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public ProductDTO getProductById(Long id) throws CustomException {
    if (id == null) {
      throw new CustomException(
          new IllegalArgumentException("Product ID is required"),
          Constants.PRODUCT_GET_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    try {
      Product product = productRepository.findById(id)
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Product", "with id", id)),
              Constants.PRODUCT_GET_FAILURE_NOT_FOUND
          ));

      return productMapper.toDto(product);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.PRODUCT_GET_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws CustomException {
    if (id == null || productDTO == null) {
      throw new CustomException(
          new IllegalArgumentException("Product ID and data are required"),
          Constants.PRODUCT_PUT_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Updating product with id: {} with data: {}", id, productDTO);

    try {
      Product existingProduct = productRepository.findById(id)
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Product", "with id", id)),
              Constants.PRODUCT_GET_FAILURE_NOT_FOUND
          ));

      if (productDTO.getCode() != null && !productDTO.getCode().equals(existingProduct.getCode())
          && productRepository.existsByCode(productDTO.getCode())) {
        throw new CustomException(
            new EntityExistsException("Product with code " + productDTO.getCode() + " already exists"),
            Constants.PRODUCT_POST_FAILURE_ENTITY_EXIST
        );
      }

      productMapper.updateProductFromDto(productDTO, existingProduct);
      Product updatedProduct = productRepository.save(existingProduct);
      return productMapper.toDto(updatedProduct);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.PRODUCT_PUT_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteProduct(Long id) throws CustomException {
    if (id == null) {
      throw new CustomException(
          new IllegalArgumentException("Product ID is required"),
          Constants.PRODUCT_DELETE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    try {
      if (!productRepository.existsById(id)) {
        throw new CustomException(
            new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Product", "with id", id)),
            Constants.PRODUCT_GET_FAILURE_NOT_FOUND
        );
      }
      productRepository.deleteById(id);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.PRODUCT_DELETE_FAILURE);
    }
  }
}