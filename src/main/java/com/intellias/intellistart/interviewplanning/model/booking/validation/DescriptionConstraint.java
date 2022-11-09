package com.intellias.intellistart.interviewplanning.model.booking.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DescriptionValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DescriptionConstraint {
  String message() default "Sorry, passwords does not match";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
