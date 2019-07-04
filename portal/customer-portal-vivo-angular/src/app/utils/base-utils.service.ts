import { Injectable } from '@angular/core';
import { PhoneEntity } from '../models/phone-entity';
import { PhoneType } from '../enumerators/phone-type.enum';
import { SolicitationRequestEntity } from '../models/solicitation-request-entity';
import { AddressEntity } from '../models/address-entity';
import { AddressType } from '../enumerators/address-type.enum';
import { RoadType } from '../enumerators/road-type.enum';
import { SolicitationResponseEntity } from '../models/solicitation-response-entity';
import { DigitalDocumentEntity } from '../models/digital-document-entity';
import { DateType } from '../enumerators/date-type.enum';

@Injectable({
  providedIn: 'root'
})
export class BaseUtilsService {

  constructor() { }

  isEmpty(value) {
    if (value === null || value === undefined || value === '') {
      return true;
    }
    return false;
  }

  formatDate(date: Date, mask: string) {
    let day = date.getDate().toString();
    day = this.complementeValueLeft(day, 2, '0');
    let month = (date.getMonth() + 1).toString();
    month = this.complementeValueLeft(month, 2, '0');
    console.log('MONTH>>>>>', month);
    const year = date.getFullYear();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();
    if (mask.toUpperCase() === DateType.DATE_TYPE_DD_MM_YYYY_1) {
      return `${day}/${month}/${year}`;
    }
    if (mask.toUpperCase() === DateType.DATE_TYPE_DD_MM_YYYY_2) {
      return `${day}-${month}-${year}`;
    }
    if (mask.toUpperCase() === DateType.DATE_TYPE_YYYY_MM_DD_1) {
      return `${year}/${month}/${day}`;
    }
    if (mask.toUpperCase() === DateType.DATE_TYPE_YYYY_MM_DD_2) {
      return `${year}-${month}-${day}`;
    }
    if (mask.toUpperCase() === DateType.DATE_TYPE_YYYY_MM_DD_HH_MM_SS_1) {
      return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    }
    if (mask.toUpperCase() === DateType.DATE_TYPE_YYYY_MM_DD_HH_MM_SS_2) {
      return `${day}-${month}-${year} ${hours}:${minutes}:${seconds}`;
    }
    if (mask.toUpperCase() === DateType.DATE_TYPE_YYYYMMDDHHMMSS) {
      return `${year}${month}${day}${hours}${minutes}${seconds}`;
    }
    if (mask.toUpperCase() === DateType.DATE_TYPE_DDMMYYYYHHMMSS) {
      return `${day}${month}${year}${hours}${minutes}${seconds}`;
    }
  }

  complementeValueLeft(valor: string, size: number, complement: string) {
    for (let i = valor.length; i < size; i++) {
      valor = `${complement}${valor}`;
    }
    return valor.toString();
  }

  complementeValueRigth(valor, size: number, complement: string) {
    for (let i = valor.length; i < size; i++) {
      valor = `${valor}${complement}`;
    }
    return valor.toString();
  }

  copyDigitalDocument(digitalDocument: DigitalDocumentEntity): DigitalDocumentEntity {
    const document = new DigitalDocumentEntity();
    document.data = digitalDocument.data;
    document.documentType = digitalDocument.documentType;
    document.referenceName = digitalDocument.referenceName;
    document.url = digitalDocument.url;
    return document;
  }

  initialSolicitationRequest(solicitation: SolicitationRequestEntity) {
    solicitation.donnorDocumentType = 'Cpf';
    solicitation.donnorPhone = new PhoneEntity();
    solicitation.donnorPhone.phoneType = PhoneType.PHONE_TELEFONE_CONTATO_ID;
    solicitation.donnorAddresses = new Array();
    const donnorAddressInitial = new AddressEntity;
    donnorAddressInitial.addressType = AddressType.ENDERECO_CORRESPONDENCIA_ID;
    solicitation.donnorAddresses.push(donnorAddressInitial);

    solicitation.transfereePhone = new PhoneEntity();
    solicitation.transfereePhone.phoneType = PhoneType.PHONE_TELEFONE_CONTATO_ID;
    solicitation.transfereeAddresses = new Array();
    const transfereeAddressInitial = new AddressEntity;
    transfereeAddressInitial.addressType = AddressType.ENDERECO_CORRESPONDENCIA_ID;
    solicitation.transfereeAddresses.push(transfereeAddressInitial);
    const solicitationAddressInitial = new AddressEntity;
    solicitationAddressInitial.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    solicitation.solicitationAddress = solicitationAddressInitial;
    solicitation.digitalDocuments = new Array();
    // solicitation.digitalDocuments.push(new DigitalDocumentEntity());
  }

  initialSolicitationResponse(solicitation: SolicitationResponseEntity) {
    solicitation.donnorDocumentType = 'Cpf';
    solicitation.donnorPhone = new PhoneEntity();
    solicitation.donnorPhone.phoneType = PhoneType.PHONE_TELEFONE_CONTATO_ID;
    solicitation.donnorAddresses = new Array();
    const donnorAddressInitial = new AddressEntity;
    donnorAddressInitial.addressType = AddressType.ENDERECO_CORRESPONDENCIA_ID;
    solicitation.donnorAddresses.push(donnorAddressInitial);

    solicitation.transfereePhone = new PhoneEntity();
    solicitation.transfereePhone.phoneType = PhoneType.PHONE_TELEFONE_CONTATO_ID;
    solicitation.transfereeAddresses = new Array();
    const transfereeAddressInitial = new AddressEntity;
    transfereeAddressInitial.addressType = AddressType.ENDERECO_CORRESPONDENCIA_ID;
    solicitation.transfereeAddresses.push(transfereeAddressInitial);
    const solicitationAddressInitial = new AddressEntity;
    solicitationAddressInitial.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    solicitation.solicitationAddress = solicitationAddressInitial;
    solicitation.digitalDocuments = new Array();
    // solicitation.digitalDocuments.push(new DigitalDocumentEntity());
  }


  // ================== REMOVER MOCK =========================
  newSolicitationRequestMockTest(solicitation: SolicitationRequestEntity) {
    this.initialSolicitationRequest(solicitation);
    const addressTesteMockDonnor = new AddressEntity;
    addressTesteMockDonnor.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    addressTesteMockDonnor.zipCode = '12345678';
    addressTesteMockDonnor.roadType = RoadType.TIPO_LOGRADOURO_AVENIDA_ID;
    addressTesteMockDonnor.streetName = 'Princesa Januária 2';
    addressTesteMockDonnor.city = 'São Bernardo do Campo';
    addressTesteMockDonnor.streetNumber = 46;
    addressTesteMockDonnor.complement = 'T2 Ap17';
    addressTesteMockDonnor.province = 'SP';
    addressTesteMockDonnor.neighborhood = 'Nova petrópolis';
    addressTesteMockDonnor.country = 'Brasil';

    const addressTesteMockTransferee = new AddressEntity;
    addressTesteMockTransferee.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    addressTesteMockTransferee.zipCode = '12345678';
    addressTesteMockTransferee.roadType = RoadType.TIPO_LOGRADOURO_AVENIDA_ID;
    addressTesteMockTransferee.streetName = 'Princesa Januária 3';
    addressTesteMockTransferee.city = 'São Bernardo do Campo';
    addressTesteMockTransferee.streetNumber = 46;
    addressTesteMockTransferee.complement = 'T2 Ap17';
    addressTesteMockTransferee.province = 'SP';
    addressTesteMockTransferee.neighborhood = 'Nova petrópolis';
    addressTesteMockTransferee.country = 'Brasil';

    const addressTesteMockSolicitation = new AddressEntity;
    addressTesteMockSolicitation.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    addressTesteMockSolicitation.zipCode = '12345678';
    addressTesteMockSolicitation.roadType = RoadType.TIPO_LOGRADOURO_AVENIDA_ID;
    addressTesteMockSolicitation.streetName = 'Princesa Januária 4';
    addressTesteMockSolicitation.city = 'São Bernardo do Campo';
    addressTesteMockSolicitation.streetNumber = 46;
    addressTesteMockSolicitation.complement = 'T2 Ap17';
    addressTesteMockSolicitation.province = 'SP';
    addressTesteMockSolicitation.neighborhood = 'Nova petrópolis';
    addressTesteMockSolicitation.country = 'Brasil';

    const phoneTestMock = new PhoneEntity;
    phoneTestMock.areaCode = 11;
    phoneTestMock.countryCode = 55;
    phoneTestMock.phoneNumber = 949493227;
    phoneTestMock.phoneType = PhoneType.PHONE_TELEFONE_RESIDENCIA_ID;

    // const digitalDocumentTestMock = new DigitalDocumentEntity;
    // digitalDocumentTestMock.data = 'f39/WH9/YAAQSkZJRgA=';
    // digitalDocumentTestMock.referenceName = 'teste.pdf';
    // digitalDocumentTestMock.url = 'teste.link';
    // solicitation.digitalDocuments = new Array;
    // solicitation.digitalDocuments.push();

    solicitation.channelReception = '3';
    solicitation.entryMailbox = '12345678';

    solicitation.donnorName = 'Lucas Mantovani';
    solicitation.donnorDocumentType = 'Cpf';
    solicitation.donnorDocumentValue = '35541516021';
    solicitation.donnorRg = '123456789';
    solicitation.donnorPhone = phoneTestMock;
    solicitation.donnorAddresses[0] = addressTesteMockDonnor;

    solicitation.transfereeName = 'José da Silva';
    solicitation.transfereeDocumentType = 'Cpf';
    solicitation.transfereeDocumentValue = '94881142070';
    solicitation.transfereePhone = phoneTestMock;
    solicitation.transfereeAddresses[0] = addressTesteMockTransferee;
    solicitation.solicitationAddress = addressTesteMockSolicitation;
  }
  // ================== REMOVER MOCK =========================


  // ================== REMOVER MOCK =========================
  newSolicitationResponseMockTest(solicitation: SolicitationResponseEntity) {
    this.initialSolicitationResponse(solicitation);
    const addressTesteMockDonnor = new AddressEntity;
    addressTesteMockDonnor.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    addressTesteMockDonnor.zipCode = '12345678';
    addressTesteMockDonnor.roadType = RoadType.TIPO_LOGRADOURO_AVENIDA_ID;
    addressTesteMockDonnor.streetName = 'Princesa Januária 2';
    addressTesteMockDonnor.city = 'São Bernardo do Campo';
    addressTesteMockDonnor.streetNumber = 46;
    addressTesteMockDonnor.complement = 'T2 Ap17';
    addressTesteMockDonnor.province = 'SP';
    addressTesteMockDonnor.neighborhood = 'Nova petrópolis';
    addressTesteMockDonnor.country = 'Brasil';

    const addressTesteMockTransferee = new AddressEntity;
    addressTesteMockTransferee.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    addressTesteMockTransferee.zipCode = '12345678';
    addressTesteMockTransferee.roadType = RoadType.TIPO_LOGRADOURO_AVENIDA_ID;
    addressTesteMockTransferee.streetName = 'Princesa Januária 3';
    addressTesteMockTransferee.city = 'São Bernardo do Campo';
    addressTesteMockTransferee.streetNumber = 46;
    addressTesteMockTransferee.complement = 'T2 Ap17';
    addressTesteMockTransferee.province = 'SP';
    addressTesteMockTransferee.neighborhood = 'Nova petrópolis';
    addressTesteMockTransferee.country = 'Brasil';

    const addressTesteMockSolicitation = new AddressEntity;
    addressTesteMockSolicitation.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    addressTesteMockSolicitation.zipCode = '12345678';
    addressTesteMockSolicitation.roadType = RoadType.TIPO_LOGRADOURO_AVENIDA_ID;
    addressTesteMockSolicitation.streetName = 'Princesa Januária 4';
    addressTesteMockSolicitation.city = 'São Bernardo do Campo';
    addressTesteMockSolicitation.streetNumber = 46;
    addressTesteMockSolicitation.complement = 'T2 Ap17';
    addressTesteMockSolicitation.province = 'SP';
    addressTesteMockSolicitation.neighborhood = 'Nova petrópolis';
    addressTesteMockSolicitation.country = 'Brasil';

    const phoneTestMock = new PhoneEntity;
    phoneTestMock.areaCode = 11;
    phoneTestMock.countryCode = 55;
    phoneTestMock.phoneNumber = 949493227;
    phoneTestMock.phoneType = PhoneType.PHONE_TELEFONE_CONTATO_ID;

    // const digitalDocumentTestMock = new DigitalDocumentEntity;
    // digitalDocumentTestMock.data = 'f39/WH9/YAAQSkZJRgA=';
    // digitalDocumentTestMock.referenceName = 'teste.pdf';
    // digitalDocumentTestMock.url = 'teste.link';
    solicitation.digitalDocuments = new Array;
    // solicitation.digitalDocuments.push(digitalDocumentTestMock);

    solicitation.channelReception = '3';
    solicitation.entryMailbox = '12345678';

    solicitation.donnorName = 'Lucas Mantovani';
    solicitation.donnorDocumentType = 'Cpf';
    solicitation.donnorDocumentValue = '32472087802';
    solicitation.donnorRg = '123456789';
    solicitation.donnorPhone = phoneTestMock;
    solicitation.donnorAddresses[0] = addressTesteMockDonnor;

    solicitation.transfereeName = 'José da Silva';
    solicitation.transfereeDocumentType = 'Cpf';
    solicitation.transfereeDocumentValue = '94881142070';
    solicitation.transfereePhone = phoneTestMock;
    solicitation.transfereeAddresses[0] = addressTesteMockTransferee;

    solicitation.solicitationAddress = addressTesteMockSolicitation;
    return solicitation;
  }
  // ================== REMOVER MOCK =========================

  downloadFile(imgDocument) {

    const ext = imgDocument.fileNameX.split('.');
    // var ext = ['.PNG', '.pdf'];

    const a = document.createElement('a');

    const binary_string = atob(imgDocument.data);
    const len = binary_string.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
      bytes[i] = binary_string.charCodeAt(i);
    }

    const file = new Blob([bytes.buffer], { type: ext[1] });
    const fileName = ext[0].toLowerCase() + '.' + ext[1];

    if (window.navigator.msSaveOrOpenBlob) {
      window.navigator.msSaveOrOpenBlob(file, fileName);
    } else { // Others
      const url = URL.createObjectURL(file);
      a.href = url;
      a.target = '_blank';
      // if(ext[1].toLowerCase() == 'xlsx'){
      a.download = fileName;
      // }
      document.body.appendChild(a);
      a.click();

      setTimeout(function () {
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      }, 200);
    }
  }

  validEmail(email) {
    return /^[\w+.]+@\w+\.\w{2,}(?:\.\w{2})?$/.test(email);
  }

  // loadAttach(imgDocument) {

  //   byte[] data = Convert.FromBase64String("GQ8XQAYFAiEMfN0qD0COTgMX");

  //   const fileSize = document.getElementById('inputImg');

  //   if (fileSize > 2621440) { // MAX_FILE_SIZE = 2621440 Bytes
  //       // $translate('validate.error.max-size').then(function (texto) {
  //       //     WcAlertConsoleService.addMessage({
  //       //         message: texto,
  //       //         type: 'danger'
  //       //     });
  //       // });
  //       window.scrollTo(0, 0);
  //   } else {

  //     const preview = document.getElementById('fileView');
  //     const file = document.getElementById('inputImg').files[0];

  //       if (file.name != null) {
  //         const reader = new FileReader();
  //           imgDocument.fileNameX = file.name;
  //           reader.addEventListener('load', function () {
  //               imgDocument.data = reader.result;

  //           }, false);
  //           reader.readAsDataURL(file);
  //       }

  //   }
  // }

}
