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
public class TransfereeDto {
	
	private final Long id;

	@NotBlank(message = "expected valid value.")
	private final String transfereeName;

	@NotBlank(message = "expected Cpf, Cnpj, Rg, EletricBill, WaterBill, GasBill or TelephoneBill value.")
	@EqualsValue(values = { "Cpf", "Cnpj", "Rg", "EletricBill", "WaterBill", "GasBill", "TelephoneBill" })
	private final String transfereeDocumentType;

	@NotBlank(message = "expected valid value.")
	@Size(min = 11, max = 14)
	private final String transfereeDocumentValue;

	@Email(message = "expected valid email")
	@Size(min = 7, max = 255)
	private final String transfereeEmail;

	@Valid
	@NotNull(message = "expected valid value.")
	private final PhoneDto transfereePhone;

	@Valid
	@NotNull(message = "expected valid value.")
	private final List<AddressDto> transfereeAddresses;

	@JsonCreator
	public TransfereeDto( //
			@JsonProperty("id") final Long id, //
			@JsonProperty("transfereeName") final String transfereeName, //
			@JsonProperty("transfereeDocumentType") final String transfereeDocumentType, //
			@JsonProperty("transfereeDocumentValue") final String transfereeDocumentValue, //
			@JsonProperty("transfereeEmail") final String transfereeEmail, //
			@JsonProperty("transfereePhone") final PhoneDto transfereePhone, //
			@JsonProperty("transfereeAddresses") final List<AddressDto> transfereeAddresses) {
		this.id = id;
		this.transfereeName = transfereeName;
		this.transfereeDocumentType = transfereeDocumentType;
		this.transfereeDocumentValue = transfereeDocumentValue;
		this.transfereeEmail = transfereeEmail;
		this.transfereePhone = transfereePhone;
		this.transfereeAddresses = transfereeAddresses;
	}
}
