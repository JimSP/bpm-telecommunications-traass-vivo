package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.jpa.entities.PhoneEntity;

@Component
public class PhoneToPhoneEntityConverter implements Converter<Phone, PhoneEntity> {

	@Override
	public PhoneEntity convert(final Phone phone) {
		return PhoneEntity //
				.builder() //
				.areaCode(phone.getAreaCode()) //
				.countryCode(phone.getCountryCode()) //
				.id(phone.getId()) //
				.operatorCode(phone.getOperatorCode()) //
				.phoneNumber(phone.getPhoneNumber()) //
				.phoneType(phone.getPhoneType().name()) //
				.build();
	}
}
