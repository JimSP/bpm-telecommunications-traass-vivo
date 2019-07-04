package br.com.interfile.vivo.traass.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.dto.AddressDto;
import br.com.interfile.vivo.traass.dto.AuthUserRequestDto;
import br.com.interfile.vivo.traass.dto.ListUserResponseDto;
import br.com.interfile.vivo.traass.dto.SendEmailRequestDto;
import br.com.interfile.vivo.traass.dto.UserRequestDto;
import br.com.interfile.vivo.traass.dto.UserResponseDto;
import br.com.interfile.vivo.traass.facade.UserFacade;
import br.com.interfile.vivo.traass.validation.CpfOrCnpjValidator;

@RestController
public class UserRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private UserFacade facade;

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private CpfOrCnpjValidator cpfOrCnpjValidator;

	@GetMapping(value = "/vt-user-ms/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody UserResponseDto get(@PathVariable(name = "id", required = true) final Long id) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=get, id={}", id);

		final User user = facade.findUserById(id);
		return conversionService.convert(user, UserResponseDto.class);
	}

	@GetMapping(value = "/vt-user-ms/list/{ids}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ListUserResponseDto get(@PathVariable(name = "ids", required = true) final String ids) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=get, ids={}", ids);

		final List<Long> list = Arrays //
				.asList(ids.split("[,]")) //
				.stream() //
				.map(mapper -> Long.valueOf(mapper)) //
				.collect(Collectors.toList());

		return ListUserResponseDto //
				.builder() //
				.userResponseDtos(facade //
						.findUserByIds(list)//
						.stream() //
						.map(mapper -> conversionService.convert(mapper, UserResponseDto.class)) //
						.collect(Collectors.toList())) //
				.build();
	}

	@GetMapping(value = "/vt-user-ms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ListUserResponseDto get(@Param(value = "documentValue") final String documentValue,
			@Param(value = "email") final String email) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=get, documentValue={}, email={}", documentValue, email);

		return ListUserResponseDto //
				.builder() //
				.userResponseDtos(facade //
						.findUser( //
								User //
										.builder() //
										.documents(Arrays //
												.asList(Document //
														.builder() //
														.documentValue(documentValue) //
														.build())) //
										.email(email) //
										.build()) //
						.get() //
						.map(mapper -> conversionService.convert(mapper, UserResponseDto.class)) //
						.collect(Collectors.toList())) //
				.build();
	}

	@PostMapping(value = "/vt-user-ms", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody UserResponseDto post(@Valid @RequestBody final UserRequestDto request) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=post, request={}", request);

		Assert.isTrue(request.getPassword().equals(request.getConfirmPassword()),
				"password not equals confirmPassword");

		final User user = conversionService.convert(request, User.class);
		final User userCreated = facade.saveUserAndSendVerifyEmail(user);

		return conversionService.convert(userCreated, UserResponseDto.class);
	}

	@PostMapping(value = "/vt-user-ms/auth", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody UserResponseDto auth(@Valid @RequestBody final AuthUserRequestDto request) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=post, request={}", request);

		final User user = conversionService.convert(request, User.class);
		final User userAuth = facade.auth(user);

		return conversionService.convert(userAuth, UserResponseDto.class);
	}

	@PostMapping(value = "vt-user-ms/auth/token")
	public ResponseEntity<UserResponseDto> createToken(@Valid @RequestBody final AuthUserRequestDto request) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=createToken, request={}", request);
		final User user = conversionService.convert(request, User.class);
		final User userAuth = facade.auth(user);

		return Optional //
				.of(userAuth) //
				.map(u -> ResponseEntity.status(HttpStatus.CREATED).header("VT-USER-MS-AUTHORIZATION",
						facade.createToken(u)))
				.map(b -> b.body(conversionService.convert(userAuth, UserResponseDto.class))).get();
	}

	@PutMapping(value = "vt-user-ms/verified")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void verifyEmail(@RequestBody final String token) {
		Assert.hasText(token, "token is not blank or null");
		facade.verifyTokenAndUpdateUser(token);
	}

	@PostMapping(value = "/vt-user-ms/auth/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void recoveryPassword(@PathVariable(value = "id", required = true) final Long id) {
		facade.sendEmailRecoveryPassword(id);
	}

	@PostMapping(value = "/vt-user-ms/auth/email/{email}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void recoveryPasswordByEmail(@PathVariable(value = "email", required = true) final String email) {
		facade.sendEmailRecoveryPassword(email);
	}

	@PutMapping(value = "/vt-user-ms/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void put(@PathVariable(value = "id", required = true) final Long id,
			@Valid @RequestBody final UserRequestDto request) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=put, id={}, request={}", id, request);

		Assert.isTrue(request.getPassword().equals(request.getConfirmPassword()),
				"password not equals confirmPassword");

		final User user = conversionService.convert(request, User.class);
		facade.updateById(id, user);
	}

	@DeleteMapping(value = "/vt-user-ms/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void delete(@PathVariable(value = "id", required = true) final Long id) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=delete, id={}", id);
		facade.delete(id);
	}

	@DeleteMapping(value = "/vt-user-ms/{userId}/address/{addressId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteAddress(@PathVariable(value = "userId", required = true) final Long userId,
			@PathVariable(value = "addressId", required = true) final Long addressId) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=deleteAddress, userId={}, addressId={}", userId, addressId);
		facade.deleteAddress(userId, addressId);
	}

	@DeleteMapping(value = "/vt-user-ms/{userId}/phone/{phoneId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deletePhone(@PathVariable(value = "userId", required = true) final Long userId,
			@PathVariable(value = "phoneId", required = true) final Long phoneId) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=deletePhone, userId={}, phoneId={}", userId, phoneId);
		facade.deletePhone(userId, phoneId);
	}

	@GetMapping(value = "/validator/cpf/{cpf}")
	public Boolean validCpf(@PathVariable(value = "cpf", required = true) final String cpf) {
		return cpfOrCnpjValidator.isValidCpf(cpf);
	}

	@GetMapping(value = "/validator/cnpj/{cnpj}")
	public Boolean validCnpj(@PathVariable(value = "cnpj", required = true) final String cnpj) {
		return cpfOrCnpjValidator.isValidCnpj(cnpj);
	}
	
	@PostMapping(value = "/vt-user-ms/sendEmail/{userId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void sendEmailToUserId(@PathVariable(value = "userId", required = true) final Long userId, @Valid @RequestBody final SendEmailRequestDto request) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=post, request={}", request);
		facade.sendEmail(userId, request.getSubject(), request.getMessage());
	}
	
	@GetMapping(value = "/vt-address-ms/cep/{cep}")
	public AddressDto findAddresByCep(@PathVariable(value = "cep", required = true) final String cep) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=findAddresByCep, cep={}", cep);
		return conversionService.convert(facade.findAddressByCep(cep), AddressDto.class);
	}
}
