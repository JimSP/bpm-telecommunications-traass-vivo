package br.com.interfile.vivo.traass.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder(toBuilder=true)
public class Document {

	private final DocumentType documentType;
	private final String documentValue;
}
