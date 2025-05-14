package sn.babacar.alten.test.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informations de la liste des souhaits")
public class WishlistDTO {
  @Schema(description = "Identifiant unique de la liste des souhaits")
  private Long id;
  @Schema(description = "Identifiant de l'utilisateur propriétaire de la liste des souhaits")
  private Long userId;
  @Schema(description = "Liste des articles")
  private List<ProductDTO> products;
  @Schema(description = "Date de création de la liste des souhaits", format = "date-time")
  private LocalDateTime createdAt;

  @Schema(description = "Date de dernière mise à jour de la liste des souhaits", format = "date-time")
  private LocalDateTime updatedAt;
}

