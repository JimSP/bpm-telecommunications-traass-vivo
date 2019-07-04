package br.com.interfile.vivo.traass.facade.integration.camunda.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UriVariableDto {

	private final String id;
	private final String key;
	private final String tenantId;
}
