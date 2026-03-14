package com.pendezzapizza.pendezzapizza_api.core.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.io.Serial;

@AllArgsConstructor
@Getter
public class ValidationException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    private BindingResult bindingResult ;

}
