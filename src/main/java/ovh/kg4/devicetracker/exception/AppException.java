package ovh.kg4.devicetracker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {

  private final HttpStatus httpCode;

  public AppException(HttpStatus httpCode, String message) {
    super(message);
    this.httpCode = httpCode;
  }

  public AppException(HttpStatus httpCode, String message, Throwable cause) {
    super(message, cause);
    this.httpCode = httpCode;
  }
}
