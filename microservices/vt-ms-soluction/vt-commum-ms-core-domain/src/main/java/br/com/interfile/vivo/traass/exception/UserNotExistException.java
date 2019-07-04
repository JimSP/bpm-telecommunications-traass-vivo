package br.com.interfile.vivo.traass.exception;

import br.com.interfile.vivo.traass.domain.User;
import lombok.Getter;

@Getter
public class UserNotExistException extends RuntimeException{

	private static final long serialVersionUID = -5888840787922800207L;
	private final User user;
	
	public UserNotExistException(final User user) {
		this.user = user;
	}
}
