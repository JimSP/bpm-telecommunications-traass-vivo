package br.com.interfile.vivo.traass.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EqualValueConstraintValidator.class)
public @interface EqualsValue {

	public String message() default "{br.com.interfile.vivo.traass.validation.EqualsValue.message}";

	public Class<?>[] groups() default {};

	public Class<? extends Payload>[] payload() default {};

	public String[] values() default {};

}
