package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.CamundaIntegrationReport;
import br.com.interfile.vivo.traass.jpa.entities.CamundaIntegrationReportEntity;

@Component
public class CamundaIntegrationReportToCamundaIntegrationReportEntityConverter
		implements Converter<CamundaIntegrationReport, CamundaIntegrationReportEntity> {

	@Override
	public CamundaIntegrationReportEntity convert(final CamundaIntegrationReport camundaIntegrationReport) {
		Assert.notNull(camundaIntegrationReport, "camundaIntegrationReport is not null.");

		return CamundaIntegrationReportEntity //
				.builder() //
				.executionId(camundaIntegrationReport.getExecutionId()) //
				.qtdSuccess(camundaIntegrationReport.getQtdSuccess()) //
				.qtdErrors(camundaIntegrationReport.getQtdErrors()) //
				.qtdFail(camundaIntegrationReport.getQtdFail()) //
				.total(camundaIntegrationReport.getTotal()) //
				.build();
	}
}
