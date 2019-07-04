package br.com.interfile.vivo.traass.advice;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailPreparationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.interfile.vivo.traass.converter.UserToUserResponseDtoConverter;
import br.com.interfile.vivo.traass.dto.UserResponseDto;
import br.com.interfile.vivo.traass.exception.UserExistException;
import br.com.interfile.vivo.traass.exception.UserNotAuthorizedException;
import br.com.interfile.vivo.traass.exception.UserNotExistException;

@RestControllerAdvice
public class UserAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAdvice.class);

	@Autowired
	private UserToUserResponseDtoConverter userToUserResponseDtoConverter;

	@ExceptionHandler(UserExistException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public @ResponseBody UserResponseDto exceptionHandle(final UserExistException userExistException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, userExistException={}", userExistException);
		return userToUserResponseDtoConverter.convert(userExistException.getUser());
	}

	@ExceptionHandler(UserNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody UserResponseDto exceptionHandle(final UserNotExistException userNotExistException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, userNotExistException={}",
				userNotExistException);
		return userToUserResponseDtoConverter.convert(userNotExistException.getUser());
	}

	@ExceptionHandler(UserNotAuthorizedException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public @ResponseBody UserResponseDto exceptionHandle(final UserNotAuthorizedException userNotAuthorizedException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, userNotAuthorizedException={}",
				userNotAuthorizedException);
		return userToUserResponseDtoConverter.convert(userNotAuthorizedException.getUser());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> exceptionHandle(final IllegalArgumentException illegalArgumentException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, illegalArgumentException={}",
				illegalArgumentException);
		return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
	}

	@ExceptionHandler(MailPreparationException.class)
	public ResponseEntity<Object> exceptionHandle(final MailPreparationException mailPreparationException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, mailPreparationException={}",
				mailPreparationException);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("sistema indisponível no momento.");
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> exceptionHandle(final SQLException sqlException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, sqlException={}", sqlException);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("sistema indisponível no momento.");
	}

}
