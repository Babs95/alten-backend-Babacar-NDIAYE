package sn.babacar.alten.test.services;

import sn.babacar.alten.test.dtos.UserDTO;
import sn.babacar.alten.test.entities.User;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.response.TokenResponse;

public interface UserService {
  void register(UserDTO registrationDTO) throws CustomException;
  TokenResponse authenticate(String email, String password) throws CustomException;
  boolean isAdminUser(String email);
  User getUserByEmail(String email) throws CustomException;
}
