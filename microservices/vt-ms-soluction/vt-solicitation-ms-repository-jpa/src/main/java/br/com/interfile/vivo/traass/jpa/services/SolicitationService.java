package br.com.interfile.vivo.traass.jpa.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.AlterStatusSolicitation;
import br.com.interfile.vivo.traass.domain.CamundaIntegration;
import br.com.interfile.vivo.traass.domain.CamundaIntegrationReport;
import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.SolicitationStatus;
import br.com.interfile.vivo.traass.jpa.converter.CamundaIntegrationEntityToCamundaIntegrationConverter;
import br.com.interfile.vivo.traass.jpa.converter.CamundaIntegrationReportEntityToCamundaIntegrationReportConverter;
import br.com.interfile.vivo.traass.jpa.converter.CamundaIntegrationReportToCamundaIntegrationReportEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.CamundaIntegrationToCamundaIntegrationEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.DigitalDocumentToDigitalDocumentEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.DonnorToDonnorEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.SolicitationEntityToSolicitationConverter;
import br.com.interfile.vivo.traass.jpa.converter.SolicitationLogEntityToSolicitationConverter;
import br.com.interfile.vivo.traass.jpa.converter.SolicitationToSolicitationEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.TransfereeToTransfereeEntityConverter;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;
import br.com.interfile.vivo.traass.jpa.entities.CamundaIntegrationReportEntity;
import br.com.interfile.vivo.traass.jpa.entities.DigitalDocumentEntity;
import br.com.interfile.vivo.traass.jpa.entities.DonnorEntity;
import br.com.interfile.vivo.traass.jpa.entities.PhoneEntity;
import br.com.interfile.vivo.traass.jpa.entities.SolicitationEntity;
import br.com.interfile.vivo.traass.jpa.entities.SolicitationLogEntity;
import br.com.interfile.vivo.traass.jpa.entities.TransfereeEntity;
import br.com.interfile.vivo.traass.jpa.entities.UserEntity;
import br.com.interfile.vivo.traass.jpa.repositories.CamundaIntegrationReportJpaRepository;
import br.com.interfile.vivo.traass.jpa.repositories.DigitalDocumentJpaRepository;
import br.com.interfile.vivo.traass.jpa.repositories.SolicitationJpaRepository;
import br.com.interfile.vivo.traass.jpa.repositories.SolicitationLogRepository;
import br.com.interfile.vivo.traass.jpa.repositories.UserJpaRepository;
import br.com.interfile.vivo.traass.jpa.service.CommumService;

@Component
public class SolicitationService {

	@Autowired
	private CommumService commumService;

	@Autowired
	private SolicitationJpaRepository jpaRepository;

	@Autowired
	private SolicitationLogRepository solicitationLogRepository;

	@Autowired
	private CamundaIntegrationReportJpaRepository camundaIntegrationReportJpaRepository;

	@Autowired
	private SolicitationToSolicitationEntityConverter solicitationToSolicitationEntityConverter;

	@Autowired
	private SolicitationEntityToSolicitationConverter solicitationEntityToSolicitationConverter;

	@Autowired
	private SolicitationLogEntityToSolicitationConverter solicitationLogEntityToSolicitationConverter;

	@Autowired
	private CamundaIntegrationReportToCamundaIntegrationReportEntityConverter camundaIntegrationReportToCamundaIntegrationReportEntityConverter;

	@Autowired
	private CamundaIntegrationEntityToCamundaIntegrationConverter camundaIntegrationEntityToCamundaIntegrationConverter;

	@Autowired
	private CamundaIntegrationReportEntityToCamundaIntegrationReportConverter camundaIntegrationReportEntityToCamundaIntegrationReportConverter;

	@Autowired
	private CamundaIntegrationToCamundaIntegrationEntityConverter camundaIntegrationToCamundaIntegrationEntityConverter;

	@Autowired
	private DigitalDocumentJpaRepository digitalDocumentJpaRepository;

	@Autowired
	private DigitalDocumentToDigitalDocumentEntityConverter digitalDocumentToDigitalDocumentEntityConverter;

	@Autowired
	private DonnorToDonnorEntityConverter donnorToDonnorEntityConverter;

	@Autowired
	private TransfereeToTransfereeEntityConverter transfereeToTransfereeEntityConverter;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Transactional(propagation = Propagation.REQUIRED)
	public Solicitation save(final Solicitation solicitation) {
		Assert.notNull(solicitation, "solicitation is null.");
		Assert.hasText(solicitation.getProtocolNumber(), "solicitation.protocolNumber is invalid.");

		final SolicitationEntity solicitationEntity = solicitationToSolicitationEntityConverter.convert(solicitation);
		final UserEntity userEntity = userJpaRepository.findById(solicitationEntity.getUserEntity().getId()).get();
		solicitationEntity.setUserEntity(userEntity);
		final SolicitationEntity solicitationEntityCreated = jpaRepository.save(solicitationEntity);

		return solicitationEntityToSolicitationConverter.convert(solicitationEntityCreated);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Solicitation update(final Solicitation solicitation) {
		Assert.hasText(solicitation.getProtocolNumber(), "solicitation.protocolNumber is invalid.");

		final SolicitationEntity solicitationEntity = solicitationToSolicitationEntityConverter.convert(solicitation);
		final SolicitationEntity solicitationEntiryDB = jpaRepository.findById(solicitation.getId()).get();

		mergeDigitalDocuments(solicitation, solicitationEntiryDB);
		mergeDonnors(solicitation, solicitationEntiryDB);
		mergeTransfeeres(solicitation, solicitationEntiryDB);

		if (solicitation.getCamundaIntegration() != null) {
			solicitationEntiryDB.setCamundaIntegration(camundaIntegrationToCamundaIntegrationEntityConverter
					.convert(solicitation.getCamundaIntegration()));
		}

		if (solicitationEntity.getSolicitationStatus() != null) {
			solicitationEntiryDB.setSolicitationStatus(solicitationEntity.getSolicitationStatus());
		}

		solicitationEntiryDB.setChannelReception(solicitationEntity.getChannelReception());
		solicitationEntiryDB.setComment(solicitationEntity.getComment());
		solicitationEntiryDB.setEntryMailbox(solicitationEntity.getEntryMailbox());
		solicitationEntiryDB.setProtocolNumber(solicitationEntity.getProtocolNumber());

		return solicitationEntityToSolicitationConverter.convert(jpaRepository.save(solicitationEntiryDB));
	}

	private void mergeTransfeeres(final Solicitation solicitation, final SolicitationEntity solicitationEntityDB) {
		final List<TransfereeEntity> transfereeEntitySaves = Optional //
				.ofNullable(solicitation //
						.getTransferees() //
						.stream() //
						.map(mapper -> transfereeToTransfereeEntityConverter.convert(mapper)) //
						.collect(Collectors.toList())) //
				.orElse(Collections.emptyList());

		final List<Long> exists = new ArrayList<>();
		solicitationEntityDB //
				.getTransferees() //
				.forEach(actionDB -> {
					transfereeEntitySaves //
							.forEach(action -> {
								if (actionDB.getId().equals(action.getId())) {
									actionDB.setTransfereeDocumentType(action.getTransfereeDocumentType());
									actionDB.setTransfereeDocumentValue(action.getTransfereeDocumentValue());
									actionDB.setTransfereeEmail(action.getTransfereeEmail());
									actionDB.setTransfereeName(action.getTransfereeName());
									mergeAddress(action.getTransfereeAddresses(), actionDB.getTransfereeAddresses());
									mergePhone(action.getTransfereePhone(), actionDB.getTransfereePhone());
									exists.add(actionDB.getId());
								}
							});
				});

		transfereeEntitySaves //
				.forEach(action -> {
					if (!exists.contains(action.getId())) {
						solicitationEntityDB.getTransferees().add(action);
					}
				});
	}

	private void mergeDonnors(final Solicitation solicitation, final SolicitationEntity solicitationEntityDB) {
		final List<DonnorEntity> donnorsSaves = Optional //
				.ofNullable(solicitation //
						.getDonnors() //
						.stream() //
						.map(mapper -> donnorToDonnorEntityConverter.convert(mapper)) //
						.collect(Collectors.toList())) //
				.orElse(Collections.emptyList());

		final List<Long> exists = new ArrayList<>();
		solicitationEntityDB //
				.getDonnors() //
				.forEach(actionDB -> {
					donnorsSaves //
							.forEach(action -> {
								if (actionDB.getId().equals(action.getId())) {
									actionDB.setDonnorDocumentType(action.getDonnorDocumentType());
									actionDB.setDonnorDocumentValue(action.getDonnorDocumentValue());
									actionDB.setDonnorEmail(action.getDonnorEmail());
									actionDB.setDonnorName(action.getDonnorName());
									actionDB.setDonnorRg(action.getDonnorRg());
									mergeAddress(action.getDonnorAddresses(), actionDB.getDonnorAddresses());
									mergePhone(action.getDonnorPhone(), actionDB.getDonnorPhone());
									exists.add(actionDB.getId());
								}
							});
				});

		donnorsSaves //
				.forEach(action -> {
					if (!exists.contains(action.getId())) {
						solicitationEntityDB.getDonnors().add(action);
					}
				});
	}
	
	private void mergePhone(final PhoneEntity phone, final PhoneEntity phoneEntity) {
		phoneEntity.setAreaCode(phone.getAreaCode());
		phoneEntity.setCountryCode(phone.getCountryCode());
		phoneEntity.setOperatorCode(phone.getOperatorCode());
		phoneEntity.setPhoneNumber(phone.getPhoneNumber());
		phoneEntity.setPhoneType(phone.getPhoneType());
	}

	private void mergeAddress(final List<AddressEntity> address, final List<AddressEntity> addressEntities) {

		final List<Long> exists = new ArrayList<>();

		addressEntities //
				.forEach(actionDB -> {
					address //
							.forEach(action -> {
								if (actionDB.equals(action)) {
									actionDB.setAddressType(action.getAddressType());
									actionDB.setCity(action.getCity());
									actionDB.setComplement(action.getComplement());
									actionDB.setCountry(action.getCountry());
									actionDB.setNeighborhood(action.getNeighborhood());
									actionDB.setProvince(action.getProvince());
									actionDB.setRoadType(action.getRoadType());
									actionDB.setStreetName(action.getStreetName());
									actionDB.setStreetNumber(action.getStreetNumber());
									actionDB.setZipCode(action.getZipCode());
									exists.add(actionDB.getId());
								}
							});
				});

		address //
				.forEach(action -> {
					if (!exists.contains(action.getId())) {
						addressEntities.add(action);
					}
				});
	}

	private void mergeDigitalDocuments(final Solicitation solicitation, final SolicitationEntity solicitationEntityDB) {
		final List<DigitalDocumentEntity> digitalDocumentEntitySaves = Optional //
				.ofNullable(solicitation //
						.getDigitalDocuments() //
						.stream() //
						.map(mapper -> digitalDocumentToDigitalDocumentEntityConverter.convert(mapper)) //
						.collect(Collectors.toList())) //
				.orElse(Collections.emptyList());

		final List<Long> exists = new ArrayList<>();
		solicitationEntityDB //
				.getDigitalDocuments() //
				.forEach(actionDB -> {
					digitalDocumentEntitySaves //
							.forEach(action -> {
								if (actionDB.getId().equals(action.getId())) {
									actionDB.setDocumentType(action.getDocumentType());
									actionDB.setReferenceName(action.getReferenceName());
									actionDB.setUrl(action.getUrl());
									exists.add(actionDB.getId());
								}
							});
				});

		digitalDocumentEntitySaves //
				.forEach(action -> {
					if (!exists.contains(action.getId())) {
						solicitationEntityDB.getDigitalDocuments().add(action);
					}
				});
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Solicitation updateStatusSolicitation(final Solicitation solicitation) {
		Assert.hasText(solicitation.getProtocolNumber(), "solicitation.protocolNumber is invalid.");
		final SolicitationEntity solicitationEntity = jpaRepository.findById(solicitation.getId()).get();
		solicitationEntity.setSolicitationStatus(solicitation.getSolicitationStatus().name());
		solicitationEntity.setComment(solicitation.getComment());
		return solicitationEntityToSolicitationConverter.convert(jpaRepository.save(solicitationEntity));
	}

	public void updateDigitalDocument(final DigitalDocument digitalDocument) {
		final DigitalDocumentEntity digitalDocumentEntity = digitalDocumentToDigitalDocumentEntityConverter
				.convert(digitalDocument);

		final DigitalDocumentEntity digitalDocumentEntityExist = digitalDocumentJpaRepository
				.findById(digitalDocument.getId()).get();

		digitalDocumentEntityExist.setDocumentType(digitalDocumentEntity.getDocumentType());
		digitalDocumentEntityExist.setReferenceName(digitalDocumentEntity.getReferenceName());
		digitalDocumentEntityExist.setUrl(digitalDocumentEntity.getUrl());
		digitalDocumentJpaRepository.save(digitalDocumentEntityExist);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void logDataBaseMachineState(final Solicitation solicitationOld,
			final AlterStatusSolicitation alterStatusSolicitation) {
		final SolicitationEntity solicitationEntity = solicitationToSolicitationEntityConverter
				.convert(solicitationOld);

		solicitationLogRepository //
				.save(SolicitationLogEntity //
						.builder() //
						.comment(alterStatusSolicitation.getComment()) //
						.operator(alterStatusSolicitation.getOperator()) //
						.channelReception(solicitationEntity.getChannelReception()) //
						.entryMailbox(solicitationEntity.getEntryMailbox()) //
						.protocolNumber(solicitationEntity.getProtocolNumber()) //
						.solicitationId(solicitationEntity.getId()) //
						.solicitationStatus(solicitationEntity.getSolicitationStatus()) //
						.userId(solicitationEntity.getUserEntity().getId()) //
						.build());
	}

	public Optional<Solicitation> getById(final Long id) {
		final Optional<SolicitationEntity> solicitationEntityOptional = jpaRepository.findById(id);
		return solicitationEntityOptional //
				.map(mapper -> solicitationEntityToSolicitationConverter.convert(mapper));
	}

	public List<Solicitation> findSolicitationByIds(final List<Long> list) {
		return jpaRepository //
				.findByIds(list) //
				.stream() //
				.map(mapper -> solicitationEntityToSolicitationConverter.convert(mapper)) //
				.collect(Collectors.toList());
	}

	public List<Solicitation> findByUserId(final Long userId) {
		return jpaRepository //
				.findByUserEntity_Id(userId) //
				.stream() //
				.map(mapper -> solicitationEntityToSolicitationConverter.convert(mapper)) //
				.collect(Collectors.toList());
	}

	public List<Solicitation> findLogById(final Long id) {
		return solicitationLogRepository //
				.findBySolicitationIdOrderById(id) //
				.stream() //
				.map(mapper -> solicitationLogEntityToSolicitationConverter.convert(mapper)) //
				.collect(Collectors.toList());
	}

	public List<Solicitation> findLogByStatusArrayInterval(final Date begin, final Date end,
			final SolicitationStatus... solicitationStatus) {

		final List<SolicitationLogEntity> solicitationEntities = solicitationLogRepository //
				.findByCreateDateBetweenAndSolicitationStatusIn( //
						begin, //
						end, //
						toString(solicitationStatus));

		final List<Solicitation> solicitations = solicitationEntities.stream() //
				.map(mapper -> solicitationLogEntityToSolicitationConverter.convert(mapper)) //
				.collect(Collectors.toList());

		solicitationEntities //
				.stream() //
				.distinct() //
				.forEach(action -> {
					solicitations.add( //
							solicitationEntityToSolicitationConverter //
									.convert(jpaRepository //
											.findById(action.getId()) //
											.get()));
				});

		return solicitations;
	}

	private String[] toString(final SolicitationStatus[] solicitationStatus) {
		final String[] type = new String[solicitationStatus.length];
		return Arrays //
				.asList(solicitationStatus) //
				.stream() //
				.map(mapper -> mapper.name()) //
				.collect(Collectors.toList()) //
				.toArray(type);
	}

	public List<Solicitation> findSolicitationNotIntegratedWithBpmn() {
		return jpaRepository.findByCamundaIntegrationIsNull() //
				.stream() //
				.map(solicitationEntity -> solicitationEntityToSolicitationConverter.convert(solicitationEntity))
				.collect(Collectors.toList());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(final CamundaIntegrationReport camundaIntegrationReport) {
		final CamundaIntegrationReportEntity camundaIntegrationReportEntity = camundaIntegrationReportToCamundaIntegrationReportEntityConverter
				.convert(camundaIntegrationReport);
		camundaIntegrationReportJpaRepository.save(camundaIntegrationReportEntity);
	}

	public CamundaIntegration findSolicitaitonIntegretedsDetailBySolicitationId(final Long id) {
		return jpaRepository.findByIdOrderByCamundaIntegration_CreateDate(id).stream() //
				.filter(predicate -> predicate.getCamundaIntegration() != null) //
				.reduce((a,
						b) -> a.getCamundaIntegration().getCreateDate().getTime() > b.getCamundaIntegration()
								.getCreateDate().getTime() ? a : b) //
				.map(solicitationEntity -> solicitationEntityToSolicitationConverter.convert(solicitationEntity)) //
				.get() //
				.getCamundaIntegration();
	}

	public Page<CamundaIntegrationReport> findSolicitaitonIntegretedsWithBPM(final Pageable pageable) {
		return camundaIntegrationReportJpaRepository //
				.findAll(pageable) //
				.map(mapper -> camundaIntegrationReportEntityToCamundaIntegrationReportConverter //
						.convert(mapper));
	}

	public List<CamundaIntegration> findSolicitaitonIntegretedsDetails(final String executionId) {
		return jpaRepository.findByCamundaIntegration_ExecutionId(executionId) //
				.stream() //
				.filter(predicate -> predicate.getCamundaIntegration() != null)
				.map(mapper -> camundaIntegrationEntityToCamundaIntegrationConverter //
						.convert(mapper.getCamundaIntegration())) //
				.collect(Collectors.toList());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDigitalDocument(final Long solicitationId, final Long digitalDocumentId) {
		digitalDocumentJpaRepository.deleteById(digitalDocumentId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTransfereeAddresses(final Long solicitationId, final Long addressId) {
		commumService.deleteAddress(addressId);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDonnorAddress(final Long solicitationId, final Long addressId) {
		commumService.deleteAddress(addressId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDonnor(final Long solicitationId, final Long donnorId) {
		commumService.deleteDonnor(solicitationId, donnorId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTransferee(final Long solicitationId, final Long transfereeId) {
		commumService.deleteTransferee(solicitationId, transfereeId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Solicitation updateCamumdaIntegration(final Solicitation solicitation) {
		final SolicitationEntity solicitationEntity = jpaRepository.findById(solicitation.getId()).get();
		solicitationEntity.setCamundaIntegration(
				camundaIntegrationToCamundaIntegrationEntityConverter.convert(solicitation.getCamundaIntegration()));
		return solicitationEntityToSolicitationConverter.convert(jpaRepository.save(solicitationEntity));
	}
}
