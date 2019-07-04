package br.com.interfile.vivo.traass.facade.integration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.StartProcessInstanceDto;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.interfile.vivo.traass.domain.CamundaIntegration;
import br.com.interfile.vivo.traass.domain.CamundaIntegrationReport;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.domain.IntegrationStatus;
import br.com.interfile.vivo.traass.domain.Link;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.facade.integration.camunda.dto.CamundaIntegrationException;
import br.com.interfile.vivo.traass.facade.integration.camunda.dto.CamundaUriVariableFactory;
import br.com.interfile.vivo.traass.facade.integration.camunda.dto.UriVariableDto;
import br.com.interfile.vivo.traass.jpa.services.SolicitationService;

@Component
public class CamundaRestClientStartInstance {

	private static final Logger LOGGER = LoggerFactory.getLogger(CamundaRestClientStartInstance.class);

	@Autowired
	private SolicitationService solicitationService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${interfile.vivo.camunda.process.id}")
	private String id;

	@Value("${interfile.vivo.camunda.process.key}")
	private String key;

	@Value("${interfile.vivo.camunda.process.tenantId}")
	private String tenantId;

	@Value("${interfile.vivo.camunda.process.url}")
	private String url;

	@Value("${interfile.vivo.camunda.process.active:true}")
	private Boolean integrationCamundaActive;

	@Async
	@Scheduled(fixedDelayString = "${interfile.vivo.camunda.process.fixedDelay}")
	public void sendSolicitationsToCamundaBpm() {

		if (!integrationCamundaActive) {
			return;
		}

		final String executionId = UUID.randomUUID().toString();

		LOGGER.info(MarkerFactory.getMarker("INTEGRATION"),
				"m=sendSolicitationsToCamundaBpm, executionId={}, msg=\"iniciando serviço de integração com Bpm.\"",
				executionId);

		try {
			final CamundaIntegrationReport result = solicitationService //
					.findSolicitationNotIntegratedWithBpmn() //
					.stream() //
					.map(solicitation -> proccess(UriVariableDto //
							.builder() //
							.id(id) //
							.key(key) //
							.tenantId(tenantId) //
							.build(), //
							solicitation, //
							executionId)) //
					.map(status -> CamundaIntegrationReport //
							.builder() //
							.executionId(executionId).qtdSuccess(IntegrationStatus.Success == status ? 1 : 0) //
							.qtdErrors(IntegrationStatus.Error == status ? 1 : 0) //
							.qtdFail(IntegrationStatus.Fail == status ? 1 : 0) //
							.total(1) //
							.build()) //
					.reduce(accumulator()) //
					.orElse(CamundaIntegrationReport //
							.builder() //
							.total(0) //
							.qtdSuccess(0) //
							.qtdErrors(0) //
							.qtdFail(0) //
							.executionId(executionId) //
							.build())
					.toBuilder() //
					.build();

			solicitationService.save(result);

			LOGGER.info(MarkerFactory.getMarker("INTEGRATION"),
					"m=sendSolicitationsToCamundaBpm, executionId={}, msg=\"integração concluida.\"", executionId);

		} catch (Exception e) {
			LOGGER.error(MarkerFactory.getMarker("INTEGRATION"),
					"m=sendSolicitationsToCamundaBpm, executionId={}, msg=\"termino inesperado do serviço de integração com Bpm.\"",
					executionId, e);
		}
	}

	private BinaryOperator<CamundaIntegrationReport> accumulator() {
		return (a, b) -> CamundaIntegrationReport //
				.builder() //
				.qtdSuccess(a.getQtdSuccess() + b.getQtdSuccess()) //
				.qtdErrors(a.getQtdErrors() + b.getQtdErrors()) //
				.qtdFail(a.getQtdFail() + b.getQtdFail()) //
				.total(a.getTotal() + b.getTotal()) //
				.executionId(a.getExecutionId()) //
				.build();
	}

	private IntegrationStatus proccess(final UriVariableDto uriVariableDto, final Solicitation solicitation,
			final String executionId) {
		LOGGER.debug(MarkerFactory.getMarker("INTEGRATION"),
				"m=sendSolicitationsToCamundaBpm, executionId={}, msg=\"solicitação preparada para integração\" uriVariableDto={}, solicitation={}",
				executionId, uriVariableDto, solicitation);

		try {
			final CamundaIntegration camundaIntegration = sendCamuda( //
					solicitation, //
					uriVariableDto, //
					CamundaIntegration //
							.builder() //
							.executionId(executionId), //
					executionId) //
							.build();

			LOGGER.debug(MarkerFactory.getMarker("INTEGRATION"),
					"m=sendSolicitationsToCamundaBpm, executionId={}, msg=\"solicitacao enviada\" uriVariableDto={}, camundaIntegration={}",
					executionId, uriVariableDto, camundaIntegration);

			final Solicitation solicitationSaved = updateSolicitation( //
					solicitation //
							.toBuilder() //
							.camundaIntegration(camundaIntegration) //
							.build());

			LOGGER.debug(MarkerFactory.getMarker("INTEGRATION"),
					"m=sendSolicitationsToCamundaBpm, executionId={}, msg=\"integração salva com sucesso\", uriVariableDto={}, solicitationSaved={}",
					executionId, uriVariableDto, solicitationSaved);

			return IntegrationStatus.Success;
		} catch (CamundaIntegrationException e) {
			LOGGER.error(MarkerFactory.getMarker("INTEGRATION"),
					"m=sendSolicitationsToCamundaBpm, executionId={}, msg=\"ocorreu uma falha durante a integração.\", uriVariableDto={}, solicitation={}",
					executionId, e.getUriVariableDto(), e.getSolicitation(), e);
			return IntegrationStatus.Fail;
		} catch (Throwable e) {
			LOGGER.error(MarkerFactory.getMarker("INTEGRATION"),
					"m=sendSolicitationsToCamundaBpm, executionId={}, msg=\"ocorreu um erro durante a integração.\"",
					executionId, e);
			return IntegrationStatus.Error;
		}
	}

	public CamundaIntegration.CamundaIntegrationBuilder sendCamuda(final Solicitation solicitation,
			final UriVariableDto uriVariableDto, final CamundaIntegration.CamundaIntegrationBuilder builder,
			final String executionId) {

		final ClientHttpRequestInterceptor clientHttpRequestInterceptor = (req, body, exec) -> {
			LOGGER.info("m=sendCamuda, msg=\"clientHttpRequestInterceptor\", body={}", new String(body));
			return exec.execute(req, body);
		};
		restTemplate.setInterceptors(Arrays.asList(clientHttpRequestInterceptor));

		try {

			final String solicitationDocumentType = containsCnpj(solicitation) ? DocumentType.Cnpj.name()
					: DocumentType.NotDefined.name();

			final Solicitation solicitationCamunda = solicitation.toBuilder().links(createLinks()).build();

			final Pair<String, Map<String, Object>> tuppla = CamundaUriVariableFactory.createProccessDefinition(url,
					uriVariableDto);

			final Map<String, VariableValueDto> variablePayLoad = new HashMap<>();
			variablePayLoad.put("TRASSSolicitation",
					CamundaUriVariableFactory.createVariableValueDto(solicitationCamunda.getId(), ValueType.LONG));

			variablePayLoad.put("DocumentType",
					CamundaUriVariableFactory.createVariableValueDto(solicitationDocumentType, ValueType.STRING));
			variablePayLoad.put("executionId",
					CamundaUriVariableFactory.createVariableValueDto(executionId, ValueType.STRING));

			final StartProcessInstanceDto startProcessInstanceDto = new StartProcessInstanceDto();
			startProcessInstanceDto.setBusinessKey(solicitation.getProtocolNumber());
			startProcessInstanceDto.setVariables(variablePayLoad);

			final ProcessInstanceDto processInstanceDto = restTemplate.postForEntity( //
					tuppla.getFirst(), //
					startProcessInstanceDto, ProcessInstanceDto.class, //
					tuppla.getSecond()) //
					.getBody();

			builder //
					.id(processInstanceDto.getId()) //
					.solicitation(solicitation) //
					.businessKey(processInstanceDto.getBusinessKey()) //
					.caseInstanceId(processInstanceDto.getCaseInstanceId()) //
					.definitionId(processInstanceDto.getDefinitionId()) //
					.ended(processInstanceDto.isEnded()) //
					.suspended(processInstanceDto.isSuspended()) //
					.tenantId(processInstanceDto.getTenantId());

			return builder;
		} catch (Exception e) {
			throw new CamundaIntegrationException(solicitation, uriVariableDto, e);
		}
	}

	private Boolean containsCnpj(final Solicitation solicitation) {

		final Supplier<Stream<Donnor>> supplierDonnor = () -> solicitation //
				.getDonnors() //
				.stream();

		final Predicate<Donnor> predicateDonnor = donnor -> DocumentType //
				.Cnpj //
						.equals(Optional //
								.ofNullable(donnor.getDocument()) //
								.orElse(Document //
										.builder() //
										.build()) //
								.getDocumentType());


		return countPredicateMath(supplierDonnor, predicateDonnor) > 0;
	}

	private <T> Long countPredicateMath(final Supplier<Stream<T>> supplier, final Predicate<T> predicate) {
		return supplier //
				.get() //
				.filter(predicate) //
				.count();
	}

	private List<Link> createLinks() {
		return Arrays //
				.asList( //
						Link //
								.builder() //
								.name("FIND") //
								.method("GET") //
								.path("/vt-solicitation-ms/{id}") //
								.build(),

						Link //
								.builder() //
								.name("LIST_STATUS") //
								.method("GET") //
								.path("/vt-solicitation-ms/status") //
								.build(),

						Link //
								.builder() //
								.name("STATES_OF_SOLICITATION") //
								.method("GET") //
								.path("vt-solicitation-ms/{id}/status") //
								.build(),

						Link //
								.builder() //
								.name("PENDING") //
								.method("PUT") //
								.path("/vt-solicitation-ms/{id}/status/pending") //
								.build(),

						Link //
								.builder() //
								.name("ANALYSIS") //
								.method("PUT") //
								.path("/vt-solicitation-ms/{id}/status/inAnalysis") //
								.build(),

						Link //
								.builder() //
								.name("CONFIGURATION") //
								.method("PUT") //
								.path("/vt-solicitation-ms/{id}/status/withProblem") //
								.build(),

						Link //
								.builder() //
								.name("WITH_PROBLEM") //
								.method("PUT") //
								.path("/vt-solicitation-ms/{id}/status/close") //
								.build());
	}

	public Solicitation updateSolicitation(final Solicitation solicitation) {
		return solicitationService //
				.updateCamumdaIntegration(solicitation);
	}
}
