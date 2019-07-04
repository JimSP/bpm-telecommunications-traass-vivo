package br.com.interfile.vivo.traass.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder=true)
@Data
@AllArgsConstructor
public class UserResponseDto {
	
	private final Long id;
	private final String name;
	private final String documentType;
	private final String documentValue;
	private final String email;
	private final List<PhoneDto> phones;
	private final List<AddressDto> addresses;
	private final String password;
}
