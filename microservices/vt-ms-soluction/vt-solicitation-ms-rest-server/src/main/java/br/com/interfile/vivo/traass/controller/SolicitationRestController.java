package br.com.interfile.vivo.traass.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.interfile.vivo.traass.converter.SolicitationLogToSolicitationResponseDtoConverter;
import br.com.interfile.vivo.traass.converter.SolicitationRequestDtoToSolicitationConverter;
import br.com.interfile.vivo.traass.converter.SolicitationToSolicitationResponseDto;
import br.com.interfile.vivo.traass.converter.SolicitationToStepSolicitationResponseDto;
import br.com.interfile.vivo.traass.domain.AlterStatusSolicitation;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.SolicitationStatus;
import br.com.interfile.vivo.traass.dto.ListSolicitationResponseDto;
import br.com.interfile.vivo.traass.dto.SolicitationRequestDto;
import br.com.interfile.vivo.traass.dto.SolicitationResponseDto;
import br.com.interfile.vivo.traass.dto.StepSolicitationResponseDto;
import br.com.interfile.vivo.traass.facade.SolicitationFacade;

@RestController
public class SolicitationRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SolicitationRestController.class);

	@Autowired
	private SolicitationFacade facade;

	@Autowired
	private SolicitationRequestDtoToSolicitationConverter solicitationRequestDtoToSolicitationConverter;

	@Autowired
	private SolicitationLogToSolicitationResponseDtoConverter solicitationLogToSolicitationResponseDto;

	@Autowired
	private SolicitationToSolicitationResponseDto solicitationToSolicitationResponseDto;

	@Autowired
	private SolicitationToStepSolicitationResponseDto solicitationToStepSolicitationResponseDto;

	@GetMapping(value = "/vt-solicitation-ms/{id}/step", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<StepSolicitationResponseDto> findStepById(@PathVariable(name = "id", required = true) final Long id) {
		return facade //
				.mountStep(id) //
				.stream() //
				.map(mapper -> solicitationToStepSolicitationResponseDto.convert(mapper)) //
				.collect(Collectors.toList());
	}

	@GetMapping(value = "/vt-solicitation-ms/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody SolicitationResponseDto get(@PathVariable(name = "id", required = true) final Long id) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=get, id={}", id);

		final Solicitation solicitation = facade.findById(id);
		return solicitationToSolicitationResponseDto.convert(solicitation);
	}

	@GetMapping(value = "/vt-solicitation-ms/list/{ids}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ListSolicitationResponseDto get(
			@PathVariable(name = "ids", required = true) final String ids) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=get, ids={}", ids);

		final List<Long> list = Arrays //
				.asList(ids.split("[,]")) //
				.stream() //
				.map(mapper -> Long.valueOf(mapper)) //
				.collect(Collectors.toList());

		return ListSolicitationResponseDto //
				.builder() //
				.solicitationResponseDtos(facade //
						.findSolicitationByIds(list)//
						.stream() //
						.map(mapper -> solicitationToSolicitationResponseDto.convert(mapper)) //
						.collect(Collectors.toList())) //
				.build();
	}

	@GetMapping(value = "{userId}/vt-solicitation-ms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ListSolicitationResponseDto getByUserId(
			@PathVariable(name = "userId", required = true) final Long userId) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=get, userId={}", userId);
		return ListSolicitationResponseDto //
				.builder() //
				.solicitationResponseDtos(//
						facade.findByUserId(userId)//
								.stream() //
								.map(mapper -> solicitationToSolicitationResponseDto.convert(mapper)) //
								.collect(Collectors.toList())) //
				.build();
	}

	@GetMapping(value = "/vt-solicitation-ms/{id}/log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ListSolicitationResponseDto findLogById(
			@PathVariable(name = "id", required = true) final Long id) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=get, id={}", id);

		return ListSolicitationResponseDto //
				.builder() //
				.solicitationResponseDtos(//
						facade.findLogById(id)//
								.stream() //
								.map(mapper -> solicitationLogToSolicitationResponseDto.convert(mapper)) //
								.collect(Collectors.toList())) //
				.build();
	}

	@PostMapping(value = "/vt-solicitation-ms", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody SolicitationResponseDto post(@Valid @RequestBody final SolicitationRequestDto request) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=post, request={}", request);

		final Solicitation solicitation = solicitationRequestDtoToSolicitationConverter.convert(request);
		final Solicitation solicitationOpen = facade.openSolicitationAndSaveAzure(solicitation);
		return solicitationToSolicitationResponseDto.convert(solicitationOpen);
	}

	@PutMapping(value = "/vt-solicitation-ms/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void put(@PathVariable(name = "id", required = true) final Long id,
			@Valid @RequestBody final SolicitationRequestDto request) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=put, id={}, request={}", id, request);

		final Solicitation solicitation = solicitationRequestDtoToSolicitationConverter.convert(request);
		facade.updateByIdAndSendMessageCamunda(id, solicitation);
	}

	@GetMapping(value = "/vt-solicitation-ms/status", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> status() {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=status");
		return Arrays //
				.asList(SolicitationStatus.values()) //
				.stream() //
				.map(mapper -> mapper.name()) //
				.collect(Collectors.toList());
	}

	@GetMapping(value = "/vt-solicitation-ms/status/{solicitationStatus}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> status(
			@PathVariable(name = "solicitationStatus", required = true) final String solicitationStatus) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=status, solicitationStatus={}", solicitationStatus);
		return facade //
				.status(SolicitationStatus.create(solicitationStatus)) //
				.stream() //
				.map(mapper -> mapper.name()) //
				.collect(Collectors.toList());
	}

	@GetMapping(value = "/vt-solicitation-ms/{id}/status", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> status(@PathVariable(name = "id", required = true) final Long id) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=status, id={}", id);
		return facade //
				.status(id) //
				.stream() //
				.map(mapper -> mapper.name()) //
				.collect(Collectors.toList());
	}

	@PutMapping(value = "/vt-solicitation-ms/{id}/operator/{operator}/status/open")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void toOpen( //
			@PathVariable(name = "id", required = true) final Long id, //
			@PathVariable(name = "operator", required = true) final String operator, //
			@RequestBody final String comment) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=toOpen, id={}", id);
		facade.status(id, SolicitationStatus.Open, AlterStatusSolicitation //
				.builder() //
				.comment(comment) //
				.operator(operator) //
				.build());
	}

	@PutMapping(value = "/vt-solicitation-ms/{id}/operator/{operator}/status/pending")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void toPending( //
			@PathVariable(name = "id", required = true) final Long id, //
			@PathVariable(name = "operator", required = true) final String operator, //
			@RequestBody final String comment) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=toPending, id={}", id);
		facade.status(id, SolicitationStatus.Pending, AlterStatusSolicitation //
				.builder() //
				.comment(comment) //
				.operator(operator) //
				.build());
	}

	@PutMapping(value = "/vt-solicitation-ms/{id}/operator/{operator}/status/inAnalysis")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void toInAnalysis( //
			@PathVariable(name = "id", required = true) final Long id, //
			@PathVariable(name = "operator", required = true) final String operator, //
			@RequestBody final String comment) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=toInAnalysis, id={}", id);
		facade.status(id, SolicitationStatus.InAnalysis, AlterStatusSolicitation //
				.builder() //
				.comment(comment) //
				.operator(operator) //
				.build());
	}

	@PutMapping(value = "/vt-solicitation-ms/{id}/operator/{operator}/status/inConfiguration")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void toInConfiguration( //
			@PathVariable(name = "id", required = true) final Long id, //
			@PathVariable(name = "operator", required = true) final String operator, //
			@RequestBody final String comment) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=toInConfiguration, id={}", id);
		facade.status(id, SolicitationStatus.InConfiguration, AlterStatusSolicitation //
				.builder() //
				.comment(comment) //
				.operator(operator) //
				.build());
	}

	@PutMapping(value = "/vt-solicitation-ms/{id}/operator/{operator}/status/withProblem")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void toWithProblem( //
			@PathVariable(name = "id", required = true) final Long id, //
			@PathVariable(name = "operator", required = true) final String operator, //
			@RequestBody final String comment) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=toWithProblem, id={}", id);
		facade.status(id, SolicitationStatus.WithProblem, AlterStatusSolicitation //
				.builder() //
				.comment(comment) //
				.operator(operator) //
				.build());
	}

	@PutMapping(value = "/vt-solicitation-ms/{id}/operator/{operator}/status/close")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void toClose( //
			@PathVariable(name = "id", required = true) final Long id, //
			@PathVariable(name = "operator", required = true) final String operator, //
			@RequestBody final String comment) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=toClose, id={}", id);
		facade.status(id, SolicitationStatus.Close, AlterStatusSolicitation //
				.builder() //
				.comment(comment) //
				.operator(operator) //
				.build());
	}

	@DeleteMapping(value = "/vt-solicitation-ms/{solicitationId}/donnorAddress/{addressId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteDonnorAddress(@PathVariable(value = "solicitationId", required = true) final Long solicitationId,
			@PathVariable(value = "addressId", required = true) final Long addressId) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=deleteDonnorAddress, solicitationId={}, addressId={}",
				solicitationId, addressId);
		facade.deleteDonnorAddress(solicitationId, addressId);
	}

	@DeleteMapping(value = "/vt-solicitation-ms/{solicitationId}/transfereeAddress/{addressId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteTransfereeAddresses(
			@PathVariable(value = "solicitationId", required = true) final Long solicitationId,
			@PathVariable(value = "addressId", required = true) final Long addressId) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=deleteTransfereeAddresses, solicitationId={}, addressId={}",
				solicitationId, addressId);
		facade.deleteTransfereeAddresses(solicitationId, addressId);
	}

	@DeleteMapping(value = "/vt-solicitation-ms/{solicitationId}/digitalDocument/{digitalDocumentId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteDigitalDocument(
			@PathVariable(value = "solicitationId", required = true) final Long solicitationId,
			@PathVariable(value = "digitalDocumentId", required = true) final Long digitalDocumentId) {
		LOGGER.debug(MarkerFactory.getMarker("REST"),
				"m=deleteDigitalDocument, solicitationId={}, digitalDocumentId={}", solicitationId, digitalDocumentId);
		facade.deleteDigitalDocument(solicitationId, digitalDocumentId);
	}

	@DeleteMapping(value = "/vt-solicitation-ms/{solicitationId}/donnor/{donnorId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteDonnor(@PathVariable(value = "solicitationId", required = true) final Long solicitationId,
			@PathVariable(value = "donnorId", required = true) final Long donnorId) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=deleteDonnor, solicitationId={}, donnorId={}", solicitationId,
				donnorId);
		facade.deleteDonnor(solicitationId, donnorId);
	}

	@DeleteMapping(value = "/vt-solicitation-ms/{solicitationId}/transferee/{transfereeId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteTransferee(@PathVariable(value = "solicitationId", required = true) final Long solicitationId,
			@PathVariable(value = "transfereeId", required = true) final Long transfereeId) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=deleteTransferee, solicitationId={}, transfereeId={}",
				solicitationId, transfereeId);
		facade.deleteTransferee(solicitationId, transfereeId);
	}
}
