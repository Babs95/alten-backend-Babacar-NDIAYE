package sn.babacar.alten.test.util;

public class Constants {

  // PRODUCT
  public static final String PRODUCT_POST_SUCCESS = "Product created successfully";
  public static final String PRODUCT_PUT_SUCCESS = "Product updated successfully";
  public static final String PRODUCT_GET_SUCCESS = "Product(s) retrieved successfully";
  public static final String PRODUCT_GET_FAILURE = "Failed to retrieve product(s)";
  public static final String PRODUCT_POST_FAILURE_BAD_REQUEST = "Invalid product data provided";
  public static final String PRODUCT_PUT_FAILURE = "Failed to update product";
  public static final String PRODUCT_POST_FAILURE_ENTITY_EXIST = "Product with this identifier already exists";
  public static final String PRODUCT_POST_FAILURE_UNAUTHORIZE = "Unauthorized to create product";
  public static final String PRODUCT_GET_FAILURE_NOT_FOUND = "Product not found";
  public static final String PRODUCT_POST_FAILURE = "Failed to create product";
  public static final String PRODUCT_GET_FAILURE_UNAUTHORIZE = "Unauthorized to access product information";
  public static final String PRODUCT_GET_FAILURE_ILLEGAL_ARGUMENT = "Invalid argument provided for product retrieval";
  public static final String PRODUCT_GET_FAILURE_BAD_REQUEST = "Bad request for product retrieval";

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
  }
}
