package br.com.interfile.vivo.traass.rules;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.User;

@Component
public class UserTokenPackage {

	private static final String $ = "$";
	private static final String PAD_STR2 = "*";
	private static final String PAD_STR = "@";
	private static final String X = "X";

	@Autowired
	private StringEncryptor stringEncryptor;

	public String toPack(final User user) {
		return encript(formmat(user));
	}

	public User toUser(final String token, final Integer expireTime) {
		return parse(decrypt(token), expireTime);
	}

	private String encript(final String pack) {
		try {
			return URLEncoder.encode(stringEncryptor.encrypt(pack), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private String decrypt(final String pack) {
		return stringEncryptor.decrypt(pack);
	}

	private String formmat(final User user) {
		return StringUtils //
				.leftPad(String.valueOf(user.getId()), 20, X) //
				.concat(StringUtils.rightPad(user.getEmail(), 255, PAD_STR)) //
				.concat(StringUtils.rightPad(user.getDocuments().get(0).getDocumentValue(), 25, PAD_STR2)) //
				.concat(StringUtils.rightPad(user.getPassword(), 255, $)) //
				.concat(String.valueOf(System.currentTimeMillis()));
	}

	private User parse(final String pack, final Integer expireTime) {

		final Long id = Long.parseLong(StringUtils.remove(StringUtils.substring(pack, 0, 20), X));
		final String email = StringUtils.remove(StringUtils.substring(pack, 20, 275), PAD_STR);
		final String documentValue = StringUtils.remove(StringUtils.substring(pack, 275, 300), PAD_STR2);
		final String password = StringUtils.remove(StringUtils.substring(pack, 300, 555), $);
		final Long packedTimeMillis = Long.parseLong(StringUtils.substring(pack, 555));
		final Long currentTimeMillis = System.currentTimeMillis();

		Assert.isTrue(currentTimeMillis - packedTimeMillis <= expireTime * 1_000 * 60, "token expired.");

		return User //
				.builder() //
				.id(id) //
				.email(email) //
				.documents(Arrays //
						.asList(Document //
								.builder() //
								.documentValue(documentValue) //
								.build())) //
				.password(password) //
				.verified(Boolean.TRUE) //
				.build();
	}
}
