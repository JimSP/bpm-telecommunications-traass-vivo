package br.com.interfile.vivo.traass.converter;

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
import br.com.interfile.vivo.traass.dto.TransfereeDto;

@Component
public class TransferreDtoToTransferreConverter implements Converter<TransfereeDto, Transferee> {

	@Autowired
	private AddresDtoToAddressConverter addresDtoToAddressConverter;

	@Autowired
	private PhoneDtoPhoneConverter phoneDtoPhoneConverter;

	@Override
	public Transferee convert(final TransfereeDto transferee) {
		final Long id = transferee.getId();
		final String transfereeName = transferee.getTransfereeName();
		final String transfereeDocumentType = transferee.getTransfereeDocumentType();
		final String transfereeDocumentValue = transferee.getTransfereeDocumentValue();
		final String transfereeEmail = transferee.getTransfereeEmail();
		final Phone transfereePhone = phoneDtoPhoneConverter.convert(transferee.getTransfereePhone());
		final List<Address> transfereeAddresses = transferee.getTransfereeAddresses().stream()
				.map(mapper -> addresDtoToAddressConverter.convert(mapper)).collect(Collectors.toList());

		return Transferee //
				.builder() //
				.id(id) //
				.transfereeName(transfereeName) //
				.document(Document //
						.builder() //
						.documentValue(transfereeDocumentValue) //
						.documentType(DocumentType.create(transfereeDocumentType)) //
						.build()) //
				.transfereePhone(transfereePhone) //
				.transfereeEmail(transfereeEmail) //
				.transfereeAddresses(transfereeAddresses) //
				.build();
	}
}
