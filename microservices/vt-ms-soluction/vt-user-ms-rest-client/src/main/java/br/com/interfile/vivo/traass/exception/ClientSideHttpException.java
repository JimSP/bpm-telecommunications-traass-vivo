package br.com.interfile.vivo.traass.exception;

import org.springframework.web.client.RestClientException;

public class ClientSideHttpException extends RuntimeException{

	private static final long serialVersionUID = -7680053604623379177L;
	
	public ClientSideHttpException(RestClientException e) {
		super(e);
	}
}
