package br.com.interfile.vivo.traass.facade.integration.camunda.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.springframework.data.util.Pair;

public class CamundaUriVariableFactory {

	private static final String START_ID = "/process-definition/{id}/start";
	private static final String START_KEY = "/process-definition/key/{key}/start";
	private static final String START_KEY_TENANT_ID = "/process-definition/key/{key}/tenant-id/{tenant-id}/start";
	
	private static final String VARIABLE = "/message";

	public static Pair<String, Map<String, Object>> createMessage(final String url) {
		return Pair.of(url + VARIABLE, Collections.emptyMap());
	}
	
	public static Pair<String, Map<String, Object>> createProccessDefinition(final String url, final UriVariableDto uriVariableDto) {
		final Map<String, Object> variable = new HashMap<>();
		final AtomicBoolean id = new AtomicBoolean(false);
		final AtomicBoolean key = new AtomicBoolean(false);
		final AtomicBoolean tenantId = new AtomicBoolean(false);
		
		Optional //
				.ofNullable(uriVariableDto.getId()) //
				.filter(predicate -> StringUtils.isNotEmpty(predicate)).ifPresent(consumer -> {
					variable.put("id", uriVariableDto.getId());
					id.set(Boolean.TRUE);
				});

		Optional //
				.ofNullable(uriVariableDto.getKey()) //
				.filter(predicate -> StringUtils.isNotEmpty(predicate)).ifPresent(consumer -> {
					variable.put("key", uriVariableDto.getKey());
					key.set(Boolean.TRUE);
				});

		Optional //
				.ofNullable(uriVariableDto.getTenantId()) //
				.filter(predicate -> StringUtils.isNotEmpty(predicate)).ifPresent(consumer -> {
					variable.put("tenant-id", uriVariableDto.getTenantId());
					tenantId.set(Boolean.TRUE);
				});

		final String fullPath = url + (key.get() && tenantId.get() ? START_KEY_TENANT_ID
				: key.get() ? START_KEY : id.get() ? START_ID : "");

		return Pair.of(fullPath, variable);
	}

	public static VariableValueDto createVariableValueDto(final Object value, final ValueType valueType) {
		final VariableValueDto variableValueDto = new VariableValueDto();
		variableValueDto.setValue(value);
		variableValueDto.setType(valueType.getName());
		return variableValueDto;
	}

}
