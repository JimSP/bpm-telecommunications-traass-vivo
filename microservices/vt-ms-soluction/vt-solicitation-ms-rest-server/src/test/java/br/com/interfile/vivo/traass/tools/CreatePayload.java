package br.com.interfile.vivo.traass.tools;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.interfile.vivo.traass.domain.Address;
import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.domain.Donnor;
import br.com.interfile.vivo.traass.domain.Phone;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.Transferee;
import br.com.interfile.vivo.traass.dto.AddressDto;
import br.com.interfile.vivo.traass.dto.DigitalDocumentDto;
import br.com.interfile.vivo.traass.dto.DonnorDto;
import br.com.interfile.vivo.traass.dto.PhoneDto;
import br.com.interfile.vivo.traass.dto.SolicitationRequestDto;
import br.com.interfile.vivo.traass.dto.TransfereeDto;

public class CreatePayload {
	
	private final ObjectWriter objectWriter = new ObjectMapper() //
			.setSerializationInclusion(Include.ALWAYS) //
			.writerWithDefaultPrettyPrinter();

	@Test
	public void createPayloadCamunda() throws JsonProcessingException {
		System.out.println("camunda:" + //
				objectWriter //
						.writeValueAsString(Solicitation//
								.builder() //
								.digitalDocuments(Arrays.asList( //
										DigitalDocument //
										.builder() //
										.build())) //
								.donnors(Arrays.asList( //
										Donnor //
										.builder() //
										.donnorPhone(Phone //
												.builder() //
												.build()) //
										.donnorAddresses(Arrays.asList( //
												Address //
												.builder() //
												.build())) //
										.build())) //
								.solicitationAddress(Address.builder().build())
								.transferees(Arrays.asList(
										Transferee
										.builder()
										.transfereeAddresses(Arrays.asList(Address.builder().build()))
										.transfereePhone(Phone.builder().build())
										.build()))
								.build()));
	}
	
	@Test
	public void createCreatePayload() throws JsonProcessingException {
		System.out.println("portal" + //
				objectWriter //
						.writeValueAsString(SolicitationRequestDto//
								.builder() //
								.digitalDocuments(Arrays.asList( //
										DigitalDocumentDto //
										.builder() //
										.build())) //
								.donnors(Arrays.asList( //
										DonnorDto //
										.builder() //
										.donnorPhone(PhoneDto //
												.builder() //
												.build()) //
										.donnorAddresses(Arrays.asList( //
												AddressDto //
												.builder() //
												.build())) //
										.build())) //
								.solicitationAddress(AddressDto.builder().build())
								.transferees(Arrays.asList(
										TransfereeDto
										.builder()
										.transfereeAddresses(Arrays.asList(AddressDto.builder().build()))
										.transfereePhone(PhoneDto.builder().build())
										.build()))
								.build()));
		System.out.println();
	}
}
