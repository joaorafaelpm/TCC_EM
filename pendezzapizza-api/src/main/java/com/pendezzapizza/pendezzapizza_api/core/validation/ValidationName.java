package com.pendezzapizza.pendezzapizza_api.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationNameConfig.class)
@ConstraintComposition(CompositionType.OR)
public @interface ValidationName {

    String message() default "contém caracteres inválidos";

    int min() default 1;
    int max() default 255;

    boolean allowHyphen() default true;
    boolean allowApostrophe() default true;
    boolean allowNull() default false ;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}