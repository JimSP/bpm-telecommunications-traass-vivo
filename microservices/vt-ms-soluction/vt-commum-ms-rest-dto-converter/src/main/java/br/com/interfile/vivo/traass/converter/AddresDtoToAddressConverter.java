package br.com.interfile.vivo.traass.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.AddressType;
import br.com.interfile.vivo.traass.dto.AddressDto;

@Component
public class AddresDtoToAddressConverter implements Converter<AddressDto, Address> {

	@Override
	public Address convert(final AddressDto addressDto) {
		Assert.notNull(addressDto, "addressDto is not null.");

		return Address //
				.builder() //
				.id(addressDto.getId()) //
				.addressType(AddressType.create(addressDto.getAddressType())) //
				.city(addressDto.getCity()) //
				.complement(addressDto.getComplement()) //
				.country(addressDto.getCountry()) //
				.neighborhood(addressDto.getNeighborhood()) //
				.province(addressDto.getProvince()) //
				.roadType(addressDto.getRoadType()) //
				.streetName(addressDto.getStreetName()) //
				.streetNumber(addressDto.getStreetNumber()) //
				.zipCode(addressDto.getZipCode()) //
				.build();
	}
}
