package br.com.interfile.vivo.traass.validation;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

public class CpfOrCnpjValidator {

	private final CPFValidator cpfValidator;
	private final CNPJValidator cnpjValidator;

	public CpfOrCnpjValidator() {
		this.cpfValidator = new CPFValidator();
		this.cnpjValidator = new CNPJValidator();
	}

	public CpfOrCnpjValidator(final CPFValidator cpfValidator, final CNPJValidator cnpjValidator) {
		this.cpfValidator = cpfValidator;
		this.cnpjValidator = cnpjValidator;
	}

	public CPFValidator getCpfValidator() {
		return cpfValidator;
	}

	public CNPJValidator getCnpjValidator() {
		return cnpjValidator;
	}

	public Boolean isValid(final String documentValue) {
		return isValidCpf(documentValue) || isValidCnpj(documentValue);
	}

	public Boolean isValidCpf(final String cpf) {
		try {
			if (cpfValidator.isEligible(cpf)) {
				cpfValidator.assertValid(cpf);
				return Boolean.TRUE;
			}else {
				return Boolean.FALSE;
			}
		} catch (InvalidStateException e) {
			return Boolean.FALSE;
		}
	}

	public Boolean isValidCnpj(final String cnpj) {
		try {
			if (cnpjValidator.isEligible(cnpj)) {
				cnpjValidator.assertValid(cnpj);
				return Boolean.TRUE;
			}else {
				return Boolean.FALSE;
			}
		} catch (InvalidStateException e) {
			return Boolean.FALSE;
		}
	}
}
