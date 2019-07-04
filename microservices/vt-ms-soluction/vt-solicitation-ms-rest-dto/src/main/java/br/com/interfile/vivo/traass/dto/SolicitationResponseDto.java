package br.com.interfile.vivo.traass.dto;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder=true)
@Data
public class SolicitationResponseDto {

	private final Long id;
	private final String protocolNumber;
	private final String solicitationStatus;
	private final String channelReception;
	private final String entryMailbox;
	private final List<DonnorDto> donnors;
	private final List<TransfereeDto> transferees;
	private final AddressDto solicitationAddress;
	private final String comment;
	private final UserResponseDto userResponseDto;
	private final List<DigitalDocumentDto> digitalDocuments;
	private final Boolean isVisibleForEdit;
	private final Date creationDate;
	private final Date alterDate;

}
