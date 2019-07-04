package br.com.interfile.vivo.traass.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

public class CpfOrCnpjConstraintValidator implements ConstraintValidator<CpfOrCnpj, String> {

	private CpfOrCnpjValidator cpfOrCnpjValidator;
	private CpfOrCnpj cpfOrCnpj;

	public CpfOrCnpjConstraintValidator() {
		final CPFValidator cpfValidator = new CPFValidator();
		final CNPJValidator cnpjValidator = new CNPJValidator();
		this.cpfOrCnpjValidator = new CpfOrCnpjValidator(cpfValidator, cnpjValidator);
	}

	public CpfOrCnpjConstraintValidator(final CPFValidator cpfValidator, final CNPJValidator cnpjValidator) {
		this.cpfOrCnpjValidator = new CpfOrCnpjValidator(cpfValidator, cnpjValidator);
	}

	public CpfOrCnpj getCpfOrCnpj() {
		return cpfOrCnpj;
	}

	public void initialize(final CpfOrCnpj cpfOrCnpj) {
		this.cpfOrCnpj = cpfOrCnpj;
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return cpfOrCnpjValidator.isValid(value);
	}
}
