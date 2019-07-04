package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.jpa.entities.AddressEntity;

@Component
public class AddressToAddressEntityConverter implements Converter<Address, AddressEntity> {

	@Override
	public AddressEntity convert(final Address address) {
		Assert.notNull(address, "address is null");
		Assert.notNull(address.getAddressType(), "address.AddressType is null");
		
		return AddressEntity //
				.builder() //
				.addressType(address.getAddressType().name()) //
				.city(address.getCity()) //
				.complement(address.getComplement()) //
				.country(address.getCountry()) //
				.id(address.getId()) //
				.neighborhood(address.getNeighborhood()) //
				.province(address.getProvince()) //
				.roadType(address.getRoadType()) //
				.streetName(address.getStreetName()) //
				.streetNumber(address.getStreetNumber()) //
				.zipCode(address.getZipCode()) //
				.build();
	}

}
