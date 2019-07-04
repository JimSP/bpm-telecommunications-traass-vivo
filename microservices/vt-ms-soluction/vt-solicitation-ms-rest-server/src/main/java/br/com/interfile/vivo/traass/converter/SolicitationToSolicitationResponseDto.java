package br.com.interfile.vivo.traass.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.dto.AddressDto;
import br.com.interfile.vivo.traass.dto.DigitalDocumentDto;
import br.com.interfile.vivo.traass.dto.DonnorDto;
import br.com.interfile.vivo.traass.dto.SolicitationResponseDto;
import br.com.interfile.vivo.traass.dto.TransfereeDto;
import br.com.interfile.vivo.traass.dto.UserResponseDto;

@Component
public class SolicitationToSolicitationResponseDto implements Converter<Solicitation, SolicitationResponseDto> {

	@Autowired
	private AddressToAddressDtoConverter addressToAddressDtoConverter;

	@Autowired
	private UserToUserResponseDtoConverter userToUserResponseDtoConverter;

	@Autowired
	private DigitalDocumentToDigitalDocumentDtoConverter digitalDocumentDtoToDigitalDocumentConverter;

	@Autowired
	private DonnorToDonnorDtoConverter donnorToDonnorDtoConverter;

	@Autowired
	private TransferreToTransferreDtoConverter transferreToTransferreDtoConverter;

	@Override
	public SolicitationResponseDto convert(final Solicitation solicitation) {

		Assert.notNull(solicitation, "solicitation is not null.");
		Assert.notNull(solicitation.getUser(), "solicitation.user is not null.");
		Assert.notNull(solicitation.getSolicitationStatus(), "solicitation.solicitationStatus is not null.");
		Assert.notNull(solicitation.getDigitalDocuments(), "solicitation.digitalDocuments is not null.");
		Assert.notNull(solicitation.getDonnors(), "solicitation.donnors is not null.");
		Assert.notNull(solicitation.getTransferees(), "solicitation.Transferees is not null.");
		Assert.notNull(solicitation.getSolicitationAddress(), "solicitation.solicitationAddress is not null.");

		final Long id = solicitation.getId();
		final String protocolNumber = solicitation.getProtocolNumber();
		final String channelReception = solicitation.getChannelReception();
		final String comment = solicitation.getComment();

		final List<DigitalDocumentDto> digitalDocuments = solicitation //
				.getDigitalDocuments() //
				.stream() //
				.map(mapper -> digitalDocumentDtoToDigitalDocumentConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final String entryMailbox = solicitation.getEntryMailbox();
		final UserResponseDto userId = userToUserResponseDtoConverter.convert(solicitation.getUser());

		final AddressDto solicitationAddress = addressToAddressDtoConverter
				.convert(solicitation.getSolicitationAddress());

		final String solicitationStatus = solicitation //
				.getSolicitationStatus() //
				.name();

		final List<DonnorDto> donnors = solicitation //
				.getDonnors() //
				.stream() //
				.map(mapper -> donnorToDonnorDtoConverter //
						.convert(mapper)) //
				.collect(Collectors.toList());

		final List<TransfereeDto> transferees = solicitation //
				.getTransferees() //
				.stream() //
				.map(mapper -> transferreToTransferreDtoConverter //
						.convert(mapper)) //
				.collect(Collectors.toList());

		return SolicitationResponseDto //
				.builder() //
				.id(id) //
				.protocolNumber(protocolNumber) //
				.channelReception(channelReception) //
				.comment(comment) //
				.digitalDocuments(digitalDocuments) //
				.entryMailbox(entryMailbox) //
				.userResponseDto(userId) //
				.solicitationAddress(solicitationAddress) //
				.solicitationStatus(solicitationStatus) //
				.isVisibleForEdit(solicitation //
						.getSolicitationStatus() //
						.visibleForEdit()) //
				.donnors(donnors) //
				.transferees(transferees) //
				.alterDate(solicitation.getAlterDate())
				.creationDate(solicitation.getCreationDate())
				.build();
	}
}
