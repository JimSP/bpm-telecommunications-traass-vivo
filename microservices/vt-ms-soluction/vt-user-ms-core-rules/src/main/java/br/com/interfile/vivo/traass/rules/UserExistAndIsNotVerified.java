package br.com.interfile.vivo.traass.rules;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.exception.UserExistException;

@Component
public class UserExistAndIsNotVerified {

	public User execute(final Optional<User> userOptional, final User user) {
		userOptional //
				.filter(predicate -> predicate.getVerified()) //
				.ifPresent(userDB -> {
					throw new UserExistException(user);
				});
		
		final User.UserBuilder builder = user //
				.toBuilder();

		userOptional //
				.ifPresent(consumer -> {
					builder.id(consumer.getId());
				});
		
		return builder.build();
	}
}
