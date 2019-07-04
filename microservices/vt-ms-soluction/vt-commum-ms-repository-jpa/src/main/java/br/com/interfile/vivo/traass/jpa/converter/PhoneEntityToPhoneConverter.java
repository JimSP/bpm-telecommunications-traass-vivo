package br.com.interfile.vivo.traass.jpa.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.PhoneType;
import br.com.interfile.vivo.traass.jpa.entities.PhoneEntity;

@Component
public class PhoneEntityToPhoneConverter implements Converter<PhoneEntity, Phone>{

	@Override
	public Phone convert(final PhoneEntity phoneEntity) {
		return Phone //
				.builder() //
				.id(phoneEntity.getId()) //
				.areaCode(phoneEntity.getAreaCode()) //
				.countryCode(phoneEntity.getCountryCode()) //
				.operatorCode(phoneEntity.getOperatorCode()) //
				.phoneNumber(phoneEntity.getPhoneNumber()) //
				.phoneType(PhoneType.create(phoneEntity.getPhoneType())) //
				.build();
	}
}
