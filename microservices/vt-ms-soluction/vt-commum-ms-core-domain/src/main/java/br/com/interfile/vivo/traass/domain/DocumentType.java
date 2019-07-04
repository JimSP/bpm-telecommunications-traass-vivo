package br.com.interfile.vivo.traass.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum DocumentType {
	Cpf(11, "###.###.###-##"), //
	Cnpj(14, "##.###.###/####-##"), //
	Rg(9, "##.###.###-#"), //
	EletricBill(null, null), //
	WaterBill(null, null), //
	GasBill(null, null), //
	TelephoneBill(null, null), //
	NotDefined(null, null);

	public static String[] nameValues() {
		final DocumentType[] values = DocumentType.values();
		return Arrays //
				.asList(values) //
				.stream() //
				.map(mapper -> mapper.name()) //
				.collect(Collectors.toList()) //
				.toArray(new String[values.length]);
	}

	private final Integer length;
	private final String mask;

	private DocumentType(final Integer length, final String mask) {
		this.length = length;
		this.mask = mask;
	}

	public static DocumentType create(final String documentType) {
		return Arrays //
				.asList(DocumentType.values()) //
				.stream() //
				.filter(predicate -> predicate //
						.name() //
						.equals(documentType)) //
				.findFirst() //
				.orElse(null);
	}
}
