package br.com.interfile.vivo.traass.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder=true)
@Data
public class StepSolicitationResponseDto {
	
	private String comment;
	private Date stepDate;
	private String status;
	

}
