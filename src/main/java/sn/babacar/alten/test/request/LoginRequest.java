package sn.babacar.alten.test.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informations de connexion")
public class LoginRequest {
  @Schema(description = "Email de l'utilisateur", example = "admin@admin.com")
  private String email;

  @Schema(description = "Mot de passe", example = "admin123")
  private String password;
}
