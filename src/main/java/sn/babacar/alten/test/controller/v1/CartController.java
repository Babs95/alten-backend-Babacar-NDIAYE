package sn.babacar.alten.test.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.babacar.alten.test.dtos.CartDTO;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.response.CustomResponse;
import sn.babacar.alten.test.services.CartService;
import sn.babacar.alten.test.util.Constants;

@RestController
@RequestMapping("/v1/cart")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Cart", description = "API pour la gestion du panier d'achat")
public class CartController {

  private final CartService cartService;

  @Operation(summary = "Récupérer le panier de l'utilisateur",
      description = "Retourne le panier d'achat de l'utilisateur authentifié avec tous ses produits")
  @ApiResponse(responseCode = "200", description = "Panier récupéré avec succès",
      content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  @GetMapping
  public ResponseEntity<CustomResponse> getCart(Authentication authentication) throws CustomException {
    log.info("authentication: {}", authentication);
    log.info("authentication getName: {}", authentication.getName());
    String email = authentication.getName();

    CartDTO cart = cartService.getCartForUser(email);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        "Cart retrieved successfully", cart));
  }

  @Operation(summary = "Ajouter un produit au panier",
      description = "Ajoute un produit spécifié au panier de l'utilisateur authentifié")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produit ajouté au panier avec succès",
          content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "404", description = "Produit non trouvé",
          content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  })
  @PostMapping("/products/{productId}")
  public ResponseEntity<CustomResponse> addProductToCart(
      Authentication authentication,
      @Parameter(description = "ID du produit à ajouter au panier", required = true)
      @PathVariable Long productId,
      @Parameter(description = "Quantité du produit à ajouter (par défaut: 1)", example = "1")
      @RequestParam(defaultValue = "1") Integer quantity) throws CustomException {
    String email = authentication.getName();

    CartDTO updatedCart = cartService.addProductToCart(email, productId, quantity);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        "Product added to cart", updatedCart));
  }

  @Operation(summary = "Mettre à jour la quantité d'un produit dans le panier",
      description = "Modifie la quantité d'un produit spécifique dans le panier de l'utilisateur")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Quantité mise à jour avec succès",
          content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "404", description = "Produit non trouvé dans le panier",
          content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "400", description = "Quantité invalide",
          content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  })
  @PatchMapping("/items/{itemId}")
  public ResponseEntity<CustomResponse> updateCartItem(
      Authentication authentication,
      @Parameter(description = "ID de l'élément du panier à mettre à jour", required = true)
      @PathVariable Long itemId,
      @Parameter(description = "Nouvelle quantité", required = true, example = "3")
      @RequestParam Integer quantity) throws CustomException {
    String email = authentication.getName();

    CartDTO updatedCart = cartService.updateCartItem(email, itemId, quantity);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        "Cart item updated", updatedCart));
  }

  @Operation(summary = "Supprimer un produit du panier",
      description = "Retire complètement un produit du panier de l'utilisateur")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produit supprimé du panier avec succès",
          content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "404", description = "Produit non trouvé dans le panier",
          content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  })
  @DeleteMapping("/products/{productId}")
  public ResponseEntity<CustomResponse> removeProductFromCart(
      Authentication authentication,
      @Parameter(description = "ID du produit à supprimer du panier", required = true)
      @PathVariable Long productId) throws CustomException {
    String email = authentication.getName();

    CartDTO updatedCart = cartService.removeProductFromCart(email, productId);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        "Product removed from cart", updatedCart));
  }

  @Operation(summary = "Vider le panier",
      description = "Supprime tous les produits du panier de l'utilisateur")
  @ApiResponse(responseCode = "200", description = "Panier vidé avec succès",
      content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  @DeleteMapping
  public ResponseEntity<CustomResponse> clearCart(Authentication authentication) throws CustomException {
    String email = authentication.getName();

    cartService.clearCart(email);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        "Cart cleared", null));
  }
}
