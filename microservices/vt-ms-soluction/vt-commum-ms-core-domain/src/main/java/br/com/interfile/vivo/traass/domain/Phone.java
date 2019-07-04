package br.com.interfile.vivo.traass.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder=true)
@Data
public class Phone {

	private final Long id;
	private final PhoneType phoneType;
	private final Integer countryCode;
	private final Integer areaCode;
	private final Integer operatorCode;
	private final String phoneNumber;
}
