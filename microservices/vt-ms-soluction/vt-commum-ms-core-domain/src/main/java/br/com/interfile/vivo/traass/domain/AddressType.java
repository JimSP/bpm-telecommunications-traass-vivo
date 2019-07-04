package br.com.interfile.vivo.traass.domain;

import java.util.Arrays;

public enum AddressType {
	Home, Comercial, Delivery, Correspondence, Undefined;

	public static AddressType create(final String addressType) {
		return Arrays //
				.asList(AddressType.values()) //
				.stream() //
				.filter(predicate -> predicate //
						.name() //
						.equals(addressType)) //
				.findFirst() //
				.orElse(null);
	}
}
