package br.com.interfile.vivo.traass.exception;

import br.com.interfile.vivo.traass.domain.SolicitationStatus;

public class SolicitationStatusInvalidStateException extends RuntimeException {

	private final SolicitationStatus solicitationStatus;

	private static final long serialVersionUID = 3084621782220827238L;

	public SolicitationStatusInvalidStateException(final SolicitationStatus solicitationStatus) {
		this.solicitationStatus = solicitationStatus;
	}

	public SolicitationStatus getSolicitationStatus() {
		return solicitationStatus;
	}

}
