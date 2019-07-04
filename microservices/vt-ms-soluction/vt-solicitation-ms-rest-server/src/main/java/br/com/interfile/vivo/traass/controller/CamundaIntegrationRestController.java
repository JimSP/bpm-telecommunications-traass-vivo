package br.com.interfile.vivo.traass.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.interfile.vivo.traass.converter.CamundaIntegrationToCamundaIntegrationReportResponseDtoConverter;
import br.com.interfile.vivo.traass.converter.CamundaIntegrationToCamundaIntegrationResponseDtoConverter;
import br.com.interfile.vivo.traass.dto.CamundaIntegrationReportResponseDto;
import br.com.interfile.vivo.traass.dto.CamundaIntegrationResponseDto;
import br.com.interfile.vivo.traass.facade.SolicitationFacade;

@RestController
public class CamundaIntegrationRestController {

	@Autowired
	private SolicitationFacade facade;

	@Autowired
	private CamundaIntegrationToCamundaIntegrationReportResponseDtoConverter camundaIntegrationToCamundaIntegrationReportResponseDto;

	@Autowired
	private CamundaIntegrationToCamundaIntegrationResponseDtoConverter camundaIntegrationToCamundaIntegrationResponseDto;

	@GetMapping(value = "/vt-solicitation-ms/bpm", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Page<CamundaIntegrationReportResponseDto> get(
			@RequestParam(name = "pageNumber", defaultValue = "1") final Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "720") final Integer pageSize) {

		return facade //
				.findSolicitaitonIntegretedsWithBPM( //
						PageRequest.of(pageNumber, pageSize)) //
				.map(mapper -> camundaIntegrationToCamundaIntegrationReportResponseDto //
						.convert(mapper));
	}

	@GetMapping(value = "/vt-solicitation-ms/bpm/{executionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody List<CamundaIntegrationResponseDto> get(
			@PathVariable(name = "executionId", required = true) final String executionId) {
		
		return facade //
				.findSolicitaitonIntegretedsDetails(executionId) //
				.stream() //
				.map(mapper -> camundaIntegrationToCamundaIntegrationResponseDto //
						.convert(mapper)) //
				.collect(Collectors.toList());
	}
}
