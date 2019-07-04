package br.com.interfile.vivo.traass.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AlterStatusSolicitation {
	
	private final String comment;
	private final String operator;

}
