package sn.babacar.alten.test.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.babacar.alten.test.dtos.WishlistDTO;
import sn.babacar.alten.test.entities.Product;
import sn.babacar.alten.test.entities.User;
import sn.babacar.alten.test.entities.Wishlist;
import sn.babacar.alten.test.exception.CustomException;
import sn.babacar.alten.test.repositories.WishlistRepository;
import sn.babacar.alten.test.services.ProductService;
import sn.babacar.alten.test.services.UserService;
import sn.babacar.alten.test.services.WishlistService;
import sn.babacar.alten.test.services.mapper.WishlistMapper;
import sn.babacar.alten.test.util.Constants;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

  private final WishlistRepository wishlistRepository;
  private final UserService userService;
  private final ProductService productService;
  private final WishlistMapper wishlistMapper;

  @Override
  public WishlistDTO getWishlistForUser(String email) throws CustomException {
    if (email == null) {
      throw new CustomException(
          new IllegalArgumentException("User email is required"),
          Constants.WISHLIST_GET_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Retrieving wishlist for user with email: {}", email);

    try {
      User user = userService.getUserByEmail(email);

      Wishlist wishlist = wishlistRepository.findByUserId(user.getId())
          .orElseGet(() -> {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(user);
            newWishlist.setProducts(new ArrayList<>());
            return wishlistRepository.save(newWishlist);
          });

      return wishlistMapper.toDto(wishlist);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.WISHLIST_GET_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public WishlistDTO addProductToWishlist(String email, Long productId) throws CustomException {
    if (email == null || productId == null) {
      throw new CustomException(
          new IllegalArgumentException("User email and product ID are required"),
          Constants.WISHLIST_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Adding product ID: {} to wishlist for user email: {}", productId, email);

    try {
      User user = userService.getUserByEmail(email);

      Product product = productService.getProductEntityById(productId);

      Wishlist wishlist = wishlistRepository.findByUserId(user.getId())
          .orElseGet(() -> {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(user);
            newWishlist.setProducts(new ArrayList<>());
            return wishlistRepository.save(newWishlist);
          });

      // Check if product already exists in the wishlist
      boolean productExists = wishlist.getProducts().stream()
          .anyMatch(p -> p.getId().equals(productId));

      if (!productExists) {
        wishlist.getProducts().add(product);
        wishlist = wishlistRepository.save(wishlist);
      }

      return wishlistMapper.toDto(wishlist);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.WISHLIST_UPDATE_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public WishlistDTO removeProductFromWishlist(String email, Long productId) throws CustomException {
    if (email == null || productId == null) {
      throw new CustomException(
          new IllegalArgumentException("User email and product ID are required"),
          Constants.WISHLIST_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Removing product ID: {} from wishlist for user email: {}", productId, email);

    try {
      User user = userService.getUserByEmail(email);

      Wishlist wishlist = wishlistRepository.findByUserId(user.getId())
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Wishlist", "for user with email", email)),
              Constants.WISHLIST_GET_FAILURE_NOT_FOUND
          ));

      boolean productRemoved = wishlist.getProducts().removeIf(product -> product.getId().equals(productId));

      if (!productRemoved) {
        throw new CustomException(
            new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Product", "with id", productId) + " in wishlist"),
            Constants.WISHLIST_ITEM_GET_FAILURE_NOT_FOUND
        );
      }

      wishlist = wishlistRepository.save(wishlist);

      return wishlistMapper.toDto(wishlist);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.WISHLIST_UPDATE_FAILURE);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void clearWishlist(String email) throws CustomException {
    if (email == null) {
      throw new CustomException(
          new IllegalArgumentException("User email is required"),
          Constants.WISHLIST_UPDATE_FAILURE_ILLEGAL_ARGUMENT
      );
    }

    log.info("Clearing wishlist for user email: {}", email);

    try {
      User user = userService.getUserByEmail(email);

      Wishlist wishlist = wishlistRepository.findByUserId(user.getId())
          .orElseThrow(() -> new CustomException(
              new EntityNotFoundException(String.format(Constants.NOT_FOUND_ENTITY, "Wishlist", "for user with email", email)),
              Constants.WISHLIST_GET_FAILURE_NOT_FOUND
          ));

      wishlist.getProducts().clear();
      wishlistRepository.save(wishlist);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(e, Constants.WISHLIST_CLEAR_FAILURE);
    }
  }
}