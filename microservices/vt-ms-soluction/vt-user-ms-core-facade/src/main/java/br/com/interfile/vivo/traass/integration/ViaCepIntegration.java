package br.com.interfile.vivo.traass.integration;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.integration.converter.ViaCepIntegrationDtoToAddressConverter;
import br.com.interfile.vivo.traass.integration.dto.ViaCepIntegrationDto;

@Component
public class ViaCepIntegration {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ViaCepIntegrationDtoToAddressConverter viaCepIntegrationDtoToAddressConverter;

	public Address getAddressByCep(final String cep) {
		final ViaCepIntegrationDto viaCepIntegrationDto = restTemplate.getForObject(
				"http://viacep.com.br/ws/{cep}/json/", ViaCepIntegrationDto.class,
				Collections.singletonMap("cep", cep));
		
		return viaCepIntegrationDtoToAddressConverter.convert(viaCepIntegrationDto);
	}
}
