package br.com.interfile.vivo.traass.integration;

import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.interfile.vivo.traass.configuration.UserRestConfiguration;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.facade.UserFacade;
import br.com.interfile.vivo.traass.jpa.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserRestConfiguration.class)
@Ignore
public class SendEmailTest {

	private static final String EMAIL = "felipe.cavalieri@gingaone.com";

	@Autowired
	private UserFacade userFacade;

	@MockBean
	private UserService userService;

	@Test
	public void test() {
		final User user = User //
				.builder() //
				.email(EMAIL) //
				.build();

		Mockito //
				.when(userService //
						.findByDocumentValueOrEmail(user)) //
				.thenReturn(Optional //
						.of(user //
								.toBuilder() //
								.password("senha") //
								.build()));

		userFacade.sendEmailRecoveryPassword(EMAIL);
	}
}
