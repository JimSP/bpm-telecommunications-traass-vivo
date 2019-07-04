package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.jpa.entities.DigitalDocumentEntity;

@Component
public class DigitalDocumentEntityToDigitalDocumentConverter
		implements Converter<DigitalDocumentEntity, DigitalDocument> {

	@Override
	public DigitalDocument convert(final DigitalDocumentEntity digitalDocumentEntity) {
		return DigitalDocument //
				.builder() //
				.id(digitalDocumentEntity.getId()) //
				.referenceName(digitalDocumentEntity.getReferenceName()) //
				.url(digitalDocumentEntity.getUrl()) //
				.documentType(digitalDocumentEntity.getDocumentType()) //
				.build();
	}
}
