package com.pendezzapizza.pendezzapizza_api.core.validation.restaurant_owner;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String message() default "Telefone inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
 