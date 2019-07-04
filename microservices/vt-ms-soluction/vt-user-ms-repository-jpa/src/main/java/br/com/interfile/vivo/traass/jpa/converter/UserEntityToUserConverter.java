package br.com.interfile.vivo.traass.jpa.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.jpa.entities.UserEntity;

@Component
public class UserEntityToUserConverter implements Converter<UserEntity, User> {

	@Autowired
	private AddressEntityToAddressConverter addressEntityToAddressConverter;
	
	@Autowired
	private PhoneEntityToPhoneConverter phoneEntityToPhoneConverter;
	
	@Override
	public User convert(final UserEntity userEntity) {
		final User.UserBuilder builder = User.builder();
		final List<Document> documents = Arrays //
				.asList(Document //
						.builder() //
						.documentValue(userEntity //
								.getDocumentValue()) //
						.documentType( //
								DocumentType //
										.valueOf(userEntity //
												.getDocumentType()))
						.build());

		final List<Phone> phones = userEntity //
				.getPhoneEntitys() //
				.stream() //
				.map(mapper -> phoneEntityToPhoneConverter.convert(mapper))
				.collect(Collectors.toList());

		final List<Address> address = userEntity //
				.getAddressEntitys() //
				.stream() //
				.map(mapper -> addressEntityToAddressConverter.convert(mapper))
				.collect(Collectors.toList());

		return builder //
				.id(userEntity.getId()) //
				.name(userEntity.getName()) //
				.email(userEntity.getEmail()) //
				.password(userEntity.getPassword()) //
				.documents(documents) //
				.phones(phones) //
				.addresses(address) //
				.verified(userEntity.getVerified())
				.build();
	}

}
