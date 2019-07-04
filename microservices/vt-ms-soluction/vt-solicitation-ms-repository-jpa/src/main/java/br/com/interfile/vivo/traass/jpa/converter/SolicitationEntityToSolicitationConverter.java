package br.com.interfile.vivo.traass.jpa.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.CamundaIntegration;
import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.SolicitationStatus;
import br.com.interfile.vivo.traass.domain.Transferee;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.jpa.entities.SolicitationEntity;

@Component
public class SolicitationEntityToSolicitationConverter implements Converter<SolicitationEntity, Solicitation> {

	@Autowired
	private AddressEntityToAddressConverter addressEntityToAddressConverter;

	@Autowired
	private DigitalDocumentEntityToDigitalDocumentConverter digitalDocumentEntityToDigitalDocumentConverter;

	@Autowired
	private CamundaIntegrationEntityToCamundaIntegrationConverter camundaIntegrationEntityToCamundaIntegrationConverter;

	@Autowired
	private DonnorEntityToDonnorConverter donnorEntityToDonnorConverter;

	@Autowired
	private TransfereeEntityToTransfereeConverter transfereeEntityToTransfereeConverter;

	@Override
	public Solicitation convert(final SolicitationEntity solicitationEntity) {

		Assert.notNull(solicitationEntity, "solicitationEntity is not null.");
		Assert.notNull(solicitationEntity.getUserEntity().getId(), "solicitationEntity.user is not null.");
		Assert.notNull(solicitationEntity.getSolicitationStatus(),
				"solicitationEntity.solicitationStatus is not null.");
		Assert.notNull(solicitationEntity.getDigitalDocuments(), "solicitationEntity.digitalDocuments is not null.");
		Assert.notNull(solicitationEntity.getDonnors(), "solicitationEntity.donnors is not null.");
		Assert.notNull(solicitationEntity.getTransferees(), "solicitationEntity.Transferees is not null.");
		Assert.notNull(solicitationEntity.getSolicitationAddress(),
				"solicitationEntity.solicitationAddress is not null.");

		final String protocolNumber = solicitationEntity.getProtocolNumber();
		final String channelReception = solicitationEntity.getChannelReception();
		final String comment = solicitationEntity.getComment();
		final SolicitationStatus solicitationStatus = SolicitationStatus
				.create(solicitationEntity.getSolicitationStatus());

		final List<DigitalDocument> digitalDocuments = solicitationEntity //
				.getDigitalDocuments() //
				.stream() //
				.map(mapper -> digitalDocumentEntityToDigitalDocumentConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final String entryMailbox = solicitationEntity.getEntryMailbox();
		final Long userId = solicitationEntity.getUserEntity().getId();

		final Address solicitationAddress = addressEntityToAddressConverter
				.convert(solicitationEntity.getSolicitationAddress());

		final CamundaIntegration camundaIntegration = Optional //
				.ofNullable(solicitationEntity.getCamundaIntegration()) //
				.map(mapper -> camundaIntegrationEntityToCamundaIntegrationConverter //
						.convert(solicitationEntity.getCamundaIntegration())) //
				.orElse(null);

		final List<Donnor> donnors = solicitationEntity //
				.getDonnors() //
				.stream() //
				.map(mapper -> donnorEntityToDonnorConverter.convert(mapper)) //
				.collect(Collectors.toList());

		final List<Transferee> transferees = solicitationEntity //
				.getTransferees() //
				.stream() //
				.map(mapper -> transfereeEntityToTransfereeConverter.convert(mapper)) //
				.collect(Collectors.toList());

		return Solicitation //
				.builder() //
				.id(solicitationEntity.getId()) //
				.protocolNumber(protocolNumber) //
				.channelReception(channelReception) //
				.comment(comment) //
				.digitalDocuments(digitalDocuments) //
				.donnors(donnors) //
				.entryMailbox(entryMailbox) //
				.user(User.builder().id(userId).build()) //
				.transferees(transferees) //
				.solicitationAddress(solicitationAddress) //
				.solicitationStatus(solicitationStatus) //
				.camundaIntegration(camundaIntegration) //
				.creationDate(solicitationEntity.getCreateDate()) //
				.alterDate(solicitationEntity.getAlterDate()) //
				.build();
	}
}
