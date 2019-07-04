package br.com.interfile.vivo.traass.jpa.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;
import br.com.interfile.vivo.traass.jpa.entities.DonnorEntity;
import br.com.interfile.vivo.traass.jpa.entities.PhoneEntity;

@Component
public class DonnorToDonnorEntityConverter implements Converter<Donnor, DonnorEntity> {

	@Autowired
	private AddressToAddressEntityConverter addressToAddressEntityConverter;

	@Autowired
	private PhoneToPhoneEntityConverter phoneToPhoneEntityConverter;

	@Override
	public DonnorEntity convert(final Donnor donnor) {

		Assert.notNull(donnor.getDocument(), "donnor.document is not null");

		final Long id = donnor.getId();
		final String donnorName = donnor.getDonnorName();
		final String donnorDocumentType = Optional.ofNullable(donnor.getDocument().getDocumentType())
				.orElse(DocumentType.NotDefined).name();
		final String donnorDocumentValue = donnor.getDocument().getDocumentValue();
		final String donnorRg = donnor.getDonnorRg();
		final String donnorEmail = donnor.getDonnorEmail();

		final List<AddressEntity> donnorAddresses = donnor //
				.getDonnorAddresses() //
				.stream() //
				.map(mapper -> addressToAddressEntityConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final PhoneEntity donnorPhone = phoneToPhoneEntityConverter.convert(donnor.getDonnorPhone());

		return DonnorEntity //
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
