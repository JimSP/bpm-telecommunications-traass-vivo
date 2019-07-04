package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.CamundaIntegrationReport;
import br.com.interfile.vivo.traass.jpa.entities.CamundaIntegrationReportEntity;

@Component
public class CamundaIntegrationReportEntityToCamundaIntegrationReportConverter
		implements Converter<CamundaIntegrationReportEntity, CamundaIntegrationReport> {

	@Override
	public CamundaIntegrationReport convert(final CamundaIntegrationReportEntity camundaIntegrationReportEntity) {

		Assert.notNull(camundaIntegrationReportEntity, "camundaIntegrationReportEntity is not null.");

		return CamundaIntegrationReport //
				.builder() //
				.executionId(camundaIntegrationReportEntity.getExecutionId()) //
				.qtdSuccess(camundaIntegrationReportEntity.getQtdSuccess()) //
				.qtdErrors(camundaIntegrationReportEntity.getQtdErrors()) //
				.qtdFail(camundaIntegrationReportEntity.getQtdFail()) //
				.total(camundaIntegrationReportEntity.getTotal()) //
				.build();
	}
}
