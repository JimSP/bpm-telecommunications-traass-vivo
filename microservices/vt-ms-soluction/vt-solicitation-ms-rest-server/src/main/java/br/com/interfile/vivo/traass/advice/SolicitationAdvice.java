package br.com.interfile.vivo.traass.advice;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.interfile.vivo.traass.exception.SolicitationNotFoundException;
import br.com.interfile.vivo.traass.exception.SolicitationStatusInvalidStateException;

@RestControllerAdvice
public class SolicitationAdvice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolicitationAdvice.class);

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> exceptionHandle(final IllegalArgumentException illegalArgumentException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, illegalArgumentException={}", illegalArgumentException);
		return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
	}

	@ExceptionHandler(SolicitationNotFoundException.class)
	public ResponseEntity<Object> exceptionHandle(final SolicitationNotFoundException solicitationNotFoundException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, solicitationNotFoundException={}", solicitationNotFoundException);
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(SolicitationStatusInvalidStateException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> exceptionHandle(
			final SolicitationStatusInvalidStateException solicitationStatusInvalidStateException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, solicitationStatusInvalidStateException={}", solicitationStatusInvalidStateException);
		return ResponseEntity.badRequest().body(solicitationStatusInvalidStateException.getSolicitationStatus());
	}
	
	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> exceptionHandle(final SQLException sqlException) {
		LOGGER.error(MarkerFactory.getMarker("ADVICE"), "m=exceptionHandle, sqlException={}", sqlException);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("sistema indisponível no momento.");
	}
}
