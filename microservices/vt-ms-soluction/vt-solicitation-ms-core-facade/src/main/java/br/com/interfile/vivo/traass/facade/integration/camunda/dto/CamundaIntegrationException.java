package br.com.interfile.vivo.traass.facade.integration.camunda.dto;

import br.com.interfile.vivo.traass.domain.Solicitation;

public class CamundaIntegrationException extends RuntimeException {
	
	private static final long serialVersionUID = -2164962819072090015L;

	private final Solicitation solicitation;
	private final UriVariableDto uriVariableDto;

	public CamundaIntegrationException(final Solicitation solicitation, final UriVariableDto uriVariableDto, final Exception e) {
		super(e);
		this.solicitation = solicitation;
		this.uriVariableDto = uriVariableDto;
	}

	public Solicitation getSolicitation() {
		return solicitation;
	}

	public UriVariableDto getUriVariableDto() {
		return uriVariableDto;
	}
}
