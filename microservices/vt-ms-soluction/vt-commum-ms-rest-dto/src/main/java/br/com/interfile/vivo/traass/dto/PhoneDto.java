package br.com.interfile.vivo.traass.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.interfile.vivo.traass.validation.EqualsValue;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder=true)
@Data
public class PhoneDto {
	
	private final Long id;

	@NotBlank(message = "expected Home, Movel or Work value.")
	@EqualsValue(values = { "Home", "Mobile", "Work", "Contact" })
	private final String phoneType;

	@NotNull(message = "expected valid value.")
	@Min(1)
	@Max(99)
	private final Integer countryCode;

	@NotNull(message = "expected valid value.")
	@Min(1)
	@Max(99)
	private final Integer areaCode;

	@NotNull(message = "expected valid value.")
	@Min(1)
	@Max(99)
	private final Integer operatorCode;

	@NotNull(message = "expected valid value.")
	@Size(min = 5, max = 25)
	private final String phoneNumber;

	@JsonCreator
	public PhoneDto( //
			@JsonProperty("id") final Long id,
			@JsonProperty("phoneType") final String phoneType, //
			@JsonProperty("countryCode") final Integer countryCode, //
			@JsonProperty("areaCode") final Integer areaCode, //
			@JsonProperty("operatorCode") final Integer operatorCode, //
			@JsonProperty("phoneNumber") final String phoneNumber) {
		this.id = id;
		this.phoneType = phoneType;
		this.countryCode = countryCode;
		this.areaCode = areaCode;
		this.operatorCode = operatorCode;
		this.phoneNumber = phoneNumber;
	}
}
