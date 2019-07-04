package br.com.interfile.vivo.traass.rules;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.exception.UserNotAuthorizedException;

@RunWith(MockitoJUnitRunner.class)
public class UserRuleTest {

	@InjectMocks
	private UserRule userRule;

	@Test(expected = IllegalArgumentException.class)
	public void testRequestNull() {
		final User userRequest = null;
		final User userDB = null;
		userRule.verifyPasswordUserRequestWithUserDB(userRequest, userDB);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDbNull() {
		final User userDB = null;
		userRule.verifyPasswordUserRequestWithUserDB(User.builder().build(), userDB);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequestAndDBIsBlanck() {
		userRule.verifyPasswordUserRequestWithUserDB(User.builder().build(),
				User.builder().verified(Boolean.TRUE).build());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequestWithBlankDocumentItem() {
		userRule.verifyPasswordUserRequestWithUserDB(
				User.builder().documents(Arrays.asList(Document.builder().build())).build(),
				User.builder().verified(Boolean.TRUE).build());
	}

	@Test(expected = UserNotAuthorizedException.class)
	public void testRequestWithBlankDocumentItemAndDBWithBlankDOcumentItem() {
		userRule.verifyPasswordUserRequestWithUserDB(
				User.builder().documents(Arrays.asList(Document.builder().build())).build(),
				User.builder().verified(Boolean.TRUE).documents(Arrays.asList(Document.builder().build())).build());
	}

	@Test(expected = UserNotAuthorizedException.class)
	public void testRequestWithDocumentValue() {
		userRule.verifyPasswordUserRequestWithUserDB(
				User.builder().documents(Arrays.asList(Document.builder().documentValue("VALOR").build())).build(),
				User.builder().verified(Boolean.TRUE).documents(Arrays.asList(Document.builder().build())).build());
	}

	@Test(expected = UserNotAuthorizedException.class)
	public void testRequestWithDocumentValueEquals() {
		userRule.verifyPasswordUserRequestWithUserDB(
				User.builder().documents(Arrays.asList(Document.builder().documentValue("VALOR").build())).build(),
				User.builder().verified(Boolean.TRUE)
						.documents(Arrays.asList(Document.builder().documentValue("VALOR").build())).build());
	}

	@Test
	public void testOK() {
		userRule.verifyPasswordUserRequestWithUserDB(
				User.builder().documents(Arrays.asList(Document.builder().documentValue("VALOR").build()))
						.password("PASSWORD").build(),
				User.builder().documents(Arrays.asList(Document.builder().documentValue("VALOR").build()))
						.password("PASSWORD").verified(Boolean.TRUE).build());
	}
}
