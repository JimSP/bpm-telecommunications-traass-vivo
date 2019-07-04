package br.com.interfile.vivo.traass.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder=true)
@Data
@ToString(exclude = { "password" })
public class AuthUserRequestDto {

	@Size(min = 5, max = 255)
	private final String documentValue;
	
	@Email
	private final String email;

	@NotBlank(message = "expected valid value.")
	@Size(min = 4, max = 16)
	private final String password;

	@JsonCreator
	public AuthUserRequestDto( //
			@JsonProperty("name") final String documentValue, //
			@JsonProperty("email") final String email,
			@JsonProperty("password") final String password) {
		this.documentValue = documentValue;
		this.email = email;
		this.password = password;
	}
}
