package sn.babacar.alten.test.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import sn.babacar.alten.test.config.security.JwtUtil;
import sn.babacar.alten.test.dtos.UserDTO;
import sn.babacar.alten.test.entities.User;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.repositories.UserRepository;
import sn.babacar.alten.test.response.TokenResponse;
import sn.babacar.alten.test.services.impl.UserServiceImpl;
import sn.babacar.alten.test.services.mapper.UserMapper;
import sn.babacar.alten.test.util.Constants;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @Mock
  private JwtUtil jwtUtil;

  // Register tests
  @Test
  void testRegister_Success() throws CustomException {
    // Arrange
    UserDTO userDTO = new UserDTO();
    userDTO.setEmail("test@example.com");
    userDTO.setPassword("password123");
    userDTO.setFirstname("Test");

    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

    // Act
    userService.register(userDTO);

    // Assert
    verify(userRepository).existsByEmail("test@example.com");
    verify(passwordEncoder).encode("password123");
    verify(userRepository).save(any(User.class));
  }

  @Test
  void testRegister_NullDTO_ThrowsCustomException() {
    // Act & Assert
    CustomException exception = assertThrows(CustomException.class, () -> userService.register(null));
    assertEquals(Constants.USER_REGISTER_FAILURE_ILLEGAL_ARGUMENT, exception.getCodeMessage());
  }

  @Test
  void testRegister_NullEmail_ThrowsCustomException() {
    // Arrange
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("password123");

    // Act & Assert
    CustomException exception = assertThrows(CustomException.class, () -> userService.register(userDTO));
    assertEquals(Constants.USER_REGISTER_FAILURE_ILLEGAL_ARGUMENT, exception.getCodeMessage());
  }

  @Test
  void testRegister_NullPassword_ThrowsCustomException() {
    // Arrange
    UserDTO userDTO = new UserDTO();
    userDTO.setEmail("test@example.com");

    // Act & Assert
    CustomException exception = assertThrows(CustomException.class, () -> userService.register(userDTO));
    assertEquals(Constants.USER_REGISTER_FAILURE_ILLEGAL_ARGUMENT, exception.getCodeMessage());
  }

  @Test
  void testAuthenticate_Success() throws CustomException {
    // Arrange
    String email = "test@example.com";
    String password = "password123";

    User user = new User();
    user.setEmail(email);
    user.setPassword("encodedPassword");

    TokenResponse tokenResponse = new TokenResponse("jwt-token", email, "Test User");

    // Be very explicit with the mock
    when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(eq(password), eq("encodedPassword"))).thenReturn(true);
    when(jwtUtil.generateToken(eq(email), eq(false))).thenReturn("jwt-token");
    when(userMapper.toTokenResponse(eq(user), eq("jwt-token"))).thenReturn(tokenResponse);

    // Act
    TokenResponse result = userService.authenticate(email, password);

    // Assert
    assertNotNull(result);
    assertEquals("jwt-token", result.token());
    assertEquals(email, result.email());
    assertEquals("Test User", result.username());

    // Verify exact interactions
    verify(userRepository).findByEmail(eq(email));
    verify(passwordEncoder).matches(eq(password), eq("encodedPassword"));
    verify(jwtUtil).generateToken(eq(email), eq(false));
    verify(userMapper).toTokenResponse(eq(user), eq("jwt-token"));
  }

  @Test
  void testAuthenticate_NullEmail_ThrowsCustomException() {
    // Act & Assert
    CustomException exception = assertThrows(CustomException.class,
        () -> userService.authenticate(null, "password123"));
    assertEquals(Constants.USER_AUTH_FAILURE_ILLEGAL_ARGUMENT, exception.getCodeMessage());
  }

  @Test
  void testAuthenticate_NullPassword_ThrowsCustomException() {
    // Act & Assert
    CustomException exception = assertThrows(CustomException.class,
        () -> userService.authenticate("test@example.com", null));
    assertEquals(Constants.USER_AUTH_FAILURE_ILLEGAL_ARGUMENT, exception.getCodeMessage());
  }


  @Test
  void testAuthenticate_InvalidPassword_ThrowsCustomException() {
    // Arrange
    String email = "test@example.com";
    String password = "wrongPassword";

    User user = new User();
    user.setEmail(email);
    user.setPassword("encodedPassword");

    // Act & Assert
    CustomException exception = assertThrows(CustomException.class,
        () -> userService.authenticate(email, password));
    assertEquals("User not found", exception.getCodeMessage());
  }

  @Test
  void testIsAdminUser_WithAdminEmail_ReturnsTrue() {
    // Act
    boolean result = userService.isAdminUser("admin@admin.com");

    // Assert
    assertTrue(result);
  }

  @Test
  void testIsAdminUser_WithNonAdminEmail_ReturnsFalse() {
    // Act
    boolean result = userService.isAdminUser("regular@example.com");

    // Assert
    assertFalse(result);
  }

  @Test
  void testAuthenticate_JwtUtilThrowsException_ThrowsCustomException() {
    // Arrange
    String email = "test@example.com";
    String password = "password123";

    User user = new User();
    user.setEmail(email);
    user.setPassword("encodedPassword");

    // Act & Assert
    CustomException exception = assertThrows(CustomException.class,
        () -> userService.authenticate(email, password));
    assertEquals("User not found", exception.getCodeMessage());
  }
}