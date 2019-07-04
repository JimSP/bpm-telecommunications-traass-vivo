package br.com.interfile.vivo.traass.facade;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.exception.UserNotExistException;
import br.com.interfile.vivo.traass.integration.SendEmail;
import br.com.interfile.vivo.traass.integration.ViaCepIntegration;
import br.com.interfile.vivo.traass.jpa.services.UserService;
import br.com.interfile.vivo.traass.rules.UserExistAndIsNotVerified;
import br.com.interfile.vivo.traass.rules.UserMergeRules;
import br.com.interfile.vivo.traass.rules.UserRule;
import br.com.interfile.vivo.traass.rules.UserTokenPackage;

@Service
public class UserFacade {
	
	private static final String VERIFY_PATH = "/verified/{token}";

	private final static Logger LOGGER = LoggerFactory.getLogger(UserFacade.class);

	@Autowired
	private UserService service;

	@Autowired
	private SendEmail sendEmail;

	@Autowired
	private UserRule rule;

	@Autowired
	private UserTokenPackage userTokenPackage;

	@Autowired
	private UserMergeRules userMergeRules;

	@Autowired
	private UserExistAndIsNotVerified userExistAndIsNotVerified;

	@Autowired
	private ViaCepIntegration viaCepIntegration;

	@Value("${interfile.vivo.verify.url:http://localhost:8091}")
	private String veriryUrl;

	@Value("${interfile.vivo.verify.expireTokenMinutes:120}")
	private Integer expireTokenMinutes;

	public User saveUserAndSendVerifyEmail(final User user) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=saveUserAndSendVerifyEmail, user={}", user);

		final User userDB = saveUser(user);
		sendEmail.execute(user, "verify-email", "verificar e-mail.",
				veriryUrl + VERIFY_PATH.replace("{token}", userTokenPackage.toPack(userDB)));
		return userDB;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User saveUser(final User user) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=saveUser, user={}", user);

		Assert.isTrue(BooleanUtils.isFalse(user.getVerified()), "user.verified is false");
		Assert.notEmpty(user.getDocuments(), "user documents is empty");
		Assert.notNull(user.getDocuments().get(0), "user documents[0] is null");
		Assert.hasText(user.getDocuments().get(0).getDocumentValue(),
				"user documents[0].documentValue is not valid Text");

		final Optional<User> userOptional = service //
				.findByDocumentValueOrEmail(user);

		final User userNotVerifiedOrNotExist = userExistAndIsNotVerified.execute(userOptional, user);
		return service.save(userNotVerifiedOrNotExist);
	}

	public Supplier<Stream<User>> findUser(final User user) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=findUser, user={}", user);
		return service.findByExample(user);
	}

	@Async
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateById(final Long id, final User user) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=updateById, id={}, user={}", id, user);
		service.update(user.toBuilder().id(id).build());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final Long id) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=delete, id={}", id);
		service.delete(id);
	}

	public User findUserById(final Long id) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=findUserById, id={}", id);
		return service //
				.findUserById(id).orElseThrow(() -> createUserNotFound(id));
	}

	public List<User> findUserByIds(final List<Long> list) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=findUserByIds, list.zise={}", list.size());

		return service.findUserByIds(list);
	}

	public User auth(final User user) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=auth, user={}", user);

		final User userDB = service //
				.findByDocumentValueOrEmail(user) //
				.orElseThrow(() -> new UserNotExistException(user));
		rule.verifyPasswordUserRequestWithUserDB(user, userDB);

		return userDB;
	}

	public void sendEmailRecoveryPassword(final Long id) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=sendEmailRecoveryPassword, id={}", id);

		final User user = service.findUserById(id).orElseThrow(() -> createUserNotFound(id));
		sendEmailRecoveryPassword(user.getEmail());
	}

	@Async
	public void sendEmailRecoveryPassword(final String email) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=sendEmailRecoveryPassword, email={}", email);

		final User user = service.findByDocumentValueOrEmail(User.builder().email(email).build())
				.orElseThrow(() -> createUserNotFound(email));
		sendEmail.execute(user, "password-recovery", "Recuperar senha.",
				String.format("Sua senha: %s", user.getPassword()));
	}

	@Async
	public void verifyTokenAndUpdateUser(final String token) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=verifyTokenAndUpdateUser, token={}", token);

		final User user = userTokenPackage.toUser(token, expireTokenMinutes);
		final User userDB = findUserById(user.getId());
		service.updateVerified(userMergeRules.merge(user, userDB));
	}

	public String createToken(final User user) {
		LOGGER.debug(MarkerFactory.getMarker("FACADE"), "m=createToken, user={}", user);

		return userTokenPackage.toPack(user);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAddress(final Long userId, final Long addressId) {
		service.deleteAddress(userId, addressId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deletePhone(final Long userId, final Long phoneId) {
		service.deletePhone(userId, phoneId);
	}

	private UserNotExistException createUserNotFound(final Long id) {
		return new UserNotExistException(User //
				.builder() //
				.id(id) //
				.build());
	}

	private UserNotExistException createUserNotFound(final String email) {
		return new UserNotExistException(User //
				.builder() //
				.email(email) //
				.build());
	}

	public Address findAddressByCep(final String cep) {
		try {
			return viaCepIntegration.getAddressByCep(cep);
		} catch (Exception e) {
			LOGGER.error(MarkerFactory.getMarker("FACADE"), "m=findAddressByCep, cep={}", cep, e);
			return Address.builder().build();
		}
	}

	public void sendEmail(final String to, final String subject, final String message) {
		sendEmail.sendEmail("notify", to, subject, message);
	}
	
	public void sendEmail(final Long userId, final String subject, final String message) {
		final User user = findUserById(userId);
		sendEmail.sendEmail("notify", user.getEmail(), subject, message);
	}
}
