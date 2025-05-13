package sn.babacar.alten.test.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private String username;
  private String firstname;
  private String email;
  private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserLoginDTO {
  private String email;
  private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TokenResponseDTO {
  private String token;
  private String email;
  private String username;
}
