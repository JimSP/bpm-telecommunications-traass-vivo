package br.com.interfile.vivo.traass.controller;

import java.util.Random;

import org.camunda.bpm.engine.rest.dto.message.CorrelationMessageDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.runtime.StartProcessInstanceDto;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CamundaMockRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CamundaMockRestController.class);

	@PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void message(@RequestBody final CorrelationMessageDto correlationMessageDto) {
		LOGGER.debug(MarkerFactory.getMarker("MOCK"), "m=post, correlationMessageDto={}", correlationMessageDto);
	}
	
	@PostMapping(value = "/process-definition/key/{key}/start", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ProcessInstanceDto startKey(@RequestBody final StartProcessInstanceDto startProcessInstanceDto) {
		LOGGER.debug(MarkerFactory.getMarker("MOCK"), "m=post, startProcessInstanceDto={}", startProcessInstanceDto);
		return createProcessInstanceDto();
	}
	
	@PostMapping(value = "/process-definition/{id}/start", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ProcessInstanceDto startId(@RequestBody final StartProcessInstanceDto startProcessInstanceDto) {
		LOGGER.debug(MarkerFactory.getMarker("MOCK"), "m=post, startProcessInstanceDto={}", startProcessInstanceDto);
		return createProcessInstanceDto();
	}
	
	@PostMapping(value = "process-definition/key/{key}/tenant-id/{tenant-id}/start", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody ProcessInstanceDto startKeyTenantId(@RequestBody final StartProcessInstanceDto startProcessInstanceDto) {
		LOGGER.debug(MarkerFactory.getMarker("MOCK"), "m=post, startProcessInstanceDto={}", startProcessInstanceDto);
		return createProcessInstanceDto();
	}

	private ProcessInstanceDto createProcessInstanceDto() {
		final ProcessInstance instance = new ProcessInstance() {
			
			private final Random random = new Random(System.currentTimeMillis());
			
			@Override
			public boolean isEnded() {
				return false;
			}
			
			@Override
			public String getTenantId() {
				return "TENANT-ID";
			}
			
			@Override
			public String getProcessInstanceId() {
				return "INSTANCE-ID:" + random.nextLong();
			}
			
			@Override
			public String getId() {
				return String.valueOf(random.nextLong());
			}
			
			@Override
			public boolean isSuspended() {
				return false;
			}
			
			@Override
			public String getProcessDefinitionId() {
				return "PROCESS-DEFINITION-ID";
			}
			
			@Override
			public String getCaseInstanceId() {
				return "CASE-INSTANCE-ID";
			}
			
			@Override
			public String getBusinessKey() {
				return "BUSINESS-KEY";
			}
		};
		return ProcessInstanceDto.fromProcessInstance(instance);
	}
}
