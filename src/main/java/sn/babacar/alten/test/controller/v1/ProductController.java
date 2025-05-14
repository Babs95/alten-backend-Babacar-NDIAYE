package sn.babacar.alten.test.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import sn.babacar.alten.test.dtos.ProductDTO;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.response.CustomResponse;
import sn.babacar.alten.test.services.ProductService;
import sn.babacar.alten.test.util.Constants;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<CustomResponse> createProduct(@RequestBody ProductDTO productDTO) throws CustomException {
    ProductDTO createdProduct = productService.createProduct(productDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CustomResponse(Constants.Message.USER_CREATED_BODY, Constants.Status.CREATED,
            Constants.PRODUCT_POST_SUCCESS, createdProduct));
  }

  @GetMapping
  public ResponseEntity<CustomResponse> getAllProducts() throws CustomException {
    List<ProductDTO> products = productService.getAllProducts();
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.PRODUCT_GET_SUCCESS, products));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomResponse> getProductById(@PathVariable Long id) throws CustomException {
    ProductDTO product = productService.getProductById(id);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.PRODUCT_GET_SUCCESS, product));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CustomResponse> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) throws CustomException {
    ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.PRODUCT_PUT_SUCCESS, updatedProduct));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CustomResponse> deleteProduct(@PathVariable Long id) throws CustomException {
    productService.deleteProduct(id);
    return ResponseEntity.ok().body(new CustomResponse(
        Constants.Message.SUCCESS_BODY, Constants.Status.OK, Constants.PRODUCT_DELETE_SUCCESS,
        null));
  }
}