package br.com.interfile.vivo.traass.converter;

import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.dto.AuthUserRequestDto;

@Component
public class AuthUserRequestDtoToUserConverter implements Converter<AuthUserRequestDto, User> {

	@Override
	public User convert(final AuthUserRequestDto source) {
		return User //
				.builder() //
				.documents( //
						Arrays //
								.asList( //
										Document //
												.builder() //
												.documentValue( //
														source //
																.getDocumentValue()) //
												.build())) //
				.email(source.getEmail()) //
				.password(source.getPassword()) //
				.build();
	}
}
