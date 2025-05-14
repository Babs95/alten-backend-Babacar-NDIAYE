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
import sn.babacar.alten.test.dtos.WishlistDTO;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.response.CustomResponse;
import sn.babacar.alten.test.services.WishlistService;
import sn.babacar.alten.test.util.Constants;

@RestController
@RequestMapping("/v1/wishlist")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "API pour la gestion de la liste de souhaits")
public class WishlistController {

  private final WishlistService wishlistService;

  @Operation(summary = "Récupérer la liste de souhaits de l'utilisateur",
      description = "Retourne la liste de souhaits de l'utilisateur authentifié avec tous ses produits")
  @ApiResponse(responseCode = "200", description = "Liste de souhaits récupérée avec succès",
      content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  @GetMapping
  public ResponseEntity<CustomResponse> getWishlist(Authentication authentication) throws CustomException {
    log.info("Récupération de la liste de souhaits pour l'utilisateur: {}", authentication.getName());
    String email = authentication.getName();

    WishlistDTO wishlist = wishlistService.getWishlistForUser(email);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.WISHLIST_GET_SUCCESS, wishlist));
  }

  @Operation(summary = "Ajouter un produit à la liste de souhaits",
      description = "Ajoute un produit spécifié à la liste de souhaits de l'utilisateur authentifié")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produit ajouté à la liste de souhaits avec succès",
          content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "404", description = "Produit non trouvé",
          content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  })
  @PostMapping("/products/{productId}")
  public ResponseEntity<CustomResponse> addProductToWishlist(
      Authentication authentication,
      @Parameter(description = "ID du produit à ajouter à la liste de souhaits", required = true)
      @PathVariable Long productId) throws CustomException {
    String email = authentication.getName();
    log.info("Ajout du produit ID: {} à la liste de souhaits pour l'utilisateur: {}", productId, email);

    WishlistDTO updatedWishlist = wishlistService.addProductToWishlist(email, productId);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.WISHLIST_ADD_SUCCESS, updatedWishlist));
  }

  @Operation(summary = "Supprimer un produit de la liste de souhaits",
      description = "Retire un produit de la liste de souhaits de l'utilisateur")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produit supprimé de la liste de souhaits avec succès",
          content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "404", description = "Produit non trouvé dans la liste de souhaits",
          content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  })
  @DeleteMapping("/products/{productId}")
  public ResponseEntity<CustomResponse> removeProductFromWishlist(
      Authentication authentication,
      @Parameter(description = "ID du produit à supprimer de la liste de souhaits", required = true)
      @PathVariable Long productId) throws CustomException {
    String email = authentication.getName();
    log.info("Suppression du produit ID: {} de la liste de souhaits pour l'utilisateur: {}", productId, email);

    WishlistDTO updatedWishlist = wishlistService.removeProductFromWishlist(email, productId);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.WISHLIST_REMOVE_SUCCESS, updatedWishlist));
  }

  @Operation(summary = "Vider la liste de souhaits",
      description = "Supprime tous les produits de la liste de souhaits de l'utilisateur")
  @ApiResponse(responseCode = "200", description = "Liste de souhaits vidée avec succès",
      content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  @DeleteMapping
  public ResponseEntity<CustomResponse> clearWishlist(Authentication authentication) throws CustomException {
    String email = authentication.getName();
    log.info("Vidage de la liste de souhaits pour l'utilisateur: {}", email);

    wishlistService.clearWishlist(email);
    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.WISHLIST_CLEAR_SUCCESS, null));
  }
}