package com.miniblog.account.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@NotBlank
@Size(min = 36, max = 36)
@Pattern(regexp = "^[0-9a-f\\-]{36}$")
public @interface ValidUuid {
    String message() default "Invalid UUID format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
