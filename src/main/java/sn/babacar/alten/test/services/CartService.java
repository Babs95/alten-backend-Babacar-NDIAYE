package sn.babacar.alten.test.services;

import sn.babacar.alten.test.dtos.CartDTO;
import sn.babacar.alten.test.exception.CustomException;

public interface CartService {
  CartDTO getCartForUser(String userId) throws CustomException;
  CartDTO addProductToCart(String userId, Long productId, Integer quantity) throws CustomException;
  CartDTO updateCartItem(String userId, Long itemId, Integer quantity) throws CustomException;
  CartDTO removeProductFromCart(String userId, Long productId) throws CustomException;
  void clearCart(String userId) throws CustomException;
}
