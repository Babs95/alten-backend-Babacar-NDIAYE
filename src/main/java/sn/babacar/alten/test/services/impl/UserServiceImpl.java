package sn.babacar.alten.test.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.babacar.alten.test.config.security.JwtUtil;
import sn.babacar.alten.test.dtos.UserDTO;
import sn.babacar.alten.test.entities.User;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.repositories.UserRepository;
import sn.babacar.alten.test.response.TokenResponse;
import sn.babacar.alten.test.services.UserService;
import sn.babacar.alten.test.services.mapper.UserMapper;
import sn.babacar.alten.test.util.Constants;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  private static final String ADMIN_EMAIL = "admin@admin.com";

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void register(UserDTO registrationDTO) throws CustomException {
    if (registrationDTO == null) {
      throw new CustomException(
          new IllegalArgumentException("User registration data is required"),
          Constants.USER_REGISTER_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    if (registrationDTO.getEmail() == null || registrationDTO.getPassword() == null) {
      throw new CustomException(
          new IllegalArgumentException("Email and password are required"),
          Constants.USER_REGISTER_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Registering new user with email: {}", registrationDTO.getEmail());

    try {
      if (userRepository.existsByEmail(registrationDTO.getEmail())) {
        throw new CustomException(
            new EntityExistsException("User with email " + registrationDTO.getEmail() + " already exists"),
            Constants.USER_REGISTER_FAILURE_ENTITY_EXIST
        );
      }

      User user = new User();
      BeanUtils.copyProperties(registrationDTO, user);
      user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

      userRepository.save(user);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.USER_REGISTER_FAILURE);
    }
  }

  @Override
  public TokenResponse authenticate(String email, String password) throws CustomException {
    if (email == null || password == null) {
      throw new CustomException(
          new IllegalArgumentException("Email and password are required"),
          Constants.USER_AUTH_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Authenticating user with email: {}", email);

    try {
      User user = userRepository.findByEmail(email)
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException("User not found with email: " + email),
              Constants.USER_AUTH_FAILURE_NOT_FOUND
          ));

      if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new CustomException(
            new IllegalArgumentException("Invalid password"),
            Constants.USER_AUTH_FAILURE_INVALID_CREDENTIALS
        );
      }

      String token = jwtUtil.generateToken(user.getEmail(), isAdminUser(user.getEmail()));
      return userMapper.toTokenResponse(user, token);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.USER_AUTH_FAILURE);
    }
  }

  @Override
  public boolean isAdminUser(String email) {
    if (email == null) {
      return false;
    }
    return ADMIN_EMAIL.equals(email);
  }
}