package br.com.interfile.vivo.traass.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder=true)
@Data
public class Solicitation {
	
	private final Long id;
	private final SolicitationStatus solicitationStatus;
	private final String protocolNumber;
	private final String channelReception;
	private final String entryMailbox;
	private final List<Donnor> donnors;
	private final List<Transferee> transferees;
	private final Address solicitationAddress;
	private final String comment;
	private final User user;
	private final List<DigitalDocument> digitalDocuments;
	private final CamundaIntegration camundaIntegration;
	private final List<Link> links;
	private final Date creationDate;
	private final Date alterDate;
}
