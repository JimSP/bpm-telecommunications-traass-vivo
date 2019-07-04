package br.com.interfile.vivo.traass.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.dto.PhoneDto;

@Component
public class PhoneToPhoneDtoConverter implements Converter<Phone, PhoneDto>{

	@Override
	public PhoneDto convert(final Phone phone) {
		Assert.notNull(phone, "phone is not null.");
		
		return PhoneDto //
				.builder() //
				.id(phone.getId()) //
				.phoneType(phone.getPhoneType().name()) //
				.countryCode(phone.getCountryCode()) //
				.areaCode(phone.getAreaCode()) //
				.operatorCode(phone.getOperatorCode()) //
				.phoneNumber(phone.getPhoneNumber()) //
				.build();
	}
}
