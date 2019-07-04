import { SolicitationRequestEntity } from './../models/solicitation-request-entity';
import { Injectable } from '@angular/core';
import { SolicitationResponseEntity } from '../models/solicitation-response-entity';

@Injectable({
  providedIn: 'root'
})
export class ConverterService {

  constructor() { }

  converterSolicitationResponseToRequest(solicit: any): SolicitationRequestEntity {
    const solicitationRequest = new SolicitationRequestEntity();
    solicitationRequest.solicitationStatus = solicit.solicitationStatus;
    solicitationRequest.channelReception = solicit.channelReception;
    solicitationRequest.entryMailbox = solicit.entryMailbox;
    solicitationRequest.donnorName = solicit.donnorName;
    solicitationRequest.donnorDocumentType = solicit.donnorDocumentType;
    solicitationRequest.donnorDocumentValue = solicit.donnorDocumentValue;
    solicitationRequest.donnorRg = solicit.donnorRg;
    solicitationRequest.donnorEmail = solicit.donnorEmail;
    solicitationRequest.donnorPhone = solicit.donnorPhone;
    solicitationRequest.donnorAddresses = solicit.donnorAddresses;
    solicitationRequest.transfereeName = solicit.transfereeName;
    solicitationRequest.transfereeDocumentType = solicit.transfereeDocumentType;
    solicitationRequest.transfereeDocumentValue = solicit.transfereeDocumentValue;
    solicitationRequest.transfereeEmail = solicit.transfereeEmail;
    solicitationRequest.transfereePhone = solicit.transfereePhone;
    solicitationRequest.transfereeAddresses = solicit.transfereeAddresses;
    solicitationRequest.solicitationAddress = solicit.solicitationAddress;
    solicitationRequest.comment = solicit.comment;
    solicitationRequest.digitalDocuments = solicit.digitalDocuments;
    return solicitationRequest;
  }
}
