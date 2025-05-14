package sn.babacar.alten.test.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.babacar.alten.test.dtos.CartDTO;
import sn.babacar.alten.test.entities.Cart;
import sn.babacar.alten.test.entities.CartItem;
import sn.babacar.alten.test.entities.Product;
import sn.babacar.alten.test.entities.User;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.repositories.CartRepository;
import sn.babacar.alten.test.repositories.ProductRepository;
import sn.babacar.alten.test.services.CartService;
import sn.babacar.alten.test.services.UserService;
import sn.babacar.alten.test.services.mapper.CartMapper;
import sn.babacar.alten.test.util.Constants;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final UserService userService;
  private final ProductRepository productRepository;
  private final CartMapper cartMapper;

  @Override
  public CartDTO getCartForUser(String userId) throws CustomException {
    if (userId == null) {
      throw new CustomException(
          new IllegalArgumentException("User ID is required"),
          Constants.CART_GET_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Retrieving cart for user with ID: {}", userId);

    try {
      User user = userService.getUserByEmail(userId);

      Cart cart = cartRepository.findByUserId(user.getId())
          .orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
          });

      return cartMapper.toDto(cart);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.CART_GET_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CartDTO addProductToCart(String userId, Long productId, Integer quantity) throws CustomException {
    if (userId == null || productId == null || quantity == null) {
      throw new CustomException(
          new IllegalArgumentException("User ID, product ID, and quantity are required"),
          Constants.CART_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    if (quantity <= 0) {
      throw new CustomException(
          new IllegalArgumentException("Quantity must be greater than 0"),
          Constants.CART_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Adding product ID: {} with quantity: {} to cart for user ID: {}", productId, quantity, userId);

    try {
      User user = userService.getUserByEmail(userId);

      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Product", "with id", productId)),
              Constants.PRODUCT_GET_FAILURE_NOT_FOUND
          ));

      Cart cart = cartRepository.findByUserId(user.getId())
          .orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
          });

      // Check if product already exists in the cart
      Optional<CartItem> existingItem = cart.getItems().stream()
          .filter(item -> item.getProduct().getId().equals(productId))
          .findFirst();

      if (existingItem.isPresent()) {
        existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
      } else {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cart.getItems().add(cartItem);
      }

      cart = cartRepository.save(cart);
      return cartMapper.toDto(cart);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.CART_UPDATE_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CartDTO updateCartItem(String userId, Long itemId, Integer quantity) throws CustomException {
    if (userId == null || itemId == null || quantity == null) {
      throw new CustomException(
          new IllegalArgumentException("User ID, item ID, and quantity are required"),
          Constants.CART_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    if (quantity <= 0) {
      throw new CustomException(
          new IllegalArgumentException("Quantity must be greater than 0"),
          Constants.CART_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Updating cart item ID: {} to quantity: {} for user ID: {}", itemId, quantity, userId);
    User user = userService.getUserByEmail(userId);

    try {
      Cart cart = cartRepository.findByUserId(user.getId())
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Cart", "for user with id", userId)),
              Constants.CART_GET_FAILURE_NOT_FOUND
          ));

      CartItem cartItem = cart.getItems().stream()
          .filter(item -> item.getId().equals(itemId))
          .findFirst()
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Cart item", "with id", itemId)),
              Constants.CART_ITEM_GET_FAILURE_NOT_FOUND
          ));

      cartItem.setQuantity(quantity);
      cart = cartRepository.save(cart);

      return cartMapper.toDto(cart);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.CART_UPDATE_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CartDTO removeProductFromCart(String userId, Long productId) throws CustomException {
    if (userId == null || productId == null) {
      throw new CustomException(
          new IllegalArgumentException("User ID and product ID are required"),
          Constants.CART_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Removing product ID: {} from cart for user ID: {}", productId, userId);
    User user = userService.getUserByEmail(userId);

    try {
      Cart cart = cartRepository.findByUserId(user.getId())
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Cart", "for user with id", userId)),
              Constants.CART_GET_FAILURE_NOT_FOUND
          ));

      boolean itemRemoved = cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

      if (!itemRemoved) {
        throw new CustomException(
            new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Product", "with id", productId) + " in cart"),
            Constants.CART_ITEM_GET_FAILURE_NOT_FOUND
        );
      }

      cart = cartRepository.save(cart);

      return cartMapper.toDto(cart);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.CART_UPDATE_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void clearCart(String userId) throws CustomException {
    if (userId == null) {
      throw new CustomException(
          new IllegalArgumentException("User ID is required"),
          Constants.CART_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Clearing cart for user ID: {}", userId);
    User user = userService.getUserByEmail(userId);
    try {
      Cart cart = cartRepository.findByUserId(user.getId())
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Cart", "for user with id", userId)),
              Constants.CART_GET_FAILURE_NOT_FOUND
          ));

      cart.getItems().clear();
      cartRepository.save(cart);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.CART_CLEAR_FAILURE);
    }
  }
}