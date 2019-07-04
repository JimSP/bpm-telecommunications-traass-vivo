package br.com.interfile.vivo.traass.exception;

public class SolicitationNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6118861260604517604L;

	private final Long id;

	public SolicitationNotFoundException(final Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
