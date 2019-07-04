package br.com.interfile.vivo.traass.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.client.UserRestClient;
import br.com.interfile.vivo.traass.domain.AlterStatusSolicitation;
import br.com.interfile.vivo.traass.domain.CamundaIntegration;
import br.com.interfile.vivo.traass.domain.CamundaIntegrationReport;
import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.SolicitationStatus;
import br.com.interfile.vivo.traass.exception.SolicitationNotFoundException;
import br.com.interfile.vivo.traass.exception.SolicitationStatusInvalidStateException;
import br.com.interfile.vivo.traass.facade.integration.AzureStorageIntegration;
import br.com.interfile.vivo.traass.facade.integration.CamundaClientSendMessage;
import br.com.interfile.vivo.traass.jpa.services.SolicitationService;
import br.com.interfile.vivo.traass.rules.EnrichSolicitationRule;
import br.com.interfile.vivo.traass.rules.GenerateProtocolSolicitation;
import br.com.interfile.vivo.traass.rules.PossibleStatusOfSolicitation;

@Service
public class SolicitationFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(SolicitationFacade.class);

	@Autowired
	private SolicitationService service;

	@Autowired
	private EnrichSolicitationRule solicitationRule;

	@Autowired
	private PossibleStatusOfSolicitation possibleStatusOfSolicitation;

	@Autowired
	private GenerateProtocolSolicitation generateProtocolSolicitation;

	@Autowired
	private AzureStorageIntegration azureStorageIntegration;

	@Autowired
	private CamundaClientSendMessage camundaClientSendMessage;

	@Autowired
	private UserRestClient userRestClient;

	@Transactional
	public Solicitation openSolicitation(final Solicitation solicitation,
			final List<DigitalDocument> digitalDocuments) {
		Assert.notNull(solicitation, "solicitation is not null");
		Assert.isNull(solicitation.getSolicitationStatus(), "solicitation.statusSolicitation has open");

		final String protocolNumber = generateProtocolSolicitation.createProtocolNumber();
		final Solicitation solicitationEnrich = solicitationRule.enrichToOpen(solicitation, digitalDocuments,
				protocolNumber);
		final Solicitation solicitationOpen = service.save(solicitationEnrich);

		return solicitationOpen;
	}

	@Async
	public void updateByIdAndSendMessageCamunda(final Long id, final Solicitation solicitation) {
		updateById(id, solicitation);

		final Solicitation solicitationEdited = service.getById(id).get();

		if (SolicitationStatus.isPending(solicitationEdited.getSolicitationStatus())) {
			final Solicitation solicitationInAnalysis = status(id, SolicitationStatus.InAnalysis, //
					AlterStatusSolicitation //
							.builder() //
							.operator("portal") //
							.comment("Alteração da Solicitação.") //
							.build());

			sendMessageCamunda(id, solicitationInAnalysis);
		}
	}

	private void sendMessageCamunda(final Long id, final Solicitation solicitation) {
		camundaClientSendMessage //
				.sendSolicitationsToCamundaBpm(service.findSolicitaitonIntegretedsDetailBySolicitationId(id),
						solicitation);
	}

	@Transactional
	public void updateById(final Long id, final Solicitation solicitation) {
		Assert.notNull(id, "id is not null");
		Assert.notNull(solicitation, "solicitation is not null");
		Assert.isTrue(service //
				.getById(id) //
				.orElseThrow(() -> new SolicitationNotFoundException(id)) //
				.getSolicitationStatus() //
				.visibleForEdit(), "solicitation not editable.");

		final List<DigitalDocument> digitalDocumentsAzure = saveAzure(solicitation);
		final List<DigitalDocument> digitalDocuments = solicitation.getDigitalDocuments();

		digitalDocuments //
				.forEach(action -> {
					if (action.getData() == null) {
						digitalDocumentsAzure.add(action);
					}
				});

		service //
				.update(solicitation //
						.toBuilder() //
						.digitalDocuments(digitalDocumentsAzure) //
						.id(id) //
						.build());
	}

	public Solicitation openSolicitationAndSaveAzure(final Solicitation solicitation) {
		final List<DigitalDocument> digitalDocuments = saveAzure(solicitation);
		return openSolicitation(solicitation, digitalDocuments);
	}

	private List<DigitalDocument> saveAzure(final Solicitation solicitation) {
		return Optional //
				.ofNullable(solicitation //
						.getDigitalDocuments()) //
				.orElse(Collections.emptyList()) //
				.stream() //
				.filter(predicate -> predicate.getData() != null)
				.map(digitalDocument -> azureStorageIntegration.uploadData(digitalDocument)) //
				.collect(Collectors.toList());
	}

	public List<SolicitationStatus> status(final SolicitationStatus solicitationStatus) {
		return possibleStatusOfSolicitation.possibles(solicitationStatus);
	}

	public List<SolicitationStatus> status(final Long id) {
		final Solicitation solicitation = findById(id);

		Assert.notNull(solicitation.getSolicitationStatus(),
				"solicitationId=" + id + " has invalid SolicitationStatus.");

		return status(solicitation.getSolicitationStatus());
	}

	public Solicitation status(final Long id, final SolicitationStatus solicitationStatus,
			final AlterStatusSolicitation alterStatusSolicitation) {
		final List<SolicitationStatus> possiblesOfSolicitation = status(id);

		final SolicitationStatus nextSolicitationStatus = possiblesOfSolicitation //
				.stream() //
				.filter(predicate -> predicate.equals(solicitationStatus)) //
				.findFirst() //
				.orElseThrow(() -> new SolicitationStatusInvalidStateException(solicitationStatus));

		final Solicitation solicitation = findById(id);

		Optional //
				.ofNullable(solicitation) //
				.ifPresent(solicitationExist -> {
					service.logDataBaseMachineState(solicitationExist, alterStatusSolicitation);
				});

		final Solicitation alterSolicitation = solicitationRule.enrichAlter( //
				solicitation //
						.toBuilder() //
						.comment(alterStatusSolicitation.getComment()) //
						.build(),
				nextSolicitationStatus);
		final Solicitation solicitationEdited = updateStatus(alterSolicitation);

		notifyUser(alterSolicitation);

		return solicitationEdited;

	}

	private void notifyUser(final Solicitation solicitation) {
		try {
			userRestClient.sendNotification(solicitation.getUser().getId());
		} catch (Exception e) {
			LOGGER.error(MarkerFactory.getMarker("FACADE"), "m=notifyUser, solicitation={}", solicitation, e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Solicitation updateStatus(final Solicitation alterSolicitation) {
		return service.updateStatusSolicitation(alterSolicitation);
	}

	public Solicitation findById(final Long id) {
		return service.getById(id).orElseThrow(() -> new SolicitationNotFoundException(id));
	}

	public List<Solicitation> findSolicitationByIds(final List<Long> list) {
		return service.findSolicitationByIds(list);
	}

	public List<Solicitation> findByUserId(final Long userId) {
		return service.findByUserId(userId);
	}

	public List<Solicitation> findLogById(final Long id) {
		return service.findLogById(id);
	}

	public Page<CamundaIntegrationReport> findSolicitaitonIntegretedsWithBPM(final Pageable pageable) {
		return service.findSolicitaitonIntegretedsWithBPM(pageable);
	}

	public List<CamundaIntegration> findSolicitaitonIntegretedsDetails(final String executionId) {
		return service.findSolicitaitonIntegretedsDetails(executionId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDonnorAddress(final Long solicitationId, final Long addressId) {
		service.deleteDonnorAddress(solicitationId, addressId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTransfereeAddresses(final Long solicitationId, final Long addressId) {
		service.deleteTransfereeAddresses(solicitationId, addressId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDigitalDocument(final Long solicitationId, final Long digitalDocumentId) {
		service.deleteDigitalDocument(solicitationId, digitalDocumentId);
	}

	public List<Solicitation> findAberto(final Pair<Date, Date> intervalSearch) {
		return service.findLogByStatusArrayInterval(intervalSearch.getLeft(), intervalSearch.getRight(),
				SolicitationStatus.Open);
	}

	public List<Solicitation> findAprovados(final Pair<Date, Date> intervalSearch) {
		return service.findLogByStatusArrayInterval(intervalSearch.getLeft(), intervalSearch.getRight(),
				SolicitationStatus.Close);
	}

	public List<Solicitation> findReprovados(final Pair<Date, Date> intervalSearch) {
		return service.findLogByStatusArrayInterval(intervalSearch.getLeft(), intervalSearch.getRight(),
				SolicitationStatus.Pending);
	}

	public List<Solicitation> findPendentesVivo(final Pair<Date, Date> intervalSearch) {
		return service.findLogByStatusArrayInterval(intervalSearch.getLeft(), intervalSearch.getRight(),
				SolicitationStatus.InAnalysis, SolicitationStatus.InConfiguration, SolicitationStatus.WithProblem);
	}

	public void deleteDonnor(final Long solicitationId, final Long donnorId) {
		service.deleteDonnor(solicitationId, donnorId);
	}

	public void deleteTransferee(final Long solicitationId, final Long transfereeId) {
		service.deleteTransferee(solicitationId, transfereeId);
	}

	public List<Solicitation> mountStep(final Long id) {
		final List<Solicitation> solicitations = findLogById(id);
		final Solicitation solicitation = findById(id);
		final List<Solicitation> steps = new ArrayList<>();

		steps.add(solicitation.toBuilder().solicitationStatus(SolicitationStatus.Open).build());
		
		if(!solicitations.isEmpty()) {
			Solicitation aux = null;
			for (Solicitation item : solicitations) {
				if (aux != null) {
					steps.add(item.toBuilder().creationDate(aux.getCreationDate()).build());
				}
				aux = item;
			}
			steps.add(aux.toBuilder().solicitationStatus(solicitation.getSolicitationStatus()).build());
		}

		return steps;
	}
}
