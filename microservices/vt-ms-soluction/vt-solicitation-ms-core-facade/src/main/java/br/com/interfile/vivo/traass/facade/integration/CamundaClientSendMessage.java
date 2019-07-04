package br.com.interfile.vivo.traass.facade.integration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.message.CorrelationMessageDto;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.interfile.vivo.traass.domain.CamundaIntegration;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.facade.integration.camunda.dto.CamundaUriVariableFactory;

@Component
public class CamundaClientSendMessage {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${interfile.vivo.camunda.process.url}")
	private String url;

	public void sendSolicitationsToCamundaBpm(final CamundaIntegration camundaIntegration,
			final Solicitation solicitation) {
		final String executionId = UUID.randomUUID().toString();
		try {
			final Map<String, VariableValueDto> processVariables = new HashMap<>();
			processVariables.put("TRASSSolicitation",
					CamundaUriVariableFactory.createVariableValueDto(solicitation.getId(), ValueType.LONG));
			processVariables.put("executionId",
					CamundaUriVariableFactory.createVariableValueDto(executionId, ValueType.STRING));

			final CorrelationMessageDto correlationMessageDto = new CorrelationMessageDto();
			correlationMessageDto.setMessageName("updateStatus");
			correlationMessageDto.setBusinessKey(camundaIntegration.getBusinessKey());
			correlationMessageDto.setWithoutTenantId(Boolean.FALSE);
			correlationMessageDto.setProcessVariables(processVariables);
			correlationMessageDto.setResultEnabled(Boolean.FALSE);
			correlationMessageDto.setTenantId(camundaIntegration.getTenantId());

			final Pair<String, Map<String, Object>> tuppla = CamundaUriVariableFactory.createMessage(url);
			restTemplate.postForLocation(tuppla.getFirst(), correlationMessageDto);
			System.out.println(new ObjectMapper().writeValueAsString(correlationMessageDto));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
