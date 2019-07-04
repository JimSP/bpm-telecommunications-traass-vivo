import { AddressEntity } from './address-entity';
import { PhoneEntity } from './phone-entity';
import { DigitalDocumentEntity } from './digital-document-entity';

export class SolicitationRequestEntity {
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
    userId: number;
    digitalDocuments: DigitalDocumentEntity[];

}
