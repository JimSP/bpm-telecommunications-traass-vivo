package br.com.interfile.vivo.traass.dto;

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
public class AddressDto {
	
	private final Long id;

	@NotBlank(message = "expected valid value.")
	@EqualsValue(values = { "Home", "Comercial", "Delivery", "Correspondence" })
	private final String addressType;

	@NotBlank(message = "expected valid value.")
	@Size(min = 3, max = 50)
	private final String roadType;

	@NotBlank(message = "expected valid value.")
	@Size(min = 1, max = 255)
	private final String streetName;

	@NotNull(message = "expected valid value.")
	private final Integer streetNumber;

	@Size(max = 50)
	private final String complement;

	@NotBlank(message = "expected valid value.")
	@Size(min = 1, max = 255)
	private final String neighborhood;

	@NotBlank(message = "expected valid value.")
	@Size(min = 1, max = 255)
	private final String city;

	@NotBlank(message = "expected valid value.")
	@Size(min = 1, max = 50)
	private final String province;

	@NotBlank(message = "expected valid value.")
	@Size(min = 1, max = 50)
	private final String country;

	@NotBlank(message = "expected valid value.")
	@Size(min = 5, max = 9)
	private final String zipCode;

	@JsonCreator
	public AddressDto( //
			@JsonProperty("id") final Long id,
			@JsonProperty("addressType") final String addressType, //
			@JsonProperty("roadType") final String roadType, //
			@JsonProperty("streetName") final String streetName, //
			@JsonProperty("streetNumber") final Integer streetNumber, //
			@JsonProperty("complement") final String complement, //
			@JsonProperty("neighborhood") final String neighborhood, //
			@JsonProperty("city") final String city, //
			@JsonProperty("province") final String province, //
			@JsonProperty("country") final String country, //
			@JsonProperty("zipCode") final String zipCode) {
		this.id = id;
		this.addressType = addressType;
		this.roadType = roadType;
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.complement = complement;
		this.neighborhood = neighborhood;
		this.city = city;
		this.province = province;
		this.country = country;
		this.zipCode = zipCode;
	}
}
