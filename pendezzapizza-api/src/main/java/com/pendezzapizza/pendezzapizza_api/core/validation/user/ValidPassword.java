package com.pendezzapizza.pendezzapizza_api.core.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Senha deve ter no mínimo 8 caracteres, incluindo letra maiúscula, minúscula e caractere especial";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}