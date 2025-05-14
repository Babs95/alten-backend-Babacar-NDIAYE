package sn.babacar.alten.test.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import sn.babacar.alten.test.dtos.UserDTO;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.request.LoginRequest;
import sn.babacar.alten.test.response.CustomResponse;
import sn.babacar.alten.test.response.TokenResponse;
import sn.babacar.alten.test.services.UserService;
import sn.babacar.alten.test.util.Constants;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "API pour l'authentification et la gestion des utilisateurs")
public class AuthController {

  private final UserService userService;

  @Operation(summary = "Créer un compte utilisateur",
      description = "Permet de créer un nouveau compte utilisateur avec les informations fournies")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Compte créé avec succès",
          content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "400", description = "Données utilisateur invalides ou email déjà utilisé",
          content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  })
  @PostMapping("/account")
  public ResponseEntity<CustomResponse> register(
      @Parameter(description = "Informations pour la création de compte", required = true)
      @RequestBody UserDTO registrationDTO) throws CustomException {
    userService.register(registrationDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CustomResponse(Constants.Message.USER_CREATED_BODY, Constants.Status.CREATED,
            Constants.USER_REGISTER_SUCCESS, null));
  }

  @Operation(summary = "Se connecter",
      description = "Authentifie un utilisateur et retourne un token JWT")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Authentification réussie, token JWT retourné",
          content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "401", description = "Identifiants invalides",
          content = @Content(schema = @Schema(implementation = CustomResponse.class)))
  })
  @PostMapping("/token")
  public ResponseEntity<CustomResponse> login(
      @Parameter(description = "Identifiants de connexion", required = true)
      @RequestBody LoginRequest loginRequest) throws CustomException {
    TokenResponse response = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.USER_AUTH_SUCCESS, response));
  }
}