package br.com.interfile.vivo.traass.dto;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class CamundaIntegrationResponseDto {

	private final String id;
	private final SolicitationResponseDto solicitation;
	private final String definitionId;
	private final String businessKey;
	private final String caseInstanceId;
	private final Boolean ended;
	private final Boolean suspended;
	private final String tenantId;
	private final String link;
	private final String executionId;
}
