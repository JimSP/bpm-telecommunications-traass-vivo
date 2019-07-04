package br.com.interfile.vivo.traass.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.dto.AddressDto;

@Component
public class AddressToAddressDtoConverter implements Converter<Address, AddressDto> {

	@Override
	public AddressDto convert(final Address address) {
		Assert.notNull(address, "address is not null");
		Assert.notNull(address.getAddressType(), "address.AddressType");

		return AddressDto //
				.builder() //
				.id(address.getId()) //
				.addressType(address.getAddressType().name()) //
				.city(address.getCity()) //
				.complement(address.getComplement()) //
				.country(address.getCountry()) //
				.neighborhood(address.getNeighborhood()) //
				.province(address.getProvince()) //
				.roadType(address.getRoadType()) //
				.streetName(address.getStreetName()) //
				.streetNumber(address.getStreetNumber()) //
				.zipCode(address.getZipCode()) //
				.build();
	}

}
