package sn.babacar.alten.test.services;

import java.util.List;

import sn.babacar.alten.test.dtos.ProductDTO;
import sn.babacar.alten.test.entities.Product;
import sn.babacar.alten.test.exception.CustomException;

public interface ProductService {
  ProductDTO createProduct(ProductDTO productDTO) throws CustomException;
  List<ProductDTO> getAllProducts() throws CustomException;
  ProductDTO getProductById(Long id) throws CustomException;
  Product getProductEntityById(Long id) throws CustomException;
  ProductDTO updateProduct(Long id, ProductDTO productDTO) throws CustomException;
  void deleteProduct(Long id) throws CustomException;
}
