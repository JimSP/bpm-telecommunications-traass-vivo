package br.com.interfile.vivo.traass.rules;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.User;

@Component
public class UserMergeRules {

	public User merge(final User user, final User userDB) {
		return User //
				.builder() //
				.addresses(getValueOrElse(() -> user.getAddresses(), () -> userDB.getAddresses())) //
				.documents(getValueOrElse(() -> user.getDocuments(), () -> userDB.getDocuments())) //
				.email(getValueOrElse(() -> user.getEmail(), () -> userDB.getEmail())) //
				.id(getValueOrElse(() -> user.getId(), () -> userDB.getId())) //
				.name(getValueOrElse(() -> user.getName(), () -> userDB.getName())) //
				.password(getValueOrElse(() -> user.getPassword(), () -> userDB.getPassword())) //
				.verified(getValueOrElse(() -> user.getVerified(),
						() -> (userDB.getVerified() == Boolean.FALSE ? null : Boolean.TRUE))) //
				.build();
	}
	
	private <T> T getValueOrElse(final Supplier<T> ifValue, final Supplier<T> orElseValue) {
		return Optional.ofNullable(ifValue.get()).orElse(orElseValue.get());
	}
}
