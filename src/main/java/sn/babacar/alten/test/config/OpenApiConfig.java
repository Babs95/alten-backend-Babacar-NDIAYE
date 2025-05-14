package sn.babacar.alten.test.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Alten Backend Test E-Commerce API")
            .description("API REST pour la gestion des produits, utilisateurs, paniers et listes d'envies")
            .version("1.0.0")
            .contact(new Contact()
                .name("Babacar NDIAYE")
                .url("https://www.linkedin.com/in/babacar-ndiaye-8abb2a160/"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")))
        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
        .components(new Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Entrer votre token JWT avec le préfixe Bearer. Example: \"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"")));
  }
}