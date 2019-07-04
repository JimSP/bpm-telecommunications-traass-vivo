package br.com.interfile.vivo.traass.jpa.converter;

import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.CamundaIntegration;
import br.com.interfile.vivo.traass.jpa.entities.CamundaIntegrationEntity;

@Component
public class CamundaIntegrationEntityToCamundaIntegrationConverter
		implements Converter<CamundaIntegrationEntity, CamundaIntegration> {

	@Override
	public CamundaIntegration convert(final CamundaIntegrationEntity camundaIntegrationEntity) {

		return CamundaIntegration //
				.builder() //
				.businessKey(camundaIntegrationEntity.getBusinessKey()) //
				.caseInstanceId(camundaIntegrationEntity.getCaseInstanceId()) //
				.definitionId(camundaIntegrationEntity.getDefinitionId()) //
				.ended(Boolean.parseBoolean(camundaIntegrationEntity.getEnded())) //
				.executionId(camundaIntegrationEntity.getExecutionId()) //
				.id(camundaIntegrationEntity.getId()) //
				.links(Arrays.asList(camundaIntegrationEntity.getLink())) //
				.suspended(Boolean.parseBoolean(camundaIntegrationEntity.getSuspended())) //
				.tenantId(camundaIntegrationEntity.getTenantId()) //
				.build();
	}
}
