package br.com.interfile.vivo.traass.exception;

import org.springframework.web.client.ResourceAccessException;

public class HttpConnectionException extends RuntimeException{

	private static final long serialVersionUID = 2764617315191443240L;
	
	public HttpConnectionException(final ResourceAccessException e) {
		super(e);
	}
}
