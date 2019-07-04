package br.com.interfile.vivo.traass.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder=true)
@Data
public class Address {
	
	private final Long id;
	private final AddressType addressType;
	private final String roadType;
	private final String streetName;
	private final Integer streetNumber;
	private final String complement;
	private final String neighborhood;
	private final String city;
	private final String province;
	private final String country;
	private final String zipCode;

}
