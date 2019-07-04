package br.com.interfile.vivo.traass.jpa.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.Document;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.User;
import br.com.interfile.vivo.traass.jpa.converter.AddressToAddressEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.PhoneToPhoneEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.UserEntityToUserConverter;
import br.com.interfile.vivo.traass.jpa.converter.UserToUserEntityConverter;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;
import br.com.interfile.vivo.traass.jpa.entities.PhoneEntity;
import br.com.interfile.vivo.traass.jpa.entities.UserEntity;
import br.com.interfile.vivo.traass.jpa.repositories.AddressJpaRepository;
import br.com.interfile.vivo.traass.jpa.repositories.PhoneJpaRepository;
import br.com.interfile.vivo.traass.jpa.repositories.UserJpaRepository;
import br.com.interfile.vivo.traass.jpa.service.CommumService;

@Component
public class UserService {

	private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private CommumService commumService;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private PhoneJpaRepository phoneJpaRepository;

	@Autowired
	private AddressJpaRepository addressJpaRepository;

	@Autowired
	private UserToUserEntityConverter userToUserEntityConverter;

	@Autowired
	private UserEntityToUserConverter userEntityToUserConverter;

	@Autowired
	private AddressToAddressEntityConverter addressToAddressEntityConverter;

	@Autowired
	private PhoneToPhoneEntityConverter phoneToPhoneEntityConverter;

	public Optional<User> findUserById(final Long id) {
		return userJpaRepository.findById(id).map(mapper -> userEntityToUserConverter.convert(mapper));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User save(final User user) {
		LOGGER.info(MarkerFactory.getMarker("SERVICE"), "m=save, user={}", user);

		final UserEntity userEntity = userToUserEntityConverter.convert(user);
		final UserEntity entityCreate = userJpaRepository.save(userEntity);

		if (entityCreate.getAddressEntitys() == null) {
			final List<Address> addresses = Optional //
					.ofNullable(user.getAddresses()) //
					.orElse(Collections.emptyList());

			entityCreate.setAddressEntitys(addresses //
					.stream() //
					.map(addressToAddressEntityConverter::convert) //
					.map(addressJpaRepository::save) //
					.collect(Collectors.toList()));
		}

		if (entityCreate.getPhoneEntitys() == null) {
			final List<Phone> phones = Optional //
					.ofNullable(user //
							.getPhones()) //
					.orElse(Collections.emptyList()); //

			entityCreate.setPhoneEntitys(phones //
					.stream() //
					.map(phoneToPhoneEntityConverter::convert) //
					.map(phoneJpaRepository::save) //
					.collect(Collectors.toList()));
		}

		return userEntityToUserConverter.convert(entityCreate);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateVerified(final User user) {
		final UserEntity userEntityExist = userJpaRepository.findById(user.getId()).get();
		userEntityExist.setVerified(Boolean.TRUE);
		userJpaRepository.save(userEntityExist);
	}

	public Supplier<Stream<User>> findByExample(final User user) {
		final UserEntity entity = userToUserEntityConverter.convert(user);

		return () -> userJpaRepository //
				.findAll(Example.of(entity)) //
				.stream() //
				.map(mapper -> userEntityToUserConverter //
						.convert(mapper));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final Long id) {
		userJpaRepository.delete(UserEntity.builder().id(id).build());
	}

	public List<User> findUserByIds(final List<Long> list) {
		return userJpaRepository //
				.findByIds(list) //
				.stream() //
				.map(mapper -> userEntityToUserConverter.convert(mapper)) //
				.collect(Collectors.toList());
	}

	public Optional<User> findByDocumentValueOrEmail(final User user) {
		final List<Document> list = Optional //
				.ofNullable(user.getDocuments()).orElse(Collections.emptyList());

		String documentValue = null;
		if (!list.isEmpty()) {
			documentValue = list.get(0).getDocumentValue();
		}

		final String email = user.getEmail();

		return userJpaRepository //
				.findByDocumentValueOrEmail(documentValue, //
						email) //
				.map(mapper -> userEntityToUserConverter.convert(mapper));
	}

	public void update(final User user) {

		final List<AddressEntity> addressEntitieSaves = new ArrayList<>();
		if (user.getAddresses() != null) {
			user.getAddresses().stream().forEach(action -> {
				if (action.getId() == null) {
					addressEntitieSaves.add(addressToAddressEntityConverter.convert(action));
				} else {
					commumService.updateAddress(action);
				}
			});
		}

		final List<PhoneEntity> phoneEntitieSaves = new ArrayList<>();
		if (user.getPhones() != null) {
			user.getPhones().stream().forEach(action -> {
				if (action.getId() == null) {
					phoneEntitieSaves.add(phoneToPhoneEntityConverter.convert(action));
				} else {
					commumService.updatePhone(action);
				}
			});
		}

		final UserEntity userEntity = userToUserEntityConverter.convert(user);
		final UserEntity userExist = userJpaRepository.findById(user.getId()).get();

		userExist.setAddressEntitys(addressEntitieSaves);
		userExist.setPhoneEntitys(phoneEntitieSaves);

		userExist.setDocumentType(userEntity.getDocumentType());
		userExist.setDocumentValue(userEntity.getDocumentValue());
		userExist.setEmail(userEntity.getEmail());
		userExist.setName(userEntity.getName());
		userExist.setPassword(userEntity.getPassword());

		userJpaRepository.save(userExist);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAddress(final Long userId, final Long addressId) {
		commumService.deleteAddress(addressId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deletePhone(final Long userId, final Long phoneId) {
		commumService.deletePhone(phoneId);
	}
}
