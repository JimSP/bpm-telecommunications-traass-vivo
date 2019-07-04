package br.com.interfile.vivo.traass.domain;

import java.util.Arrays;

public enum PhoneType {

	Home, Mobile, Work, Contact;

	public static PhoneType create(final String documentType) {
		return Arrays //
				.asList(PhoneType.values()) //
				.stream() //
				.filter(predicate -> predicate //
						.name() //
						.equals(documentType)) //
				.findFirst() //
				.orElse(null);
	}
}
