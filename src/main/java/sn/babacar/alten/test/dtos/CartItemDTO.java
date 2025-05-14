package sn.babacar.alten.test.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Article individuel dans le panier")
public class CartItemDTO {
  @Schema(description = "Identifiant unique de l'article dans le panier")
  private Long id;

  @Schema(description = "Informations détaillées du produit")
  private ProductDTO product;

  @Schema(description = "Quantité du produit dans le panier", example = "2")
  private Integer quantity;
}
