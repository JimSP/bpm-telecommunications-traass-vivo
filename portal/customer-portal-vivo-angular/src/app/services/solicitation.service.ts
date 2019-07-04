import { Mensagem } from './../models/mensagem';
import { BaseService } from './base.service';
import { SolicitationRequestEntity } from '../models/solicitation-request-entity';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EnumMesageType } from '../enumerators/enum-mesage-type.enum';
import { ListSolicitationResponseDtos } from '../models/list-solicitation-response-dto';
import { SolicitationResponseEntity } from '../models/solicitation-response-entity';
import { StepEntity } from '../models/step-entity';
import { MAT_SELECT_SCROLL_STRATEGY_PROVIDER_FACTORY } from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class SolicitationService {

  private url = `${environment.gatewayUrlSolicitation}/vt-solicitation-ms`;
  private urlBase = environment.gatewayUrlSolicitation;
  private urlEndPointBase = 'vt-solicitation-ms';
  private urlList = 'list';
  private urlStatus = 'status';
  private urlLog = 'log';

  constructor(
    private http: HttpClient,
    private baseService: BaseService
  ) { }

  getSolicitationBySolicitationId(id, msg: Mensagem) {
    return this.http.get<SolicitationResponseEntity>(this.baseService.mountUrlByParam(this.url, id)).toPromise();
      // .then((success) => {
      //   console.log('GET_SOLICITATION-ID', success);
      //   msg.tipo = EnumMesageType.SUCCESS;
      //   msg.msg = 'Solicitação encontrada com sucesso!';
      // }).catch(() => {
      //   msg.tipo = EnumMesageType.ERROR;
      //   msg.msg = 'Solicitação não encontrada!';
      // });
  }

  getListSolicitationByListSolicitationId(ids: string[], msg: Mensagem) {
    return this.http.get<ListSolicitationResponseDtos>(this.baseService.mountUrlByIds(
      this.baseService.mountUrlByParam(this.url, this.urlList), ids)).toPromise()
      .then(() => {
        msg.tipo = EnumMesageType.SUCCESS;
        msg.msg = 'Solicitações encontradas com sucesso!';
      }).catch(() => {
        msg.tipo = EnumMesageType.ERROR;
        msg.msg = 'Solicitações não encontrada!';
      });
  }

  getListSolicitationByUserId(userId, msg: Mensagem) {
    const url = this.baseService.mountUrlByMultParams(this.urlBase, [userId, this.urlEndPointBase]);
    console.log('URL: >>>>>', url);
    return this.http.get<any>(url).toPromise();
    // .then(() => {
    //   // console.log('SUCCESS_LIST >>>>>>>>>', success);
    //   msg.tipo = EnumMesageType.SUCCESS;
    //   msg.msg = 'Solicitação encontrada com sucesso!';
    // }).catch(() => {
    //   msg.tipo = EnumMesageType.ERROR;
    //   msg.msg = 'Solicitação não encontrada!';
    // });
  }

  getListSolicitationFindLogById(id, msg: Mensagem) {
    const url = this.baseService.mountUrlByMultParams(this.url, [id, this.urlLog]);
    console.log('getListSolicitationFindLogById-URL: ', url);
    return this.http.get<ListSolicitationResponseDtos>(url).toPromise();
      // .then(() => {
      //   msg.tipo = EnumMesageType.SUCCESS;
      //   msg.msg = 'Solicitação encontrada com sucesso!';
      // }).catch(() => {
      //   msg.tipo = EnumMesageType.ERROR;
      //   msg.msg = 'Solicitação não encontrada!';
      // });
  }

  addSolicitation(solicitation: SolicitationRequestEntity, msg: Mensagem) {
    console.log('Solicitacao_JSON: ', JSON.stringify(solicitation));
    return this.http.post<SolicitationResponseEntity>(this.url, solicitation).toPromise()
      .then((success) => {
        console.log('SUCCESS >>>>>>>>', success);
        msg.tipo = EnumMesageType.SUCCESS;
        msg.msg = 'Solicitação adicionada com sucesso!';
      }).catch((error) => {
        console.log('ERROR >>>>>>>>', error);
        msg.tipo = EnumMesageType.ERROR;
        msg.msg = 'Erro ao adicionar solicitação!';
      });
  }

  editSolicitation(solicitation: SolicitationRequestEntity, solicitationId: number, msg: Mensagem) {
    console.log('Solicitacao_JSON: ', JSON.stringify(solicitation));
    solicitation.entryMailbox = '12345678';
    solicitation.channelReception = '3';
    delete solicitation.solicitationStatus;
    solicitation = this.onNormalizeSolicitationId(solicitation);
    return this.http.put<any>(`${this.url}/${solicitationId}`, solicitation).toPromise()
      .then((success) => {
        console.log('SUCCESS >>>>>>>>', success);
        msg.tipo = EnumMesageType.SUCCESS;
        msg.msg = 'Solicitação editada com sucesso!';
      }).catch((error) => {
        console.log('ERROR >>>>>>>>', error);
        msg.tipo = EnumMesageType.ERROR;
        msg.msg = 'Erro ao editar solicitação!';
      });
  }

  getListStatusByStatus(solicitationStatus, msg: Mensagem) {
    return this.http.get<string[]>(this.baseService.mountUrlByMultParams(this.url, [this.urlStatus, solicitationStatus])).toPromise()
      .then(() => {
        msg.tipo = EnumMesageType.SUCCESS;
        msg.msg = 'Lista de status encontrada com sucesso!';
      }).catch(() => {
        msg.tipo = EnumMesageType.ERROR;
        msg.msg = 'Erro ao recuperar lista de status!';
      });
  }

  getListStatusByStatusAndId(id, msg: Mensagem) {
    return this.http.get<string[]>(this.baseService.mountUrlByMultParams(this.url, [id, this.urlStatus])).toPromise()
      .then(() => {
        msg.tipo = EnumMesageType.SUCCESS;
        msg.msg = 'Lista de status encontrada com sucesso!';
      }).catch(() => {
        msg.tipo = EnumMesageType.ERROR;
        msg.msg = 'Solicitação não encontrada!';
      });
  }

  updateSolicitationByStatusAndId(solicitationStatus, id, msg: Mensagem) {
    return this.http.put<string>(this.baseService.mountUrlByMultParams(this.url, [id, this.urlStatus]), solicitationStatus).toPromise()
      .then(() => {
        msg.tipo = EnumMesageType.SUCCESS;
        msg.msg = 'Solicitação atualizada com sucesso!';
      }).catch(() => {
        msg.tipo = EnumMesageType.ERROR;
        msg.msg = 'Erro ao atualizar a solicitação!';
      });
  }

  deleteDigitalDocumentBySolicitationIdAndDIgitalDocumentId(solicitationId, digitalDocumentId, msg: Mensagem) {
    // const url = this.baseService.mountUrlByMultParams(this.url, [solicitationId, 'digitalDocument' , digitalDocumentId]);
    console.log('URL: ', `${this.url}/${solicitationId}/digitalDocument/${digitalDocumentId}`);
    return this.http.delete(`${this.url}/${solicitationId}/digitalDocument/${digitalDocumentId}`).toPromise()
      .then(() => {
        msg.tipo = EnumMesageType.SUCCESS;
        msg.msg = 'Documento deletado com sucesso!';
      }).catch(() => {
        msg.tipo = EnumMesageType.ERROR;
        msg.msg = 'Erro ao deletar docuemtno!';
      });
  }

  changeListSteps(listSolicitationDto): StepEntity[] {
    const listSteps: StepEntity[] = new Array();
    console.log('listSolicitationDto : ', listSolicitationDto);
    for (let i = 0; i < listSolicitationDto.solicitationResponseDtos.length; i++) {
      const step = new StepEntity();
      step.id = listSolicitationDto.solicitationResponseDtos[i].id;
      step.title = listSolicitationDto.solicitationResponseDtos[i].solicitationStatus;
      step.info = listSolicitationDto.solicitationResponseDtos[i].comment;
      listSteps.push(step);
    }
    return listSteps;
  }


  onNormalizeSolicitationId(solicit: SolicitationRequestEntity): SolicitationRequestEntity {
    solicit.donnorAddresses.forEach((address) => {
      delete address.id;
    });
    solicit.transfereeAddresses.forEach((address) => {
      delete address.id;
    });
    delete solicit.donnorPhone.id;
    delete solicit.transfereePhone.id;
    return solicit;
  }

}
