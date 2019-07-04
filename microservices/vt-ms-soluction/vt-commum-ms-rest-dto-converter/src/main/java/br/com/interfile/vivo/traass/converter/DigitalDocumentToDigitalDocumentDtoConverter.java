package br.com.interfile.vivo.traass.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.dto.DigitalDocumentDto;

@Component
public class DigitalDocumentToDigitalDocumentDtoConverter implements Converter<DigitalDocument, DigitalDocumentDto> {

	@Override
	public DigitalDocumentDto convert(final DigitalDocument digitalDocument) {
		Assert.notNull(digitalDocument, "digitalDocument is not null.");

		return DigitalDocumentDto //
				.builder() //
				.id(digitalDocument.getId()) //
				.referenceName(digitalDocument.getReferenceName()) //
				.url(digitalDocument.getUrl()) //
				.documentType(digitalDocument.getDocumentType()) //
				.build();
	}
}
