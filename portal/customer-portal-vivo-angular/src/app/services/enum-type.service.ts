import { Injectable } from '@angular/core';
import { AddressType } from '../enumerators/address-type.enum';
import { PhoneType } from '../enumerators/phone-type.enum';
import { RoadType } from '../enumerators/road-type.enum';
import { SolicitationStatus } from '../enumerators/solicitation-status.enum';

@Injectable({
  providedIn: 'root'
})
export class EnumTypeService {

  listAddressType = [
    { 'id': AddressType.ENDERECO_PESSOA_FISIACA_ID, 'value': AddressType.ENDERECO_PESSOA_FISIACA },
    { 'id': AddressType.ENDERECO_PESSOA_JURIDICA_ID, 'value': AddressType.ENDERECO_PESSOA_JURIDICA },
    { 'id': AddressType.ENDERECO_INSTALACAO_ID, 'value': AddressType.ENDERECO_INSTALACAO },
    { 'id': AddressType.ENDERECO_CORRESPONDENCIA_ID, 'value': AddressType.ENDERECO_CORRESPONDENCIA }
  ];

  listPhoneType = [
    { 'id': PhoneType.PHONE_CELULAR_ID, 'value': PhoneType.PHONE_CELULAR },
    { 'id': PhoneType.PHONE_TELEFONE_CONTATO_ID, 'value': PhoneType.PHONE_TELEFONE_CONTATO },
    { 'id': PhoneType.PHONE_TELEFONE_TRABALHO_ID, 'value': PhoneType.PHONE_TELEFONE_TRABALHO },
    { 'id': PhoneType.PHONE_TELEFONE_RESIDENCIA_ID, 'value': PhoneType.PHONE_TELEFONE_RESIDENCIA }
  ];

  listRoadType = [
    { 'id': RoadType.TIPO_LOGRADOURO_RUA_ID, 'value': RoadType.TIPO_LOGRADOURO_RUA },
    { 'id': RoadType.TIPO_LOGRADOURO_AVENIDA_ID, 'value': RoadType.TIPO_LOGRADOURO_AVENIDA },
    { 'id': RoadType.TIPO_LOGRADOURO_VIELA_ID, 'value': RoadType.TIPO_LOGRADOURO_VIELA },
    { 'id': RoadType.TIPO_LOGRADOURO_ESQUINA_ID, 'value': RoadType.TIPO_LOGRADOURO_ESQUINA },
    { 'id': RoadType.TIPO_LOGRADOURO_PERIMETRAL_ID, 'value': RoadType.TIPO_LOGRADOURO_PERIMETRAL },
    { 'id': RoadType.TIPO_LOGRADOURO_PRACA_ID, 'value': RoadType.TIPO_LOGRADOURO_PRACA },
    { 'id': RoadType.TIPO_LOGRADOURO_TREVO_ID, 'value': RoadType.TIPO_LOGRADOURO_TREVO },
    { 'id': RoadType.TIPO_LOGRADOURO_ROTATORIA_ID, 'value': RoadType.TIPO_LOGRADOURO_ROTATORIA }
  ];

  listSolicitationStatusType = [
    { 'id': SolicitationStatus.SOLICITATION_STATUS_OPEN, 'value': SolicitationStatus.SOLICITATION_STATUS_OPEN_LABEL },
    { 'id': SolicitationStatus.SOLICITATION_STATUS_PENDING, 'value': SolicitationStatus.SOLICITATION_STATUS_PENDING_LABEL },
    { 'id': SolicitationStatus.SOLICITATION_STATUS_IN_ANALISIS, 'value': SolicitationStatus.SOLICITATION_STATUS_IN_ANALISIS_LABEL },
    { 'id': SolicitationStatus.SOLICITATION_STATUS_WITH_PROBLEM, 'value': SolicitationStatus.SOLICITATION_STATUS_WITH_PROBLEM_LABEL },
    { 'id': SolicitationStatus.SOLICITATION_STATUS_IN_CONFIGURATION,
    'value': SolicitationStatus.SOLICITATION_STATUS_IN_CONFIGURATION_LABEL },
    { 'id': SolicitationStatus.SOLICITATION_STATUS_CLOSE, 'value': SolicitationStatus.SOLICITATION_STATUS_CLOSE_LABEL },
    { 'id': SolicitationStatus.SOLICITATION_STATUS_INVALID, 'value': SolicitationStatus.SOLICITATION_STATUS_INVALID_LABEL }
  ];

  // SOLICITATION_STATUS_OPEN = 'Open',
  // SOLICITATION_STATUS_PENDING = 'Pending',
  // SOLICITATION_STATUS_IN_ANALISIS = 'InAnalysis',
  // SOLICITATION_STATUS_WITH_PROBLEM = 'WithProblem',
  // SOLICITATION_STATUS_CLOSE = 'Close',
  // SOLICITATION_STATUS_INVALID = 'Invalid'

  getSolicitationStatusLabel(statatusId: string): string {
    for (let i = 0; i < this.listSolicitationStatusType.length; i++) {
      if (statatusId === this.listSolicitationStatusType[i].id) {
        return this.listSolicitationStatusType[i].value;
      }
    }
    return null;
  }

  constructor() { }
}
