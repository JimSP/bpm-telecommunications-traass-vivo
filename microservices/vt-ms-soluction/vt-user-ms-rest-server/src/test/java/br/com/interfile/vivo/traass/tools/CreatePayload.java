package br.com.interfile.vivo.traass.tools;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.interfile.vivo.traass.dto.AddressDto;
import br.com.interfile.vivo.traass.dto.AuthUserRequestDto;
import br.com.interfile.vivo.traass.dto.PhoneDto;
import br.com.interfile.vivo.traass.dto.UserRequestDto;

public class CreatePayload {

	private final ObjectWriter objectWriter = new ObjectMapper() //
			.setSerializationInclusion(Include.NON_NULL) //
			.writerWithDefaultPrettyPrinter();

	@Test
	public void createCreatePayload() throws JsonProcessingException {
		System.out.println( //
				objectWriter //
						.writeValueAsString(UserRequestDto//
								.builder() //
								.name("Alexandre Moraes") //
								.email("alexandre.moraes@gingaone.com") //
								.confirmPassword("password") //
								.password("password") //
								.documentType("Cpf") //
								.documentValue("30553271873") //
								.phones(Arrays //
										.asList(PhoneDto //
												.builder() //
												.phoneType("Mobile") //
												.countryCode(55) //
												.operatorCode(15) //
												.areaCode(11) //
												.phoneNumber("989430384") //
												.build())) //
								.addresses(Arrays //
										.asList(AddressDto //
												.builder() //
												.addressType("Comercial") //
												.city("São Paulo") //
												.country("Brasil") //
												.complement("10 andar") //
												.neighborhood("Brooklin") //
												.province("SP") //
												.roadType("Avenida") //
												.streetName("Eng. Luis Carlos Berrini") //
												.streetNumber(1700) //
												.zipCode("04571935") //
												.build()))
								.build()));
		System.out.println();
	}

	@Test
	public void createAuthPayloadWithEmail() throws JsonProcessingException {
		System.out.println( //
				objectWriter //
						.writeValueAsString(AuthUserRequestDto//
								.builder() //
								.email("alexandre.moraes@gingaone.com") //
								.password("password") //
								.build()));
		System.out.println();
	}

	@Test
	public void createAuthPayloadWithDocumentValue() throws JsonProcessingException {
		System.out.println( //
				objectWriter //
						.writeValueAsString(AuthUserRequestDto//
								.builder() //
								.documentValue("30553271873") //
								.password("password") //
								.build()));
		System.out.println();
	}
}
