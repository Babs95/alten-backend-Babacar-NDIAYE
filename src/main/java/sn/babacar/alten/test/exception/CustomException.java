package sn.babacar.alten.test.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends Exception {
  private Exception exception;
  private String codeMessage;

  public CustomException(String codeMessage, Throwable cause) {
    super(codeMessage, cause);
    this.codeMessage = codeMessage;
  }

  public CustomException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  public CustomException(String codeMessage) {
    super(codeMessage);
    this.codeMessage = codeMessage;
  }

  @Override
  public String getMessage() {
    return codeMessage != null ? codeMessage : super.getMessage();
  }
}
