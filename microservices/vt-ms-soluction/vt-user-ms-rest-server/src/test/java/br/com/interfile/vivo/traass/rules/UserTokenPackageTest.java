package br.com.interfile.vivo.traass.rules;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.interfile.vivo.traass.configuration.UserRestConfiguration;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserRestConfiguration.class)
@Ignore
public class UserTokenPackageTest {

	private static final User USER = User //
			.builder() //
			.id(1L) //
			.password("TESTE")
			.email("ALEXANDRE.MSL@GMAIL.COM") //
			.documents(Arrays
					.asList( //
					Document //
							.builder() //
							.documentValue("30553271873") //
							.build())) //
			.build();
	@Autowired
	private UserTokenPackage userTokenPackage;

	@Test
	public void test() throws UnsupportedEncodingException {
		final String token = userTokenPackage //
				.toPack(USER);

		final User user = userTokenPackage.toUser(URLDecoder.decode(token, "UTF-8"), 120);
		System.out.println(user);
	}
}
