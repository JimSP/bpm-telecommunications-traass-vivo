package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.AddressType;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;

@Component
public class AddressEntityToAddressConverter implements Converter<AddressEntity, Address> {

	@Override
	public Address convert(final AddressEntity addressEntity) {
		return Address //
				.builder() //
				.id(addressEntity.getId()) //
				.addressType(AddressType.create(addressEntity.getAddressType())) //
				.city(addressEntity.getCity()) //
				.complement(addressEntity.getComplement()) //
				.country(addressEntity.getCountry()) //
				.neighborhood(addressEntity.getNeighborhood()) //
				.province(addressEntity.getProvince()) //
				.roadType(addressEntity.getRoadType()) //
				.streetName(addressEntity.getStreetName()) //
				.streetNumber(addressEntity.getStreetNumber()) //
				.zipCode(addressEntity.getZipCode()) //
				.build();
	}
}
