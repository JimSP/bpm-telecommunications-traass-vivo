package br.com.interfile.vivo.traass.jpa.converter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.CamundaIntegration;
import br.com.interfile.vivo.traass.jpa.entities.CamundaIntegrationEntity;
import br.com.interfile.vivo.traass.jpa.entities.CamundaIntegrationEntity.CamundaIntegrationEntityBuilder;

@Component
public class CamundaIntegrationToCamundaIntegrationEntityConverter
		implements Converter<CamundaIntegration, CamundaIntegrationEntity> {

	@Override
	public CamundaIntegrationEntity convert(final CamundaIntegration camundaIntegration) {

		if (camundaIntegration == null) {
			return null;
		}

		final CamundaIntegrationEntityBuilder builder = CamundaIntegrationEntity //
				.builder();

		final List<String> links = Optional.ofNullable(camundaIntegration.getLinks()).orElse(Collections.emptyList());

		if (!links.isEmpty()) {
			builder.link(links.get(0));
		}

		return builder//
				.businessKey(camundaIntegration.getBusinessKey()) //
				.definitionId(camundaIntegration.getDefinitionId()) //
				.ended(String.valueOf(camundaIntegration.getEnded())) //
				.id(camundaIntegration.getId()) //
				.suspended(String.valueOf(camundaIntegration.getSuspended())) //
				.tenantId(camundaIntegration.getTenantId()) //
				.executionId(camundaIntegration.getExecutionId()) //
				.build();
	}
}
