package br.com.interfile.vivo.traass.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.Transferee;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.dto.SolicitationRequestDto;

@Component
public class SolicitationRequestDtoToSolicitationConverter implements Converter<SolicitationRequestDto, Solicitation> {

	@Autowired
	private AddresDtoToAddressConverter addresDtoToAddressConverter;

	@Autowired
	private DigitalDocumentDtoToDigitalDocumentConverter digitalDocumentDtoToDigitalDocumentConverter;
	
	@Autowired
	private DonnorDtoToDonnorConverter dDonnorDtoToDonnorConverter;

	@Autowired
	private TransferreDtoToTransferreConverter transferreDtoToTransferreConverter;

	@Override
	public Solicitation convert(final SolicitationRequestDto solicitationRequestDto) {
		Assert.notNull(solicitationRequestDto, "solicitationRequestDto is not null.");
		Assert.isNull(solicitationRequestDto.getSolicitationStatus(),
				"solicitationRequestDto.solicitationStatus is null.");
		Assert.notNull(solicitationRequestDto.getDigitalDocuments(),
				"solicitationRequestDto.digitalDocuments is not null.");
		Assert.notEmpty(solicitationRequestDto.getDonnors(),
				"solicitationRequestDto.donnors is not empty.");
		Assert.notEmpty(solicitationRequestDto.getTransferees(),
				"solicitationRequestDto.transferees is not empty.");
		Assert.notNull(solicitationRequestDto.getSolicitationAddress(),
				"solicitationRequestDto.solicitationAddress is not null.");

		final String protocolNumber = solicitationRequestDto.getProtocolNumber();
		final String channelReception = solicitationRequestDto.getChannelReception();
		final String comment = solicitationRequestDto.getComment();
		final String entryMailbox = solicitationRequestDto.getEntryMailbox();
		final Long userId = solicitationRequestDto.getUserId();

		final List<DigitalDocument> digitalDocuments = solicitationRequestDto //
				.getDigitalDocuments() //
				.stream() //
				.map(mapper -> digitalDocumentDtoToDigitalDocumentConverter //
						.convert(mapper)) //
				.collect(Collectors //
						.toList());


		final Address solicitationAddress = addresDtoToAddressConverter
				.convert(solicitationRequestDto.getSolicitationAddress());

		final List<Donnor> donnors = solicitationRequestDto //
				.getDonnors() //
				.stream() //
				.map(mapper -> dDonnorDtoToDonnorConverter //
						.convert(mapper)) //
				.collect(Collectors.toList());

		final List<Transferee> transferees = solicitationRequestDto //
				.getTransferees() //
				.stream() //
				.map(mapper -> transferreDtoToTransferreConverter //
						.convert(mapper)) //
				.collect(Collectors.toList());
		
		return Solicitation //
				.builder() //
				.protocolNumber(protocolNumber) //
				.channelReception(channelReception) //
				.comment(comment) //
				.digitalDocuments(digitalDocuments) //
				.donnors(donnors) //
				.entryMailbox(entryMailbox) //
				.user(User //
						.builder() //
						.id(userId) //
						.build()) //
				.transferees(transferees) //
				.solicitationAddress(solicitationAddress) //
				.build();
	}
}
