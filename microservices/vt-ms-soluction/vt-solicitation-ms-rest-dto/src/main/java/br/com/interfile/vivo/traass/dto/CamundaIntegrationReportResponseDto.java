package br.com.interfile.vivo.traass.dto;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class CamundaIntegrationReportResponseDto {

	private final String executionId;
	private final Integer total;
	private final Integer qtdSuccess;
	private final Integer qtdErrors;
	private final Integer qtdFail;

}
