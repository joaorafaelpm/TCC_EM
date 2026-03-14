package com.pendezzapizza.pendezzapizza_api.domain.exception;

import java.io.Serial;

public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message , Throwable cause) {
        super(message , cause);
    }
}
