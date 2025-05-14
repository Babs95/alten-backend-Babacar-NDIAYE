package sn.babacar.alten.test.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.babacar.alten.test.dtos.ProductDTO;
import sn.babacar.alten.test.entities.Product;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.repositories.ProductRepository;
import sn.babacar.alten.test.services.impl.ProductServiceImpl;
import sn.babacar.alten.test.services.mapper.ProductMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTests {
  @Mock
  private ProductRepository productRepository;

  @Mock
  private ProductMapper productMapper;

  @InjectMocks
  private ProductServiceImpl productService;

  private ProductDTO productDTO;
  private Product product;

  @BeforeEach
  void setUp() {
    productDTO = new ProductDTO();
    productDTO.setId(1L);
    productDTO.setCode("TEST001");
    productDTO.setName("Test Product");

    product = new Product();
    product.setId(1L);
    product.setCode("TEST001");
    product.setName("Test Product");
  }

  @Test
  void createProduct_Success() throws CustomException {
    when(productRepository.existsByCode(anyString())).thenReturn(false);
    when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
    when(productRepository.save(any(Product.class))).thenReturn(product);
    when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

    ProductDTO result = productService.createProduct(productDTO);

    assertNotNull(result);
    assertEquals(productDTO.getCode(), result.getCode());
    verify(productRepository).save(any(Product.class));
  }

  @Test
  void createProduct_NullProduct() {
    assertThrows(CustomException.class, () -> productService.createProduct(null));
  }

  @Test
  void createProduct_DuplicateCode() {
    when(productRepository.existsByCode(anyString())).thenReturn(true);

    assertThrows(CustomException.class, () -> productService.createProduct(productDTO));
  }

  @Test
  void getAllProducts_Success() throws CustomException {
    List<Product> products = Arrays.asList(product);
    when(productRepository.findAll()).thenReturn(products);
    when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

    List<ProductDTO> result = productService.getAllProducts();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
  }

  @Test
  void getProductById_Success() throws CustomException {
    when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
    when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

    ProductDTO result = productService.getProductById(1L);

    assertNotNull(result);
    assertEquals(productDTO.getId(), result.getId());
  }

  @Test
  void getProductById_NotFound() {
    when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> productService.getProductById(1L));
  }

  @Test
  void updateProduct_Success() throws CustomException {
    when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
    when(productRepository.save(any(Product.class))).thenReturn(product);
    when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

    ProductDTO result = productService.updateProduct(1L, productDTO);

    assertNotNull(result);
    assertEquals(productDTO.getId(), result.getId());
    verify(productRepository).save(any(Product.class));
  }

  @Test
  void updateProduct_NotFound() {
    when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(CustomException.class, () -> productService.updateProduct(1L, productDTO));
  }

  @Test
  void deleteProduct_Success() throws CustomException {
    when(productRepository.existsById(anyLong())).thenReturn(true);
    doNothing().when(productRepository).deleteById(anyLong());

    assertDoesNotThrow(() -> productService.deleteProduct(1L));
    verify(productRepository).deleteById(1L);
  }

  @Test
  void deleteProduct_NotFound() {
    when(productRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(CustomException.class, () -> productService.deleteProduct(1L));
  }

}
