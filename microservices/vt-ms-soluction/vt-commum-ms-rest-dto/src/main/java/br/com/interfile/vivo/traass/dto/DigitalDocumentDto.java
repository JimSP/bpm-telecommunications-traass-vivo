package br.com.interfile.vivo.traass.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.interfile.vivo.traass.validation.EqualsValue;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class DigitalDocumentDto {
	
	private final Long id;

	private final byte[] data;

	@NotBlank
	@Size(min = 1, max = 255)
	private final String referenceName;

	private final String url;

	@NotBlank(message = "expected Cpf, Cnpj, Rg, EletricBill, WaterBill, GasBill or TelephoneBill value.")
	@EqualsValue(values = { "Cpf", "Cnpj", "Rg", "EletricBill", "WaterBill", "GasBill", "TelephoneBill" })
	private final String documentType;

	@JsonCreator
	public DigitalDocumentDto( //
			@JsonProperty("id") final Long id,
			@JsonProperty("data") final byte[] data, //
			@JsonProperty("referenceName") final String referenceName, //
			@JsonProperty("url") final String url, //
			@JsonProperty("documentType") final String documentType) {
		this.id = id;
		this.data = data;
		this.referenceName = referenceName;
		this.url = url;
		this.documentType = documentType;
	}
}
