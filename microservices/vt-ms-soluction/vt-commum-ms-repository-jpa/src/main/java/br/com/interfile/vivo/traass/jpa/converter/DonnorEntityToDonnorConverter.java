package br.com.interfile.vivo.traass.jpa.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.jpa.entities.DonnorEntity;

@Component
public class DonnorEntityToDonnorConverter implements Converter<DonnorEntity, Donnor> {

	@Autowired
	private PhoneEntityToPhoneConverter phoneEntityToPhoneConverter;

	@Autowired
	private AddressEntityToAddressConverter addressEntityToAddressConverter;

	@Override
	public Donnor convert(final DonnorEntity donnorEntity) {

		Assert.notEmpty(donnorEntity //
				.getDonnorAddresses(), "donnorEntity.DonnorAddresses is not empty");

		final Long id = donnorEntity.getId();
		final String donnorName = donnorEntity.getDonnorName();
		final String donnorDocumentType = donnorEntity.getDonnorDocumentType();
		final String donnorDocumentValue = donnorEntity.getDonnorDocumentValue();
		final String donnorRg = donnorEntity.getDonnorRg();
		final String donnorEmail = donnorEntity.getDonnorEmail();
		final Phone donnorPhone = phoneEntityToPhoneConverter.convert(donnorEntity.getDonnorPhone());
		
		final Document document = Document
				.builder()
				.documentType(DocumentType.create(donnorDocumentType))
				.documentValue(donnorDocumentValue)
				.build();

		final List<Address> donnorAddresses = donnorEntity //
				.getDonnorAddresses() //
				.stream() //
				.map(mapper -> addressEntityToAddressConverter.convert(mapper)) //
				.collect(Collectors.toList());

		return Donnor //
				.builder() //
				.id(id) //
				.donnorName(donnorName) //
				.document(document) //
				.donnorRg(donnorRg) //
				.donnorEmail(donnorEmail) //
				.donnorPhone(donnorPhone) //
				.donnorAddresses(donnorAddresses) //
				.build();
	}
}
