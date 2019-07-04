package br.com.interfile.vivo.traass.integration.converter;

import java.util.Arrays;
import java.util.function.Function;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.AddressType;
import br.com.interfile.vivo.traass.integration.dto.ViaCepIntegrationDto;

@Component
public class ViaCepIntegrationDtoToAddressConverter implements Converter<ViaCepIntegrationDto, Address> {

	@Override
	public Address convert(final ViaCepIntegrationDto viaCepIntegrationDto) {
		if (viaCepIntegrationDto == null) {
			return null;
		}

		final LogradouroPartName logradouroPartName = splitLogradouro().apply(viaCepIntegrationDto.getLogradouro());

		return Address //
				.builder() //
				.addressType(AddressType.Undefined) //
				.roadType(logradouroPartName.roadType) //
				.province(viaCepIntegrationDto.getUf()) //
				.neighborhood(viaCepIntegrationDto.getBairro()) //
				.country("Brasil") //
				.complement(viaCepIntegrationDto.getComplemento()) //
				.city(viaCepIntegrationDto.getLocalidade()) //
				.streetName(logradouroPartName.streetName) //
				.zipCode(viaCepIntegrationDto.getCep()) //
				.build();
	}

	private Function<String, LogradouroPartName> splitLogradouro() {
		return (logradouro) -> {
			final String[] pattern = logradouro.split(" ");

			final String streetName = Arrays //
					.asList(pattern) //
					.subList(1, pattern.length) //
					.stream() //
					.reduce((a, b) -> a //
							.concat(" ") //
							.concat(b)) //
					.orElse(null);

			return new LogradouroPartName(pattern[0], streetName);
		};
	}

	private class LogradouroPartName {

		private final String roadType;
		private final String streetName;

		private LogradouroPartName(final String roadType, final String streetName) {
			this.roadType = roadType;
			this.streetName = streetName;
		}
	}
}
