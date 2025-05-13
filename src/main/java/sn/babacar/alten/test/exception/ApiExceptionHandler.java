package sn.babacar.alten.test.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import sn.babacar.alten.test.response.CustomResponse;
import sn.babacar.alten.test.util.Constants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
  @ExceptionHandler(value = CustomException.class)
  public ResponseEntity<CustomResponse> handleException(CustomException e) {
    log.error("Erreur code: {}", e.getCodeMessage());
    return ResponseEntity.status(determineHttpStatus(e.getException())).body(getReponse(e));
  }

  private HttpStatus determineHttpStatus(Exception e) {
    log.error(e.getMessage());
    log.error("Erreur: {}", e.getMessage());
    if (e instanceof EntityExistsException) return HttpStatus.CONFLICT;
    if (e instanceof IllegalArgumentException) return HttpStatus.BAD_REQUEST;
    if (e instanceof UnAuthorizedException) return HttpStatus.UNAUTHORIZED;
    if (e instanceof EntityNotFoundException || e instanceof NotFoundException) return HttpStatus.NOT_FOUND;
    else return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  private CustomResponse getReponse(CustomException e) {
    if (e.getException() instanceof EntityExistsException)
      return new CustomResponse(Constants.Message.CONFLICT_BODY, Constants.Status.CONFLICT, e.getCodeMessage(), null);
    if (e.getException() instanceof IllegalArgumentException)
      return new CustomResponse(Constants.Message.BAD_REQUEST_BODY, Constants.Status.BAD_REQUEST, e.getCodeMessage(),
          null);
    if (e.getException() instanceof UnAuthorizedException)
      return new CustomResponse(Constants.Message.UNAUTHORIZED_BODY, Constants.Status.UNAUTHORIZED, e.getCodeMessage(),
          null);
    if (e.getException() instanceof EntityNotFoundException || e.getException() instanceof NotFoundException)
      return new CustomResponse(Constants.Message.NOT_FOUND_BODY, Constants.Status.NOT_FOUND, e.getCodeMessage(), null);
    else
      return new CustomResponse(Constants.Message.SERVER_ERROR_BODY, Constants.Status.INTERNAL_SERVER_ERROR,
          e.getMessage(), null);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<CustomResponse> maxUploadSizeExceeded(MaxUploadSizeExceededException e) {
    log.info("Erreur: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse(Constants.Message.BAD_REQUEST_BODY,
        Constants.Status.BAD_REQUEST, "Taille max 50MB", null));
  }
}

