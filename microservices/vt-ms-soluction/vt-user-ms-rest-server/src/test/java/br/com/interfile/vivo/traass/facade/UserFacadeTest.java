package br.com.interfile.vivo.traass.facade;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.exception.UserExistException;
import br.com.interfile.vivo.traass.exception.UserNotExistException;
import br.com.interfile.vivo.traass.jpa.services.UserService;
import br.com.interfile.vivo.traass.rules.UserRule;

@Ignore
public class UserFacadeTest {

	@InjectMocks
	private UserFacade userFacade;

	@Mock
	private UserService userService;

	@Mock
	private UserRule userRule;

	@Test(expected = UserNotExistException.class)
	public void testAuthNOK() {
		final User user = User.builder().build();

		Mockito.when(userService //
				.findByDocumentValueOrEmail(user)).thenReturn(Optional.empty());

		userFacade.auth(user);
	}

	@Test
	public void testAuthOK() {
		final User user = User.builder().build();

		Mockito.when(userService //
				.findByDocumentValueOrEmail(user)).thenReturn(Optional.of(user));

		final User userAuth = userFacade.auth(user);
		Assert.assertEquals(user, userAuth);
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveUserNUll() {
		userFacade.saveUser(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveUserBlanck() {
		final User user = User.builder().build();
		userFacade.saveUser(user);
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveUserDocumetnsEmpty() {
		final User user = User.builder().documents(Collections.emptyList()).build();
		userFacade.saveUser(user);
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveUserDocumetItemBlank() {
		final User user = User.builder().documents(Arrays.asList(Document.builder().build())).build();
		userFacade.saveUser(user);
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveUserDocumetValueSpaces() {
		final User user = User //
				.builder() //
				.documents(Arrays //
						.asList(Document //
								.builder() //
								.documentValue(" ") //
								.build())) //
				.build(); //

		userFacade.saveUser(user);
	}

	@Test(expected = UserExistException.class)
	public void saveUserUserExistException() {
		final User user = User.builder() //
				.documents(Arrays //
						.asList( //
								Document //
										.builder() //
										.documentValue("DOCUMENT_VALUE") //
										.build())) //
				.build();

		Mockito.when(userService.findByDocumentValueOrEmail(user)).thenReturn(Optional.of(user));
		userFacade.saveUser(user);
	}

	@Test
	public void saveUserOK() {
		final User user = User.builder() //
				.documents(Arrays //
						.asList( //
								Document //
										.builder() //
										.documentValue("DOCUMENT_VALUE") //
										.build())) //
				.build();

		Mockito.when(userService.findByDocumentValueOrEmail(user)).thenReturn(Optional.empty());
		Mockito.when(userService.save(user)).thenReturn(user);

		final User userSaved = userFacade.saveUser(user);
		Assert.assertEquals(user, userSaved);
	}
}
