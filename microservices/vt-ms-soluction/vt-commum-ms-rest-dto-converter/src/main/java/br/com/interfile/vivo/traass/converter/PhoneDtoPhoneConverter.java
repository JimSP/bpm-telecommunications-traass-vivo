package br.com.interfile.vivo.traass.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.PhoneType;
import br.com.interfile.vivo.traass.dto.PhoneDto;

@Component
public class PhoneDtoPhoneConverter implements Converter<PhoneDto, Phone> {

	@Override
	public Phone convert(final PhoneDto phoneDto) {
		Assert.notNull(phoneDto, "phoneDto is not null.");

		return Phone //
				.builder() //
				.id(phoneDto.getId()) //
				.areaCode(phoneDto.getAreaCode()) //
				.countryCode(phoneDto.getCountryCode()) //
				.operatorCode(phoneDto.getOperatorCode()) //
				.phoneType(PhoneType.create(phoneDto.getPhoneType())) //
				.phoneNumber(phoneDto.getPhoneNumber())//
				.build();
	}

}
