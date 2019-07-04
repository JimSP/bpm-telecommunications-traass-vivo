package br.com.interfile.vivo.traass.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.dto.DonnorDto;

@Component
public class DonnorDtoToDonnorConverter implements Converter<DonnorDto, Donnor> {

	@Autowired
	private AddresDtoToAddressConverter addresDtoToAddressConverter;

	@Autowired
	private PhoneDtoPhoneConverter phoneDtoPhoneConverter;

	@Override
	public Donnor convert(final DonnorDto donnorDto) {

		final Long id = donnorDto.getId();
		final String donnorName = donnorDto.getDonnorName();
		final Document document = Document //
				.builder() //
				.documentValue(donnorDto.getDonnorDocumentValue()) //
				.documentType(DocumentType //
						.create(donnorDto //
								.getDonnorDocumentType())) //
				.build();
		final String donnorRg = donnorDto.getDonnorRg();
		final String donnorEmail = donnorDto.getDonnorEmail();
		final Phone donnorPhone = phoneDtoPhoneConverter.convert(donnorDto.getDonnorPhone());
		final List<Address> donnorAddresses = donnorDto //
				.getDonnorAddresses() //
				.stream() //
				.map(mapper -> addresDtoToAddressConverter //
						.convert(mapper)) //
				.collect(Collectors //
						.toList());

		return Donnor //
				.builder() //
				.id(id) //
				.document(document) //
				.donnorAddresses(donnorAddresses) //
				.donnorEmail(donnorEmail) //
				.donnorName(donnorName) //
				.donnorPhone(donnorPhone) //
				.donnorRg(donnorRg) //
				.build();
	}
}
