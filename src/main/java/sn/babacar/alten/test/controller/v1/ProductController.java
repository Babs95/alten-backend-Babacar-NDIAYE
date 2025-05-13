package sn.babacar.alten.test.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.babacar.alten.test.dtos.ProductDTO;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.response.CustomResponse;
import sn.babacar.alten.test.services.ProductService;
import sn.babacar.alten.test.util.Constants;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<CustomResponse> createProduct(@RequestBody ProductDTO productDTO) {
    try {
      ProductDTO createdProduct = productService.createProduct(productDTO);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new CustomResponse(Constants.Message.USER_CREATED_BODY, Constants.Status.CREATED,
              Constants.PRODUCT_POST_SUCCESS, createdProduct));
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new CustomResponse(Constants.Message.BAD_REQUEST_BODY, Constants.Status.BAD_REQUEST,
              e.getMessage(), null));
    }
  }

  @GetMapping
  public ResponseEntity<CustomResponse> getAllProducts() {
    List<ProductDTO> products = productService.getAllProducts();
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.PRODUCT_GET_SUCCESS, products));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomResponse> getProductById(@PathVariable Long id) {
    try {
      ProductDTO product = productService.getProductById(id);
      return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
          Constants.PRODUCT_GET_SUCCESS, product));
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new CustomResponse(Constants.Message.NOT_FOUND_BODY, Constants.Status.NOT_FOUND,
              e.getMessage(), null));
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CustomResponse> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
    try {
      ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
      return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
          Constants.PRODUCT_PUT_SUCCESS, updatedProduct));
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new CustomResponse(Constants.Message.BAD_REQUEST_BODY, Constants.Status.BAD_REQUEST,
              e.getMessage(), null));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CustomResponse> deleteProduct(@PathVariable Long id) {
    try {
      productService.deleteProduct(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(new CustomResponse(Constants.Message.NO_CONTENT_BODY, Constants.Status.NO_CONTENT,
              "Product deleted successfully", null));
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new CustomResponse(Constants.Message.NOT_FOUND_BODY, Constants.Status.NOT_FOUND,
              e.getMessage(), null));
    }
  }
}