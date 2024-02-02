package se.qred.loan.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import se.qred.loan.validation.validators.SwedishOrganizationNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {SwedishOrganizationNumberValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SwedishOrganizationNumberValidation {

    String message() default "not a valid swedish organization number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}