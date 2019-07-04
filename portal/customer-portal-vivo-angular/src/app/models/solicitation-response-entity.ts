import { AddressEntity } from './address-entity';
import { PhoneEntity } from './phone-entity';
import { DigitalDocumentEntity } from './digital-document-entity';
import { UserEntity } from './user-entity';

export class SolicitationResponseEntity {
    id: number;
    protocolNumber: string;
    solicitationStatus: string;
    channelReception: string;
    entryMailbox: string;
    donnorName: string;
    donnorDocumentType: string;
    donnorDocumentValue: string;
    donnorRg: string;
    donnorEmail: string;
    donnorPhone: PhoneEntity;
    donnorAddresses: AddressEntity[];
    transfereeName: string;
    transfereeDocumentType: string;
    transfereeDocumentValue: string;
    transfereeEmail: string;
    transfereePhone: PhoneEntity;
    transfereeAddresses: AddressEntity[];
    solicitationAddress: AddressEntity;
    comment: string;
    userResponseDto: UserEntity;
    digitalDocuments: DigitalDocumentEntity[];
    isVisibleForEdit: Boolean;


    /* private final Long id;
	private final String solicitationStatus;
	private final String channelReception;
	private final String entryMailbox;
	private final String donnorName;
	private final String donnorDocumentType;
	private final String donnorDocumentValue;
	private final String donnorRg;
	private final String donnorEmail;
	private final PhoneDto donnorPhone;
	private final List<AddressDto> donnorAddresses;
	private final String transfereeName;
	private final String transfereeDocumentType;
	private final String transfereeDocumentValue;
	private final String transfereeEmail;
	private final PhoneDto transfereePhone;
	private final List<AddressDto> transfereeAddresses;
	private final AddressDto solicitationAddress;
	private final String comment;
	private final UserResponseDto userResponseDto;
	private final List<DigitalDocumentDto> digitalDocuments; */
}
