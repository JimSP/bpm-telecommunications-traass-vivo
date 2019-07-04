package br.com.interfile.vivo.traass.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.interfile.vivo.traass.validation.EqualsValue;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class SolicitationRequestDto {
	
	private final String protocolNumber;

	@EqualsValue(values = { "Open", "Pending", "InConfiguration", "InAnalysis", "WithProblem", "Close" })
	private final String solicitationStatus;

	@NotBlank(message = "expected valid value.")
	private final String channelReception;

	@NotBlank(message = "expected valid value.")
	private final String entryMailbox;

	@NotEmpty
	private final List<DonnorDto> donnors;
	
	@NotEmpty
	private final List<TransfereeDto> transferees;

	@Valid
	@NotNull(message = "expected valid value.")
	private final AddressDto solicitationAddress;

	private final String comment;

	@NotNull(message = "expected valid value.")
	@Min(1)
	private final Long userId;

	private final List<DigitalDocumentDto> digitalDocuments;

	@JsonCreator
	public SolicitationRequestDto( //
			@JsonProperty("protocolNumber") final String protocolNumber,
			@JsonProperty("solicitationStatus") final String solicitationStatus,
			@JsonProperty("channelReception") final String channelReception, //
			@JsonProperty("entryMailbox") final String entryMailbox, //
			@JsonProperty("donnors") final List<DonnorDto> donnors, //
			@JsonProperty("transferees") final List<TransfereeDto> transferees, //
			@JsonProperty("solicitationAddress") final AddressDto solicitationAddress, //
			@JsonProperty("comment") final String comment, //
			@JsonProperty("userId") final Long userId, //
			@JsonProperty("digitalDocuments") final List<DigitalDocumentDto> digitalDocuments) {
		this.protocolNumber = protocolNumber;
		this.solicitationStatus = solicitationStatus;
		this.channelReception = channelReception;
		this.entryMailbox = entryMailbox;
		this.donnors = donnors;
		this.transferees = transferees;
		this.solicitationAddress = solicitationAddress;
		this.comment = comment;
		this.userId = userId;
		this.digitalDocuments = digitalDocuments;
	}
}
