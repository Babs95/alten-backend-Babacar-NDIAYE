package sn.babacar.alten.test.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Format de réponse standard pour toutes les API")
public class CustomResponse {
  @Schema(description = "Message décrivant le statut de la réponse", example = "Success")
  private String status;
  @Schema(description = "Code de statut HTTP", example = "200")
  private Integer statusCode;
  @Schema(description = "Description détaillée du résultat", example = "Products retrieved successfully")
  private String message;
  @Schema(description = "Données de la réponse, peut être un objet ou une collection")
  private Object data;

  public CustomResponse(String status, int statusCode, String message, Object data) {
    this.status = status;
    this.statusCode = statusCode;
    this.message = message;
    this.data = data instanceof Page<?> ? convertToResponse((Page<?>) data) : data;
  }

  private static Map<String, Object> convertToResponse(final Page<?> page) {
    Map<String, Object> response = new HashMap<>();
    response.put("total-pages", page.getTotalPages());
    response.put("current-page", page.getNumber());
    response.put("total-items", page.getTotalElements());
    response.put("sortDir", page.getSort());
    response.put("size", page.getSize());
    response.put("results", page.getContent());
    response.put("isFirst", page.isFirst());
    response.put("isLast", page.isLast());
    return response;
  }
}
