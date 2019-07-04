package br.com.lcc.advice;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.lcc.dto.LucasResponseDto;

@RestControllerAdvice
public class Advice {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public @ResponseBody LucasResponseDto exceptionHandle(final RuntimeException runtimeException) {
		final Locale locale = LocaleContextHolder.getLocale();
		final String errorMessage = messageSource.getMessage("${prefixProject}.unauthorized", null, locale);

		return LucasResponseDto //
				.builder() //
				//.message(errorMessage) //
				.build();
	}
}
