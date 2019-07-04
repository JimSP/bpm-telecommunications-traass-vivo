package br.com.interfile.vivo.traass.domain;

import java.util.Arrays;

public enum SolicitationType {
	Change, Order, Purchase, Confirmation, Invalid;

	public static SolicitationType create(final String name) {
		return Arrays.asList(SolicitationType.values()) //
				.stream() //
				.filter(predicate -> predicate.name().equals(name)) //
				.findFirst() //
				.orElse(SolicitationType.Invalid);
	}
}
