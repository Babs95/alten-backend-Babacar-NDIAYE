package sn.babacar.alten.test.util;

public class Constants {
  public static final String NOT_FOUND_ENTITY = "Not Found %s %s %s";

  // PRODUCT
  public static final String PRODUCT_POST_SUCCESS = "Product created successfully";
  public static final String PRODUCT_PUT_SUCCESS = "Product updated successfully";
  public static final String PRODUCT_GET_SUCCESS = "Product(s) retrieved successfully";
  public static final String PRODUCT_GET_FAILURE = "Failed to retrieve product(s)";
  public static final String PRODUCT_PUT_FAILURE = "Failed to update product";
  public static final String PRODUCT_POST_FAILURE_ENTITY_EXIST = "Product with this identifier already exists";
  public static final String PRODUCT_GET_FAILURE_NOT_FOUND = "Product not found";
  public static final String PRODUCT_POST_FAILURE = "Failed to create product";
  public static final String PRODUCT_GET_FAILURE_ILLEGAL_ARGUMENT = "Invalid argument provided for product retrieval";
  public static final String PRODUCT_DELETE_FAILURE = "Failed to delete product";
  public static final String PRODUCT_POST_FAILURE_ILLEGAL_ARGUMENT = "Invalid argument provided for product creation";
  public static final String PRODUCT_PUT_FAILURE_ILLEGAL_ARGUMENT = "Invalid argument provided for product update";
  public static final String PRODUCT_DELETE_FAILURE_ILLEGAL_ARGUMENT = "Invalid argument provided for product deletion";
  public static final String PRODUCT_DELETE_SUCCESS = "Product deleted successfully";

  // USER
  public static final String USER_REGISTER_SUCCESS = "User registered successfully";
  public static final String USER_REGISTER_FAILURE = "Failed to register user";
  public static final String USER_REGISTER_FAILURE_ENTITY_EXIST = "User with this email already exists";
  public static final String USER_REGISTER_FAILURE_ILLEGAL_ARGUMENT = "Invalid user registration data provided";
  public static final String USER_GET_FAILURE = "Failed to retrieve user(s)";
  public static final String USER_GET_FAILURE_ILLEGAL_ARGUMENT = "Invalid argument for user retrieval";
  public static final String USER_GET_FAILURE_NOT_FOUND = "User not found";
  public static final String USER_AUTH_SUCCESS = "User authenticated successfully";
  public static final String USER_AUTH_FAILURE = "Authentication failed";
  public static final String USER_AUTH_FAILURE_NOT_FOUND = "User not found";
  public static final String USER_AUTH_FAILURE_INVALID_CREDENTIALS = "Invalid credentials";
  public static final String USER_AUTH_FAILURE_ILLEGAL_ARGUMENT = "Email and password are required for authentication";

  // CART
  public static final String CART_GET_SUCCESS = "Cart retrieved successfully";
  public static final String CART_UPDATE_SUCCESS = "Cart updated successfully";
  public static final String CART_CLEAR_SUCCESS = "Cart cleared successfully";
  public static final String CART_GET_FAILURE = "Failed to retrieve cart";
  public static final String CART_UPDATE_FAILURE = "Failed to update cart";
  public static final String CART_CLEAR_FAILURE = "Failed to clear cart";
  public static final String CART_GET_FAILURE_NOT_FOUND = "Cart not found";
  public static final String CART_GET_FAILURE_ILLEGAL_ARGUMENT = "Invalid argument provided for cart retrieval";
  public static final String CART_UPDATE_FAILURE_ILLEGAL_ARGUMENT = "Invalid argument provided for cart update";
  public static final String CART_ITEM_GET_FAILURE_NOT_FOUND = "Cart item not found";

  private Constants() {
    throw new IllegalStateException("Constants class");
  }

  public static class Status {
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int CONFLICT = 409;
    public static final int NO_CONTENT = 204;
    public static final int METHOD_NOT_ALLOWED = 405;

    private Status() {
      throw new IllegalStateException("Constants Status class");
    }
  }

  public static class Message {
    public static final String SUCCESS_BODY = "Success";
    public static final String USER_CREATED_BODY = "Created successfully";
    public static final String BAD_REQUEST_BODY = "Invalid input data";
    public static final String MISSING_REQUEST_PARAMS = "Missing required request parameter";
    public static final String UNAUTHORIZED_BODY = "Unauthorized";
    public static final String NOT_FOUND_BODY = "Not Found";
    public static final String SERVER_ERROR_BODY = "Internal server error";
    public static final String CONFLICT_BODY = "Conflict";
    public static final String NO_CONTENT_BODY = "No content";
    public static final String METHOD_NOT_ALLOWED_BODY = "Method Not Allowed";

    private Message() {
      throw new IllegalStateException("Constants Message class");
    }
  }
}
