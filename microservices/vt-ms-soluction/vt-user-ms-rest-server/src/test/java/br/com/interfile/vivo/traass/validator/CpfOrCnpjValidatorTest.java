package br.com.interfile.vivo.traass.validator;

import org.junit.Assert;
import org.junit.Test;

import br.com.interfile.vivo.traass.validation.CpfOrCnpjValidator;

public class CpfOrCnpjValidatorTest {

	private CpfOrCnpjValidator cpfOrCnpjValidator = new CpfOrCnpjValidator();
	
	@Test
	public void test() {
		Assert.assertFalse(cpfOrCnpjValidator.isValidCpf("11111111111"));
		Assert.assertTrue(cpfOrCnpjValidator.isValidCpf("30553271873"));
		Assert.assertTrue(cpfOrCnpjValidator.isValidCnpj("65410143000117"));
		Assert.assertFalse(cpfOrCnpjValidator.isValidCnpj("62410143000117"));
		
		Assert.assertFalse(cpfOrCnpjValidator.isValidCnpj("30553271873"));
		Assert.assertFalse(cpfOrCnpjValidator.isValidCpf("65410143000117"));
	}
}
