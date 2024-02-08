package ovh.kg4.devicetracker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleBookNotFoundException(ValidationException ex) {

    log.error(ex.getMessage());

    return ex.getMessage();
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<String> handleAppException(AppException ex) {

    log.error(ex.getMessage(), ex);

    return ResponseEntity.status(ex.getHttpCode()).body(ex.getMessage());
  }
}
