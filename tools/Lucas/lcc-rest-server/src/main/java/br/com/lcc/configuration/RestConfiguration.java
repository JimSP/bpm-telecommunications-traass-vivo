package br.com.lcc.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.lcc.domain.Lucas;
import br.com.lcc.dto.LucasResponseDto;

@Configuration
public class RestConfiguration {

	@Bean
	public ModelMapper modelMapper() {

		final ModelMapper modelMapper = new ModelMapper();
		modelMapper //
				.addMappings(domainMap());

		return modelMapper;
	}

	private PropertyMap<Lucas, LucasResponseDto> domainMap() {
		return new PropertyMap<Lucas, LucasResponseDto>() {

			@Override
			protected void configure() {
				//this.destination = new LucasResponseDto(this.source.getId());
			}
		};
	}
}
