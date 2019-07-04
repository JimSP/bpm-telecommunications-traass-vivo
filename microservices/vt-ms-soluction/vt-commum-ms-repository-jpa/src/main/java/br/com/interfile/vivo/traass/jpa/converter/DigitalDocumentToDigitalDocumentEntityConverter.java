package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.jpa.entities.DigitalDocumentEntity;

@Component
public class DigitalDocumentToDigitalDocumentEntityConverter implements Converter<DigitalDocument, DigitalDocumentEntity> {

	@Override
	public DigitalDocumentEntity convert(final DigitalDocument digitalDocument) {
		
		Assert.notNull(digitalDocument, "digitalDocument is not null.");
		Assert.notNull(digitalDocument, "digitalDocument.documentType is not null.");
		
		return DigitalDocumentEntity //
				.builder() //
				.id(digitalDocument.getId()) //
				.url(digitalDocument.getUrl()) //
				.referenceName(digitalDocument.getReferenceName()) //
				.documentType(digitalDocument.getDocumentType()) //
				.build();
	}
}
