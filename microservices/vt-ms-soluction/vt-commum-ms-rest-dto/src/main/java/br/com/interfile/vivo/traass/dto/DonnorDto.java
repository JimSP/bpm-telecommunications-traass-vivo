package br.com.interfile.vivo.traass.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.interfile.vivo.traass.validation.EqualsValue;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class DonnorDto {
	
	private final Long id;

	@NotBlank(message = "expected valid value.")
	private final String donnorName;

	@NotBlank(message = "expected Cpf, Cnpj, Rg, EletricBill, WaterBill, GasBill or TelephoneBill value.")
	@EqualsValue(values = { "Cpf", "Cnpj", "Rg", "EletricBill", "WaterBill", "GasBill", "TelephoneBill" })
	private final String donnorDocumentType;

	@NotBlank(message = "expected valid value.")
	@Size(min = 11, max = 14)
	private final String donnorDocumentValue;

	@NotBlank(message = "expected valid value.")
	private final String donnorRg;

	@NotBlank(message = "expected valid value.")
	@Email(message = "expected valid email")
	@Size(min = 7, max = 255)
	private final String donnorEmail;

	@Valid
	@NotNull(message = "expected valid value.")
	private final PhoneDto donnorPhone;

	@Valid
	@NotNull(message = "expected valid value.")
	private final List<AddressDto> donnorAddresses;

	@JsonCreator
	public DonnorDto( //
			@JsonProperty("id") final Long id, //
			@JsonProperty("donnorName") final String donnorName, //
			@JsonProperty("donnorDocumentType") final String donnorDocumentType, //
			@JsonProperty("donnorDocumentValue") final String donnorDocumentValue, //
			@JsonProperty("donnorRg") final String donnorRg, //
			@JsonProperty("donnorEmail") final String donnorEmail, //
			@JsonProperty("donnorPhone") final PhoneDto donnorPhone, //
			@JsonProperty("donnorAddresses") final List<AddressDto> donnorAddresses) {
		this.id = id;
		this.donnorName = donnorName;
		this.donnorDocumentType = donnorDocumentType;
		this.donnorDocumentValue = donnorDocumentValue;
		this.donnorRg = donnorRg;
		this.donnorEmail = donnorEmail;
		this.donnorPhone = donnorPhone;
		this.donnorAddresses = donnorAddresses;
	}
}
