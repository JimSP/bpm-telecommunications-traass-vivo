package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.SolicitationStatus;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.jpa.entities.SolicitationLogEntity;

@Component
public class SolicitationLogEntityToSolicitationConverter implements Converter<SolicitationLogEntity, Solicitation>{

	@Override
	public Solicitation convert(final SolicitationLogEntity solicitationLogEntity) {
		Assert.notNull(solicitationLogEntity, "solicitationLogEntity is not null.");
		Assert.notNull(solicitationLogEntity.getUserId(), "solicitationLogEntity.user is not null.");
		Assert.notNull(solicitationLogEntity.getSolicitationStatus(), "solicitationLogEntity.solicitationStatus is not null.");

		final String channelReception = solicitationLogEntity.getChannelReception();
		final String comment = solicitationLogEntity.getComment();
		final String entryMailbox = solicitationLogEntity.getEntryMailbox();
		final Long userId = solicitationLogEntity.getUserId();
		final SolicitationStatus solicitationStatus = SolicitationStatus
				.create(solicitationLogEntity.getSolicitationStatus());
		
		return Solicitation //
				.builder() //
				.id(solicitationLogEntity.getSolicitationId())
				.protocolNumber(solicitationLogEntity.getProtocolNumber()) //
				.channelReception(channelReception) //
				.comment(comment) //
				.entryMailbox(entryMailbox) //
				.user(User.builder().id(userId).build()) //
				.solicitationStatus(solicitationStatus) //
				.creationDate(solicitationLogEntity.getCreateDate()) //
				.alterDate(solicitationLogEntity.getAlterDate()) //
				.build();
	}

}
