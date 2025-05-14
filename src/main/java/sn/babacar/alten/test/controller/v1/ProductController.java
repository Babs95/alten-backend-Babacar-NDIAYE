package sn.babacar.alten.test.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products", description = "API pour la gestion des produits")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "Créer un nouveau produit", description = "Crée un nouveau produit avec les informations fournies")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Produit créé avec succès"),
      @ApiResponse(responseCode = "400", description = "Données de produit invalides"),
      @ApiResponse(responseCode = "401", description = "Non authentifié"),
      @ApiResponse(responseCode = "403", description = "Non autorisé - réservé à l'administrateur")
  })
  @PostMapping
  public ResponseEntity<CustomResponse> createProduct(
      @Parameter(description = "Données du produit à créer", required = true)
      @RequestBody ProductDTO productDTO) throws CustomException {
    ProductDTO createdProduct = productService.createProduct(productDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CustomResponse(Constants.Message.USER_CREATED_BODY, Constants.Status.CREATED,
            Constants.PRODUCT_POST_SUCCESS, createdProduct));
  }

  @Operation(summary = "Récupérer tous les produits", description = "Récupère la liste de tous les produits disponibles")
  @ApiResponse(responseCode = "200", description = "Liste des produits récupérée avec succès")
  @GetMapping
  public ResponseEntity<CustomResponse> getAllProducts() throws CustomException {
    List<ProductDTO> products = productService.getAllProducts();
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.PRODUCT_GET_SUCCESS, products));
  }

  @Operation(summary = "Récupérer un produit par ID", description = "Récupère les détails d'un produit spécifique par son ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produit trouvé"),
      @ApiResponse(responseCode = "404", description = "Produit non trouvé")
  })
  @GetMapping("/{id}")
  public ResponseEntity<CustomResponse> getProductById(
      @Parameter(description = "ID du produit à récupérer", required = true)
      @PathVariable Long id) throws CustomException {
    ProductDTO product = productService.getProductById(id);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.PRODUCT_GET_SUCCESS, product));
  }

  @Operation(summary = "Mettre à jour un produit", description = "Met à jour les informations d'un produit existant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produit mis à jour avec succès"),
      @ApiResponse(responseCode = "400", description = "Données de produit invalides"),
      @ApiResponse(responseCode = "404", description = "Produit non trouvé"),
      @ApiResponse(responseCode = "403", description = "Non autorisé - réservé à l'administrateur")
  })
  @PatchMapping("/{id}")
  public ResponseEntity<CustomResponse> updateProduct(
      @Parameter(description = "ID du produit à mettre à jour", required = true)
      @PathVariable Long id,
      @Parameter(description = "Nouvelles données du produit", required = true)
      @RequestBody ProductDTO productDTO) throws CustomException {
    ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.PRODUCT_PUT_SUCCESS, updatedProduct));
  }

  @Operation(summary = "Supprimer un produit", description = "Supprime un produit existant par son ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produit supprimé avec succès"),
      @ApiResponse(responseCode = "404", description = "Produit non trouvé"),
      @ApiResponse(responseCode = "403", description = "Non autorisé - réservé à l'administrateur")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<CustomResponse> deleteProduct(
      @Parameter(description = "ID du produit à supprimer", required = true)
      @PathVariable Long id) throws CustomException {
    productService.deleteProduct(id);
    return ResponseEntity.ok().body(new CustomResponse(
        Constants.Message.SUCCESS_BODY, Constants.Status.OK, Constants.PRODUCT_DELETE_SUCCESS,
        null));
  }
}