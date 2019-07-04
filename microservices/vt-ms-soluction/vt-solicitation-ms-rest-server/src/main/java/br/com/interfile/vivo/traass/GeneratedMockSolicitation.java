package br.com.interfile.vivo.traass;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.converter.SolicitationRequestDtoToSolicitationConverter;
import br.com.interfile.vivo.traass.domain.AddressType;
import br.com.interfile.vivo.traass.domain.DocumentType;
import br.com.interfile.vivo.traass.domain.PhoneType;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.dto.AddressDto;
import br.com.interfile.vivo.traass.dto.DigitalDocumentDto;
import br.com.interfile.vivo.traass.dto.DonnorDto;
import br.com.interfile.vivo.traass.dto.PhoneDto;
import br.com.interfile.vivo.traass.dto.SolicitationRequestDto;
import br.com.interfile.vivo.traass.dto.TransfereeDto;
import br.com.interfile.vivo.traass.jpa.converter.SolicitationToSolicitationEntityConverter;
import br.com.interfile.vivo.traass.jpa.entities.SolicitationEntity;
import br.com.interfile.vivo.traass.jpa.repositories.SolicitationJpaRepository;

@Component
public class GeneratedMockSolicitation implements CommandLineRunner {

	private static final Boolean MOCK_ENABLE = Boolean.FALSE;

	private static final Random ramdom = new Random(System.currentTimeMillis());

	@Autowired
	private SolicitationJpaRepository solicitationJpaRepository;

	@Autowired
	private SolicitationRequestDtoToSolicitationConverter solicitationRequestDtoToSolicitationConverter;

	@Autowired
	private SolicitationToSolicitationEntityConverter solicitationEntityConverter;

	@Async
	public void execute() {

		long i = 1;
		while (MOCK_ENABLE && i <= 1000) {
			final Solicitation solicitation = solicitationRequestDtoToSolicitationConverter
					.convert(createSolicitationRequestDto());
			final SolicitationEntity solicitationEntity = solicitationEntityConverter.convert(solicitation);
			solicitationJpaRepository.save(solicitationEntity.toBuilder().id(i).build());
			i++;
		}
	}

	@Override
	public void run(String... args) throws Exception {
		execute();
	}

	public static SolicitationRequestDto createSolicitationRequestDto() {
		final String channelReception = "CHANNEL RECEPTION";
		final String comment = "COMMENT";

		final List<DigitalDocumentDto> digitalDocuments = createDigitalDocumentList();
		final String entryMailbox = "ENTRY MAILBOX";
		final AddressDto solicitationAddress = createAddress();
		final List<DonnorDto> donnors = createDonnors();
		final List<TransfereeDto> transferees = createTransferees();

		return SolicitationRequestDto //
				.builder() //
				.channelReception(channelReception) //
				.comment(comment) //
				.digitalDocuments(digitalDocuments) //
				.donnors(donnors) //
				.entryMailbox(entryMailbox) //
				.userId(1L) //
				.transferees(transferees) //
				.solicitationAddress(solicitationAddress) //
				.build();
	}

	private static List<TransfereeDto> createTransferees() {

		final String transfereeDocumentType = DocumentType.Cpf.name();
		final String transfereeDocumentValue = "22222222222";
		final String transfereeEmail = "transferee@ginga.com";
		final String transfereeName = "TRANSFEREE NAME";
		final PhoneDto transfereePhone = createPhone();
		final List<AddressDto> transfereeAddresses = createAddressList();

		return Arrays //
				.asList(TransfereeDto //
						.builder() //
						.transfereeAddresses(createAddressList()) //
						.transfereeDocumentType(transfereeDocumentType) //
						.transfereeDocumentValue(transfereeDocumentValue) //
						.transfereeEmail(transfereeEmail) //
						.transfereeName(transfereeName) //
						.transfereePhone(transfereePhone) //
						.transfereeAddresses(transfereeAddresses) //
						.build());
	}

	private static List<DonnorDto> createDonnors() {

		final List<AddressDto> donnorAddresses = createAddressList();

		final String donnorDocumentType = DocumentType.Cpf.name();
		final String donnorDocumentValue = "11111111111";
		final String donnorRg = "111111111";
		final String donnorEmail = "donnor@ginga.com";
		final String donnorName = "DONNOR NAME";
		final PhoneDto donnorPhone = createPhone();

		return Arrays.asList(DonnorDto //
				.builder() //
				.donnorAddresses(donnorAddresses) //
				.donnorDocumentType(donnorDocumentType) //
				.donnorDocumentValue(donnorDocumentValue) //
				.donnorEmail(donnorEmail) //
				.donnorName(donnorName) //
				.donnorPhone(donnorPhone) //
				.donnorRg(donnorRg) //
				.build());
	}

	public static AddressDto createAddress() {
		return AddressDto //
				.builder() //
				.addressType(AddressType.Comercial.name()) //
				.city("SAO PAULO") //
				.complement("12 andar") //
				.country("BRASIL") //
				.neighborhood("BROOKLIN") //
				.province("SP") //
				.roadType("AVENIDA") //
				.streetName("ENG. LUIS CARLOS BERRINI") //
				.streetNumber(1700) //
				.zipCode("05447000") //
				.build();
	}

	public static PhoneDto createPhone() {
		return PhoneDto //
				.builder() //
				.areaCode(11) //
				.countryCode(55) //
				.operatorCode(15) //
				.phoneType(PhoneType.Work.name()) //
				.phoneNumber("5212 7379")//
				.build();
	}

	public static List<AddressDto> createAddressList() {
		return Arrays.asList(createAddress());
	}

	public static List<DigitalDocumentDto> createDigitalDocumentList() {
		final String name = "IMAGE_NAME" + ramdom.nextLong();
		return Arrays.asList( //
				DigitalDocumentDto //
						.builder() //
						.documentType(DocumentType.Cpf.name()) //
						.referenceName(name) //
						.data(new byte[] { 127, 127, 127, 88, 127, 127, 96, 0, 16, 74, 70, 73, 70, 0 }) //
						.url("http://localhost:8092/" + name).build());
	}
}