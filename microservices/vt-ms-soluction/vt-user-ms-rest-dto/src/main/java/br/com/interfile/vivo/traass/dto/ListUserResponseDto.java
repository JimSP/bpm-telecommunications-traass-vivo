package br.com.interfile.vivo.traass.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder=true)
@Data
public class ListUserResponseDto {
	
	private final List<UserResponseDto> userResponseDtos;
	
	@JsonCreator
	public ListUserResponseDto(@JsonProperty("userResponseDtos") final List<UserResponseDto> userResponseDtos) {
		this.userResponseDtos = userResponseDtos;
	}
	
}
