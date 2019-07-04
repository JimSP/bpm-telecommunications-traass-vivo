package br.com.interfile.vivo.traass.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class EqualValueConstraintValidator implements ConstraintValidator<EqualsValue, String> {

	private List<String> equalsValues;

	public void initialize(final EqualsValue equalsValue) {
		this.equalsValues = Arrays.asList(equalsValue.values());
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return Optional //
				.ofNullable(value) //
				.filter(StringUtils::isNotBlank) //
				.map(applyIfMatches(equalsValues)) //
				.orElse(Boolean.TRUE);
	}

	private static Function<String, Boolean> applyIfMatches(final List<String> values) {
		return value -> values //
				.stream() //
				.anyMatch(valueToMatch -> StringUtils //
						.equalsIgnoreCase(valueToMatch, value));
	}
}
