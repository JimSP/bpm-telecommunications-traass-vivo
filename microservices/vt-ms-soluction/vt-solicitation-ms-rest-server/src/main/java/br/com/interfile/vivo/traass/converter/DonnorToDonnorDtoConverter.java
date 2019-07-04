package br.com.interfile.vivo.traass.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.dto.AddressDto;
import br.com.interfile.vivo.traass.dto.DonnorDto;
import br.com.interfile.vivo.traass.dto.PhoneDto;

@Component
public class DonnorToDonnorDtoConverter implements Converter<Donnor, DonnorDto> {

	@Autowired
	private AddressToAddressDtoConverter addressToAddressDtoConverter;

	@Autowired
	private PhoneToPhoneDtoConverter phoneToPhoneDtoConverter;

	@Override
	public DonnorDto convert(final Donnor donnor) {
		final Long id = donnor.getId();
		final String donnorName = donnor.getDonnorName();
		final String donnorDocumentType = Optional.ofNullable(donnor.getDocument().getDocumentType())
				.orElse(DocumentType.NotDefined).name();
		final String donnorDocumentValue = donnor.getDocument().getDocumentValue();
		final String donnorRg = donnor.getDonnorRg();
		final String donnorEmail = donnor.getDonnorEmail();

		final List<AddressDto> donnorAddresses = donnor //
				.getDonnorAddresses() //
				.stream() //
				.map(mapper -> addressToAddressDtoConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final PhoneDto donnorPhone = phoneToPhoneDtoConverter.convert(donnor.getDonnorPhone());

		return DonnorDto //
				.builder() //
				.id(id) //
				.donnorName(donnorName) //
				.donnorDocumentType(donnorDocumentType) //
				.donnorDocumentValue(donnorDocumentValue) //
				.donnorRg(donnorRg) //
				.donnorEmail(donnorEmail) //
				.donnorAddresses(donnorAddresses) //
				.donnorPhone(donnorPhone) //
				.build();
	}

}
