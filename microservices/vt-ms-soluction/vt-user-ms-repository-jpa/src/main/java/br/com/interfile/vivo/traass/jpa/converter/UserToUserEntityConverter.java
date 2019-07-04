package br.com.interfile.vivo.traass.jpa.converter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;
import br.com.interfile.vivo.traass.jpa.entities.PhoneEntity;
import br.com.interfile.vivo.traass.jpa.entities.UserEntity;

@Component
public class UserToUserEntityConverter implements Converter<User, UserEntity> {

	@Autowired
	private AddressToAddressEntityConverter addressToAddressEntityConverter;

	@Autowired
	private PhoneToPhoneEntityConverter phoneToPhoneEntityConverter;

	@Override
	public UserEntity convert(final User user) {
		final UserEntity.UserEntityBuilder builder = UserEntity.builder();

		final List<Document> documents = Optional //
				.ofNullable(user.getDocuments()) //
				.orElse(Collections.emptyList());

		if (!documents.isEmpty()) {
			final Document document = documents.get(0);

			builder//
					.documentType(Optional.ofNullable(document //
							.getDocumentType()) //
							.orElse(DocumentType.Cpf)//
							.name()) //
					.documentValue(document//
							.getDocumentValue());
		}

		final List<AddressEntity> addressEntitys = Optional //
				.ofNullable(user.getAddresses()) //
				.orElse(Collections.emptyList()) //
				.stream() //
				.map(mapper -> addressToAddressEntityConverter.convert(mapper)) //
				.collect(Collectors.toList()); //

		final List<PhoneEntity> phones = Optional //
				.ofNullable(user.getPhones()) //
				.orElse(Collections.emptyList()) //
				.stream() //
				.map(mapper -> phoneToPhoneEntityConverter.convert(mapper)) //
				.collect(Collectors.toList()); //

		return builder //
				.id(user.getId()) //
				.addressEntitys(addressEntitys) //
				.phoneEntitys(phones) //
				.name(user.getName()) //
				.email(user.getEmail()) //
				.password(user.getPassword()) //
				.verified(user.getVerified()) //
				.build();
	}
}
