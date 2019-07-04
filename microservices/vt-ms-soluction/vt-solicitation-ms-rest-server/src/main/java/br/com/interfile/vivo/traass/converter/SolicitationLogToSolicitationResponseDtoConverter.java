package br.com.interfile.vivo.traass.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.dto.SolicitationResponseDto;
import br.com.interfile.vivo.traass.dto.UserResponseDto;

@Component
public class SolicitationLogToSolicitationResponseDtoConverter
		implements Converter<Solicitation, SolicitationResponseDto> {

	@Override
	public SolicitationResponseDto convert(final Solicitation solicitation) {

		Assert.notNull(solicitation, "solicitation is not null.");

		final Long id = solicitation.getId();
		final String protocolNumber = solicitation.getProtocolNumber();
		final String channelReception = solicitation.getChannelReception();
		final String comment = solicitation.getComment();

		final String entryMailbox = solicitation.getEntryMailbox();

		final String solicitationStatus = solicitation //
				.getSolicitationStatus() //
				.name();

		return SolicitationResponseDto //
				.builder() //
				.id(id) //
				.protocolNumber(protocolNumber) //
				.channelReception(channelReception) //
				.comment(comment) //
				.entryMailbox(entryMailbox) //
				.solicitationStatus(solicitationStatus) //
				.isVisibleForEdit(solicitation //
						.getSolicitationStatus() //
						.visibleForEdit()) //
				.userResponseDto(UserResponseDto //
						.builder() //
						.id(solicitation //
								.getUser() //
								.getId()) //
						.build()) //
				.alterDate(solicitation.getAlterDate())
				.creationDate(solicitation.getCreationDate())
				.build();
	}
}
