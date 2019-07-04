package br.com.interfile.vivo.traass.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder=true)
@Data
public class DigitalDocument {

	private final Long id;
	private final String referenceName;
	private final byte[] data;
	private final String url;
	private final Long bitmask;
	private final Character coparator;
	private final String description;
	private final String extension;
	private final Integer length;
	private final String mineType;
	private final Integer offSet;
	private final String type;
	private final String documentType;
}
