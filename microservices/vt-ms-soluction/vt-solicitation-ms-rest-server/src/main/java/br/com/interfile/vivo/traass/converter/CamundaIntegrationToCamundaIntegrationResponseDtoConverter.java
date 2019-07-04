package br.com.interfile.vivo.traass.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.CamundaIntegration;
import br.com.interfile.vivo.traass.dto.CamundaIntegrationResponseDto;

@Component
public class CamundaIntegrationToCamundaIntegrationResponseDtoConverter
		implements Converter<CamundaIntegration, CamundaIntegrationResponseDto> {

	@Autowired
	private SolicitationToSolicitationResponseDto solicitationToSolicitationResponseDto;

	@Override
	public CamundaIntegrationResponseDto convert(final CamundaIntegration camundaIntegration) {
		Assert.notNull(camundaIntegration, "camundaIntegration is not null.");

		return CamundaIntegrationResponseDto //
				.builder() //
				.businessKey(camundaIntegration.getBusinessKey()) //
				.caseInstanceId(camundaIntegration.getCaseInstanceId()) //
				.definitionId(camundaIntegration.getDefinitionId()) //
				.ended(camundaIntegration.getEnded()) //
				.executionId(camundaIntegration.getExecutionId()) //
				.id(camundaIntegration.getId()) //
				.solicitation(solicitationToSolicitationResponseDto.convert(camundaIntegration.getSolicitation())) //
				.suspended(camundaIntegration.getSuspended()) //
				.tenantId(camundaIntegration.getTenantId()) //
				.build();
	}
}
