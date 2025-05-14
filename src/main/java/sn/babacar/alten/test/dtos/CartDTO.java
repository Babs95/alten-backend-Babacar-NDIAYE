package sn.babacar.alten.test.dtos;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informations du panier d'achat")
public class CartDTO {
  @Schema(description = "Identifiant unique du panier")
  private Long id;

  @Schema(description = "Identifiant de l'utilisateur propriétaire du panier")
  private Long userId;

  @Schema(description = "Liste des articles dans le panier")
  private List<CartItemDTO> items;

  @Schema(description = "Date de création du panier", format = "date-time")
  private LocalDateTime createdAt;

  @Schema(description = "Date de dernière mise à jour du panier", format = "date-time")
  private LocalDateTime updatedAt;
}