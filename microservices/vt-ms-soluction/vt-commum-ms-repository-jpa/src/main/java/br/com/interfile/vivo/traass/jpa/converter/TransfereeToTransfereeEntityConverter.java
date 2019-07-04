package br.com.interfile.vivo.traass.jpa.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Transferee;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;
import br.com.interfile.vivo.traass.jpa.entities.PhoneEntity;
import br.com.interfile.vivo.traass.jpa.entities.TransfereeEntity;

@Component
public class TransfereeToTransfereeEntityConverter implements Converter<Transferee, TransfereeEntity> {

	@Autowired
	private AddressToAddressEntityConverter addressToAddressEntityConverter;

	@Autowired
	private PhoneToPhoneEntityConverter phoneToPhoneEntityConverter;

	@Override
	public TransfereeEntity convert(final Transferee transferee) {
		final Long id = transferee.getId();
		final String transfereeName = transferee.getTransfereeName();
		final String transfereeDocumentType = transferee.getDocument().getDocumentType().name();
		final String transfereeDocumentValue = transferee.getDocument().getDocumentValue();
		final String transfereeEmail = transferee.getTransfereeEmail();
		final PhoneEntity transfereePhone = phoneToPhoneEntityConverter.convert(transferee.getTransfereePhone());
		final List<AddressEntity> transfereeAddresses = transferee.getTransfereeAddresses().stream()
				.map(mapper -> addressToAddressEntityConverter.convert(mapper)).collect(Collectors.toList());

		return TransfereeEntity //
				.builder() //
				.id(id) //
				.transfereeName(transfereeName) //
				.transfereeDocumentType(transfereeDocumentType) //
				.transfereeDocumentValue(transfereeDocumentValue) //
				.transfereePhone(transfereePhone) //
				.transfereeEmail(transfereeEmail) //
				.transfereeAddresses(transfereeAddresses) //
				.build();
	}
}
