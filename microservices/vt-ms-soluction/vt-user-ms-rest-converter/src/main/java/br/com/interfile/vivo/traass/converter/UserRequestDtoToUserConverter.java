package br.com.interfile.vivo.traass.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.dto.UserRequestDto;

@Component
public class UserRequestDtoToUserConverter implements Converter<UserRequestDto, User> {

	@Autowired
	private PhoneDtoPhoneConverter phoneDtoPhoneConverter;

	@Autowired
	private AddresDtoToAddressConverter addresDtoToAddressConverter;

	@Override
	public User convert(final UserRequestDto userRequestDto) {
		final List<Document> documents = Arrays.asList(Document //
				.builder() //
				.documentType(DocumentType.valueOf(userRequestDto.getDocumentType())) //
				.documentValue(userRequestDto.getDocumentValue()) //
				.build());
		
		final List<Phone> phones = Optional.ofNullable(userRequestDto.getPhones()).orElse(Collections.emptyList()) //
				.stream() //
				.map(mapper -> phoneDtoPhoneConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final List<Address> addresses = Optional.ofNullable(userRequestDto.getAddresses()).orElse(Collections.emptyList()) //
				.stream() //
				.map(mapper -> addresDtoToAddressConverter.convert(mapper)) //
				.collect(Collectors.toList());

		return User //
				.builder() //
				.name(userRequestDto.getName()) //
				.email(userRequestDto.getEmail()) //
				.password(userRequestDto.getPassword()) //
				.documents(documents) //
				.phones(phones) //
				.addresses(addresses) //
				.verified(Boolean.FALSE) //
				.build();
	}
}
