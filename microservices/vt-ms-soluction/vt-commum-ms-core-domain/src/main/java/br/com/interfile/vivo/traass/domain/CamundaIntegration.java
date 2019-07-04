package br.com.interfile.vivo.traass.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class CamundaIntegration {

	private final String id;
	private final Solicitation solicitation;
	private final String definitionId;
	private final String businessKey;
	private final String caseInstanceId;
	private final Boolean ended;
	private final Boolean suspended;
	private final String tenantId;
	private final List<String> links;
	private final String executionId;

}
