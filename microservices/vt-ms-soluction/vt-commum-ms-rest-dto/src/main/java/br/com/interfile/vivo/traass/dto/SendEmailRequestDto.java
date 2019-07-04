package br.com.interfile.vivo.traass.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class SendEmailRequestDto {

	private final String subject;
	private final String message;

	@JsonCreator
	public SendEmailRequestDto(@JsonProperty("subject") final String subject, 
			@JsonProperty("message") final String message) {
		this.subject = subject;
		this.message = message;
	}
}
