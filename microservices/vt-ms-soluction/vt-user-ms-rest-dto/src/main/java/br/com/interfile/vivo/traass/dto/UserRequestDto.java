package br.com.interfile.vivo.traass.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.interfile.vivo.traass.validation.CpfOrCnpj;
import br.com.interfile.vivo.traass.validation.EqualsValue;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder(toBuilder=true)
@Data
@ToString(exclude = { "password", "confirmPassword" })
public class UserRequestDto {

	@NotBlank(message = "expected valid value.")
	@Size(min = 5, max = 255)
	private final String name;

	@NotBlank(message = "expected Cpf, Cnpj, Rg, EletricBill, WaterBill, GasBill or TelephoneBill value.")
	@EqualsValue(values = { "Cpf", "Cnpj", "Rg", "EletricBill", "WaterBill", "GasBill",
	"TelephoneBill" })
	private final String documentType;

	@NotBlank(message = "expected valid value.")
	@Size(min = 11, max = 14)
	@CpfOrCnpj(message="expected valid value.")
	private final String documentValue;

	@NotBlank(message = "expected valid value.")
	@Email(message = "expected valid email")
	private final String email;

	@Valid
	private final List<PhoneDto> phones;

	@Valid
	private final List<AddressDto> addresses;

	@NotBlank(message = "expected valid value.")
	@Size(min = 4, max = 16)
	private final String password;

	@NotBlank(message = "expected valid value.")
	@Size(min = 4, max = 16)
	private final String confirmPassword;

	@JsonCreator
	public UserRequestDto( 
			@JsonProperty("name") final String name, 
			@JsonProperty("documentType") final String documentType, 
			@JsonProperty("documentValue") final String documentValue, 
			@JsonProperty("email") final String email, 
			@JsonProperty("phones") final List<PhoneDto> phones,
			@JsonProperty("addresses") final List<AddressDto> addresses,
			@JsonProperty("password") final String password, 
			@JsonProperty("confirmPassword") final String confirmPassword) {

		this.name = name;
		this.documentType = documentType;
		this.documentValue = documentValue;
		this.email = email;
		this.phones = phones;
		this.addresses = addresses;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
}
