package br.com.interfile.vivo.traass.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder=true)
@Data
public class CamundaIntegrationReport {
	private final String executionId;
	private final Integer total;
	private final Integer qtdSuccess;
	private final Integer qtdErrors;
	private final Integer qtdFail;
}
