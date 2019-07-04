import { AddressEntity } from './address-entity';
import { PhoneEntity } from './phone-entity';
export class UserEntity {
    id: number;
    name: string;
    documentType: string;
    documentValue: string;
    email: string;
    password: string;
    confirmPassword: string;
    addresses: AddressEntity[];
    phones: PhoneEntity[];
}
