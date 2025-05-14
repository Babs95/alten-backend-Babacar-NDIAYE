package sn.babacar.alten.test.services;

import sn.babacar.alten.test.dtos.WishlistDTO;
import sn.babacar.alten.test.exception.CustomException;

public interface WishlistService {
  WishlistDTO getWishlistForUser(String email) throws CustomException;
  WishlistDTO addProductToWishlist(String email, Long productId) throws CustomException;
  WishlistDTO removeProductFromWishlist(String email, Long productId) throws CustomException;
  void clearWishlist(String email) throws CustomException;
}
