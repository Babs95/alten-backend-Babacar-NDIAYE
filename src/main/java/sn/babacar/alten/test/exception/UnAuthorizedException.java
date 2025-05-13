package sn.babacar.alten.test.exception;

public class UnAuthorizedException extends RuntimeException {
  public UnAuthorizedException(String message) {
    super(message);
  }
}
