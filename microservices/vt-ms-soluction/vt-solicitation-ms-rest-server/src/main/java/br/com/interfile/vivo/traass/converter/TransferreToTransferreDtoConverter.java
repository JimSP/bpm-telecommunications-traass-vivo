package br.com.interfile.vivo.traass.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Transferee;
import br.com.interfile.vivo.traass.dto.AddressDto;
import br.com.interfile.vivo.traass.dto.PhoneDto;
import br.com.interfile.vivo.traass.dto.TransfereeDto;

@Component
public class TransferreToTransferreDtoConverter implements Converter<Transferee, TransfereeDto>{

	@Autowired
	private AddressToAddressDtoConverter addressToAddressDtoConverter;

	@Autowired
	private PhoneToPhoneDtoConverter phoneToPhoneDtoConverter;
	
	@Override
	public TransfereeDto convert(final Transferee transferee) {
		final Long id = transferee.getId();
		final String transfereeName = transferee.getTransfereeName();
		final String transfereeDocumentType = transferee.getDocument().getDocumentType().name();
		final String transfereeDocumentValue = transferee.getDocument().getDocumentValue();
		final String transfereeEmail = transferee.getTransfereeEmail();
		final PhoneDto transfereePhone = phoneToPhoneDtoConverter.convert(transferee.getTransfereePhone());
		final List<AddressDto> transfereeAddresses = transferee.getTransfereeAddresses().stream()
				.map(mapper -> addressToAddressDtoConverter.convert(mapper)).collect(Collectors.toList());

		return TransfereeDto //
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
