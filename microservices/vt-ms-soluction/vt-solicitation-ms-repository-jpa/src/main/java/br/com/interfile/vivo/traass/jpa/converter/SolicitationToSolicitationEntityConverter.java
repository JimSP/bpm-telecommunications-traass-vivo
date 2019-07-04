package br.com.interfile.vivo.traass.jpa.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.SolicitationStatus;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;
import br.com.interfile.vivo.traass.jpa.entities.DigitalDocumentEntity;
import br.com.interfile.vivo.traass.jpa.entities.DonnorEntity;
import br.com.interfile.vivo.traass.jpa.entities.SolicitationEntity;
import br.com.interfile.vivo.traass.jpa.entities.TransfereeEntity;
import br.com.interfile.vivo.traass.jpa.entities.UserEntity;

@Component
public class SolicitationToSolicitationEntityConverter implements Converter<Solicitation, SolicitationEntity> {

	@Autowired
	private AddressToAddressEntityConverter addressToAddressEntityConverter;

	@Autowired
	private DigitalDocumentToDigitalDocumentEntityConverter digitalDocumentEntityToDigitalDocumentConverter;

	@Autowired
	private DonnorToDonnorEntityConverter donnorToDonnorEntityConverter;

	@Autowired
	private TransfereeToTransfereeEntityConverter transfereeToTransfereeEntityConverter;

	@Override
	public SolicitationEntity convert(final Solicitation solicitation) {

		Assert.notNull(solicitation, "solicitation is not null.");
		Assert.notNull(solicitation.getUser(), "solicitation.user is not null.");
		Assert.notNull(solicitation.getDigitalDocuments(), "solicitation.digitalDocuments is not null.");
		Assert.notNull(solicitation.getDonnors(), "solicitation.donnor is not null.");
		Assert.notNull(solicitation.getTransferees(), "solicitation.transferees is not null.");
		Assert.notNull(solicitation.getSolicitationAddress(), "solicitation.solicitationAddress is not null.");

		final Optional<SolicitationStatus> solicitationStatusOptional = Optional.ofNullable(solicitation.getSolicitationStatus());
		final String protocolNumber = solicitation.getProtocolNumber();
		final String channelReception = solicitation.getChannelReception();
		final String comment = solicitation.getComment();
		final List<DigitalDocumentEntity> digitalDocuments = solicitation //
				.getDigitalDocuments() //
				.stream() //
				.map(mapper -> digitalDocumentEntityToDigitalDocumentConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final String entryMailbox = solicitation.getEntryMailbox();

		final AddressEntity solicitationAddress = addressToAddressEntityConverter
				.convert(solicitation.getSolicitationAddress());

		final List<DonnorEntity> donnors = solicitation //
				.getDonnors() //
				.stream() //
				.map(mapper -> donnorToDonnorEntityConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final List<TransfereeEntity> transferees = solicitation //
				.getTransferees() //
				.stream() //
				.map(mapper -> transfereeToTransfereeEntityConverter.convert(mapper)) //
				.collect(Collectors.toList());

		return SolicitationEntity //
				.builder() //
				.id(solicitation.getId()) //
				.solicitationStatus(solicitationStatusOptional //
						.map(mapper->mapper.name()) //
						.orElse(null)) //
				.protocolNumber(protocolNumber) //
				.channelReception(channelReception) //
				.comment(comment) //
				.digitalDocuments(digitalDocuments) //
				.donnors(donnors) //
				.entryMailbox(entryMailbox) //
				.userEntity(UserEntity //
						.builder() //
						.id( //
								solicitation //
										.getUser() //
										.getId())
						.build()) //
				.transferees(transferees) //
				.solicitationAddress(solicitationAddress) //
				.build();
	}
}
