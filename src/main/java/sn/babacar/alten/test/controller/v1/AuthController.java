package sn.babacar.alten.test.controller.v1;

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
public class AuthController {

  private final UserService userService;

  @PostMapping("/account")
  public ResponseEntity<CustomResponse> register(@RequestBody UserDTO registrationDTO) throws CustomException {
    userService.register(registrationDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CustomResponse(Constants.Message.USER_CREATED_BODY, Constants.Status.CREATED,
            Constants.USER_REGISTER_SUCCESS, null));
  }

  @PostMapping("/token")
  public ResponseEntity<CustomResponse> login(@RequestBody LoginRequest loginRequest) throws CustomException {
    TokenResponse response = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

    return ResponseEntity.ok(new CustomResponse(Constants.Message.SUCCESS_BODY, Constants.Status.OK,
        Constants.USER_AUTH_SUCCESS, response));
  }
}
