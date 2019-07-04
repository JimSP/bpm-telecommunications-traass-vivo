package br.com.interfile.vivo.traass.exception;

import br.com.interfile.vivo.traass.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserExistException extends RuntimeException {

	private static final long serialVersionUID = 7160388490494676614L;
	private final User user;

}
