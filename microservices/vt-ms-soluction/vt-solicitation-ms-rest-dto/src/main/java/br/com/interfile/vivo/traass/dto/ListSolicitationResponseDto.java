package br.com.interfile.vivo.traass.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder=true)
@Data
public class ListSolicitationResponseDto {
	
	private final List<SolicitationResponseDto> solicitationResponseDtos;

}
