package br.com.interfile.vivo.traass.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.Transferee;
import br.com.interfile.vivo.traass.jpa.converter.AddressEntityToAddressConverter;
import br.com.interfile.vivo.traass.jpa.converter.AddressToAddressEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.DonnorToDonnorEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.PhoneEntityToPhoneConverter;
import br.com.interfile.vivo.traass.jpa.converter.PhoneToPhoneEntityConverter;
import br.com.interfile.vivo.traass.jpa.converter.TransfereeToTransfereeEntityConverter;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;
import br.com.interfile.vivo.traass.jpa.entities.DonnorEntity;
import br.com.interfile.vivo.traass.jpa.entities.PhoneEntity;
import br.com.interfile.vivo.traass.jpa.entities.SolicitationEntity;
import br.com.interfile.vivo.traass.jpa.entities.TransfereeEntity;
import br.com.interfile.vivo.traass.jpa.repositories.AddressJpaRepository;
import br.com.interfile.vivo.traass.jpa.repositories.DonnorJpaRepository;
import br.com.interfile.vivo.traass.jpa.repositories.PhoneJpaRepository;
import br.com.interfile.vivo.traass.jpa.repositories.TransfereeJpaRepository;

@Service
public class CommumService {

	@Autowired
	private PhoneJpaRepository phoneJpaRepository;

	@Autowired
	private AddressJpaRepository addressJpaRepository;

	@Autowired
	private AddressToAddressEntityConverter addressToAddressEntityConverter;

	@Autowired
	private AddressEntityToAddressConverter addressEntityToAddressConverter;

	@Autowired
	private PhoneToPhoneEntityConverter phoneToPhoneEntityConverter;

	@Autowired
	private PhoneEntityToPhoneConverter phoneEntityToPhoneConverter;

	@Autowired
	private DonnorJpaRepository donnorJpaRepository;

	@Autowired
	private TransfereeJpaRepository transfereeJpaRepository;

	@Autowired
	private DonnorToDonnorEntityConverter donnorToDonnorEntityConverter;

	@Autowired
	private TransfereeToTransfereeEntityConverter transfereeToTransfereeEntityConverter;

	@Transactional(propagation = Propagation.REQUIRED)
	public Address saveAddress(final Address address) {
		final AddressEntity addressEntity = addressToAddressEntityConverter.convert(address);
		final AddressEntity addressEntitySaved = addressJpaRepository.save(addressEntity);
		return addressEntityToAddressConverter.convert(addressEntitySaved);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAddress(final Address address) {
		final AddressEntity addressEntity = addressToAddressEntityConverter.convert(address);
		final AddressEntity adEntityExist = addressJpaRepository.findById(address.getId()).get();

		adEntityExist.setAddressType(addressEntity.getAddressType());
		adEntityExist.setCity(addressEntity.getCity());
		adEntityExist.setComplement(addressEntity.getComplement());
		adEntityExist.setCountry(addressEntity.getCountry());
		adEntityExist.setNeighborhood(addressEntity.getNeighborhood());
		adEntityExist.setProvince(addressEntity.getProvince());
		adEntityExist.setRoadType(addressEntity.getRoadType());
		adEntityExist.setStreetName(addressEntity.getStreetName());
		adEntityExist.setStreetNumber(addressEntity.getStreetNumber());
		adEntityExist.setZipCode(addressEntity.getZipCode());

		addressJpaRepository.save(adEntityExist);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Phone savePhone(final Phone phone) {
		final PhoneEntity phoneEntity = phoneToPhoneEntityConverter.convert(phone);
		final PhoneEntity phoneEntitySaved = phoneJpaRepository.save(phoneEntity);
		return phoneEntityToPhoneConverter.convert(phoneEntitySaved);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePhone(final Phone phone) {
		final PhoneEntity phoneEntity = phoneToPhoneEntityConverter.convert(phone);

		if (phone.getId() != null) {
			final PhoneEntity phoneEntityExist = phoneJpaRepository.findById(phone.getId()).get();

			phoneEntityExist.setAreaCode(phoneEntity.getAreaCode());
			phoneEntityExist.setCountryCode(phoneEntity.getCountryCode());
			phoneEntityExist.setOperatorCode(phoneEntity.getOperatorCode());
			phoneEntityExist.setPhoneNumber(phoneEntity.getPhoneNumber());
			phoneEntityExist.setPhoneType(phoneEntity.getPhoneType());
			phoneJpaRepository.save(phoneEntityExist);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateDonnor(final Donnor donnor, final Long solicitationId) {
		donnor //
				.getDonnorAddresses() //
				.forEach(action -> {
					updateAddress(action);
				});

		updatePhone(donnor.getDonnorPhone());

		final DonnorEntity donnorEntity = donnorToDonnorEntityConverter.convert(donnor);
		final DonnorEntity donnorEntityExist = donnorJpaRepository.findById(donnor.getId()).get();
		donnorEntityExist.setDonnorDocumentType(donnorEntity.getDonnorDocumentType());
		donnorEntityExist.setDonnorDocumentValue(donnorEntity.getDonnorDocumentValue());
		donnorEntityExist.setDonnorEmail(donnorEntity.getDonnorEmail());
		donnorEntityExist.setDonnorName(donnorEntityExist.getDonnorName());
		donnorEntityExist.setDonnorRg(donnorEntityExist.getDonnorRg());
		donnorEntityExist.setSolicitationEntity(SolicitationEntity.builder().id(solicitationId).build());
		donnorJpaRepository.save(donnorEntityExist);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTransferee(final Transferee transferee, final Long solicitationId) {
		transferee.getTransfereeAddresses().forEach(action -> {
			updateAddress(action);
		});

		updatePhone(transferee.getTransfereePhone());

		final TransfereeEntity transfereeEntity = transfereeToTransfereeEntityConverter.convert(transferee);
		final TransfereeEntity transfereeEntityExist = transfereeJpaRepository.findById(transferee.getId()).get();

		transfereeEntityExist.setTransfereeDocumentType(transfereeEntity.getTransfereeDocumentType());
		transfereeEntityExist.setTransfereeDocumentValue(transfereeEntity.getTransfereeDocumentValue());
		transfereeEntityExist.setSolicitationEntity(SolicitationEntity.builder().id(solicitationId).build());

		transfereeJpaRepository.save(transfereeEntityExist);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAddress(final Long addressId) {
		addressJpaRepository.deleteById(addressId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deletePhone(final Long phoneId) {
		phoneJpaRepository.deleteById(phoneId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDonnor(final Long solicitationId, final Long donnorId) {
		donnorJpaRepository.deleteById(donnorId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTransferee(final Long solicitationId, final Long transfereeId) {
		transfereeJpaRepository.deleteById(transfereeId);
	}
}
