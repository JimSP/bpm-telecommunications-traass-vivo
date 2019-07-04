package br.com.interfile.vivo.traass.jpa.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.Transferee;
import br.com.interfile.vivo.traass.jpa.entities.TransfereeEntity;

@Component
public class TransfereeEntityToTransfereeConverter implements Converter<TransfereeEntity, Transferee> {

	@Autowired
	private PhoneEntityToPhoneConverter phoneEntityToPhoneConverter;

	@Autowired
	private AddressEntityToAddressConverter addressEntityToAddressConverter;

	@Override
	public Transferee convert(final TransfereeEntity transfereeEntity) {

		final Long id = transfereeEntity.getId();
		final String transfereeName = transfereeEntity.getTransfereeName();
		final String transfereeDocumentType = transfereeEntity.getTransfereeDocumentType();
		final String transfereeDocumentValue = transfereeEntity.getTransfereeDocumentValue();
		final String transfereeEmail = transfereeEntity.getTransfereeEmail();
		final Phone transfereePhone = phoneEntityToPhoneConverter.convert(transfereeEntity.getTransfereePhone());

		final List<Address> transfereeAddresses = transfereeEntity //
				.getTransfereeAddresses() //
				.stream() //
				.map(mapper -> addressEntityToAddressConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final Document document = Document
				.builder()
				.documentType(DocumentType.create(transfereeDocumentType))
				.documentValue(transfereeDocumentValue)
				.build();

		return Transferee //
				.builder() //
				.id(id) //
				.transfereeName(transfereeName) //
				.document(document) //
				.transfereeEmail(transfereeEmail) //
				.transfereePhone(transfereePhone) //
				.transfereeAddresses(transfereeAddresses) //
				.build();
	}
}
