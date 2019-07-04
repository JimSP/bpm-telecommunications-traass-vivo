package br.com.interfile.vivo.traass.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.dto.DigitalDocumentDto;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

@Component
public class DigitalDocumentDtoToDigitalDocumentConverter implements Converter<DigitalDocumentDto, DigitalDocument> {

	@Override
	public DigitalDocument convert(final DigitalDocumentDto digitalDocumentDto) {

		Assert.notNull(digitalDocumentDto, "digitalDocumentDto id not null.");

		final Optional<byte[]> optionalDate = Optional.ofNullable(digitalDocumentDto.getData());
		final String referenceName = digitalDocumentDto.getReferenceName();
		final DigitalDocument.DigitalDocumentBuilder builder = DigitalDocument.builder();

		optionalDate //
				.ifPresent(consumer -> {
					try {
						final MagicMatch magicMatch = Magic.getMagicMatch(consumer);
						final Long bitmask = magicMatch.getBitmask();
						final Character coparator = magicMatch.getComparator();
						final String description = magicMatch.getDescription();
						final String extension = magicMatch.getExtension();
						final Integer length = magicMatch.getLength();
						final String mineType = magicMatch.getMimeType();
						final Integer offSet = magicMatch.getOffset();
						final String type = magicMatch.getType();

						builder //
								.data(consumer) //
								.bitmask(bitmask) //
								.coparator(coparator) //
								.description(description) //
								.extension(extension) //
								.length(length) //
								.mineType(mineType) //
								.offSet(offSet) //
								.type(type); //
					} catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
						throw new IllegalArgumentException("image data with problem, not possible extract metadada.",
								e);
					}
				});
		return builder //
				.id(digitalDocumentDto.getId()) //
				.url(digitalDocumentDto.getUrl()) //
				.referenceName(referenceName) //
				.documentType(digitalDocumentDto.getDocumentType()) //
				.build();
	}
}
