package br.com.interfile.vivo.traass.rules;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.exception.UserNotAuthorizedException;

@Component
public class UserRule {

	public void verifyPasswordUserRequestWithUserDB(final User userRequest, final User userDB) {
		
		Assert.notNull(userRequest, "userRequest is null.");
		Assert.notNull(userDB, "userDB is null.");
		Assert.isTrue(userDB.getVerified(), "userDB.verified is true");
		Assert.notEmpty(userRequest.getDocuments(), "documents of userRequest not contains item.");
		Assert.notEmpty(userDB.getDocuments(), "documents of userDB not contains item.");

		final String documentValue = Optional.ofNullable(userRequest.getDocuments().get(0).getDocumentValue())
				.orElse(StringUtils.EMPTY);

		final Boolean documentEquals = documentValue.equals(userDB.getDocuments().get(0).getDocumentValue());

		final Boolean emailEquals = Optional //
				.ofNullable(userRequest.getEmail()) //
				.orElse(StringUtils.EMPTY) //
				.equals(userDB.getEmail());

		final Boolean passwordEquals = Optional.ofNullable(userRequest.getPassword()).orElse(StringUtils.EMPTY)
				.equals(userDB.getPassword());

		if (!((documentEquals || emailEquals) && passwordEquals)) {
			throw new UserNotAuthorizedException(userRequest);
		}
	}
}
