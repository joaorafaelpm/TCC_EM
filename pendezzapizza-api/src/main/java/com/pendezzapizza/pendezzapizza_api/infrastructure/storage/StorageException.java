package com.pendezzapizza.pendezzapizza_api.infrastructure.storage;

import java.io.Serial;

public class StorageException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public StorageException(String message) {
    super(message);
  }
  public StorageException(String message , Throwable cause) {
    super(message , cause);
  }
}
