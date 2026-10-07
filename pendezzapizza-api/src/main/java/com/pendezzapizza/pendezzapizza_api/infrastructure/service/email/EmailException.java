package com.pendezzapizza.pendezzapizza_api.infrastructure.service.email;

import java.io.Serial;

/**
 * Excessão específica para erro no envio de email
 */
public class EmailException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public EmailException(String message) {
    super(message);
  }
  public EmailException(String message , Throwable cause) {
    super(message , cause);
  }
}
