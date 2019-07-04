package br.com.interfile.vivo.traass.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.CamundaIntegrationReport;
import br.com.interfile.vivo.traass.dto.CamundaIntegrationReportResponseDto;

@Component
public class CamundaIntegrationToCamundaIntegrationReportResponseDtoConverter
		implements Converter<CamundaIntegrationReport, CamundaIntegrationReportResponseDto> {

	@Override
	public CamundaIntegrationReportResponseDto convert(final CamundaIntegrationReport camundaIntegrationReport) {
		Assert.notNull(camundaIntegrationReport, "CamundaIntegrationReport is not null.");

		return CamundaIntegrationReportResponseDto //
				.builder() //
				.executionId(camundaIntegrationReport.getExecutionId()) //
				.qtdSuccess(camundaIntegrationReport.getQtdSuccess()) //
				.qtdErrors(camundaIntegrationReport.getQtdErrors()) //
				.qtdFail(camundaIntegrationReport.getQtdFail()) //
				.total(camundaIntegrationReport.getTotal()) //
				.build();
	}
}
