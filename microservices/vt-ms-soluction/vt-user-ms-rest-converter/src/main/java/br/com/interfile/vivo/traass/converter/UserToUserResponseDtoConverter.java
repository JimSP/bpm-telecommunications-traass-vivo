package br.com.interfile.vivo.traass.converter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.converter.AddressToAddressDtoConverter;
import br.com.interfile.vivo.traass.converter.PhoneToPhoneDtoConverter;
import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.dto.AddressDto;
import br.com.interfile.vivo.traass.dto.PhoneDto;
import br.com.interfile.vivo.traass.dto.UserResponseDto;

@Component
public class UserToUserResponseDtoConverter implements Converter<User, UserResponseDto> {

	@Autowired
	private PhoneToPhoneDtoConverter phoneToPhoneDtoConverter;

	@Autowired
	private AddressToAddressDtoConverter addressToAddressDtoConverter;

	@Override
	public UserResponseDto convert(final User user) {
		final UserResponseDto.UserResponseDtoBuilder builder = //
				UserResponseDto //
						.builder();

		final List<Document> documents = Optional //
				.ofNullable(user.getDocuments()) //
				.orElse(Collections.emptyList());

		final List<Phone> phones = Optional //
				.ofNullable(user.getPhones()) //
				.orElse(Collections.emptyList());

		final List<Address> addresses = Optional //
				.ofNullable(user.getAddresses()) //
				.orElse(Collections.emptyList());

		if (!documents.isEmpty()) {
			final Document document = documents.get(0);
			builder //
					.documentType(Optional.ofNullable(document //
							.getDocumentType()) //
							.orElse(DocumentType.NotDefined) //
							.name()) //
					.documentValue(document //
							.getDocumentValue());
		}

		final List<PhoneDto> phoneDtos = phones //
				.stream() //
				.map(mapper -> phoneToPhoneDtoConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final List<AddressDto> addressDtos = addresses //
				.stream() //
				.map(mapper -> addressToAddressDtoConverter.convert(mapper)) //
				.collect(Collectors.toList());

		return builder //
				.id(user.getId()) //
				.name(user.getName()) //
				.email(user.getEmail()) //
				.password(user.getPassword()) //
				.phones(phoneDtos) //
				.addresses(addressDtos) //
				.build();
	}

}
