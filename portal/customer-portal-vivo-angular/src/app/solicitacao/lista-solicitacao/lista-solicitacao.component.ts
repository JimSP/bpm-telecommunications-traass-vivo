import { StepDto } from './../../models/step-dto';
import { MensagemGlobalService } from './../../services/mensagem-global.service';
import { BaseUtilsService } from './../../utils/base-utils.service';
import { Component, OnInit, ViewChild, Output, EventEmitter } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatTabChangeEvent } from '@angular/material';
import { SolicitationResponseEntity } from '../../models/solicitation-response-entity';
import { ActivatedRoute } from '@angular/router';
import { SolicitationService } from '../../services/solicitation.service';
import { ListSolicitationResponseDtos } from '../../models/list-solicitation-response-dto';
import { Mensagem } from '../../models/mensagem';
import { EnumMesageType } from '../../enumerators/enum-mesage-type.enum';
import { EnumTypeService } from '../../services/enum-type.service';
import { StepEntity } from '../../models/step-entity';
import { SolicitationStatus } from '../../enumerators/solicitation-status.enum';

@Component({
  selector: 'lista-solicitacao',
  templateUrl: './lista-solicitacao.component.html',
  styleUrls: ['./lista-solicitacao.component.css']
})
export class ListaSolicitacaoComponent implements OnInit {

  @Output() mensagemEmitter = new EventEmitter<Mensagem>();
  @Output() tabSelectedEmmiter = new EventEmitter<number>();
  @Output() solicitationEditEmitter = new EventEmitter<SolicitationResponseEntity>();
  @Output() disabledSolicitationEditEmitter = new EventEmitter<Boolean>();
  @Output() stepDtoEmitter = new EventEmitter<StepDto>();

  index: number;
  stepDto: StepDto = new StepDto();
  listSteps: StepEntity[] = [
    { id: 1, title: 'Solicitação criada', info: 'Informações do passo' },
    { id: 2, title: 'Em analise', info: 'Informações do passo andamento do analise' },
    { id: 3, title: 'Pendências', info: 'Informações do passo' },
    { id: 4, title: 'Em confirmação', info: 'Informações do passo' },
    { id: 5, title: 'Aprovado', info: 'Informações do passo' }
  ];

  solicitationResponseDtos: ListSolicitationResponseDtos = new ListSolicitationResponseDtos;
  ELEMENT_DATA: SolicitationResponseEntity[] = [];
  displayedColumns = ['id', 'donnorName', 'transfereeName', 'status', 'actionsEditors'];
  dataSource = new MatTableDataSource<SolicitationResponseEntity>(this.ELEMENT_DATA);
  userId = 0;
  globalMensagem: Mensagem = new Mensagem;
  searchSolicitation: string;
  tabSelected = 0;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private solicitationService: SolicitationService,
    private baseUtils: BaseUtilsService,
    private route: ActivatedRoute,
    private mensagemService: MensagemGlobalService,
    private enumService: EnumTypeService
  ) {
    this.userId = this.route.snapshot.params['userId'];
    console.log('USER_ID_LIST: ', this.userId);
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.loadListSolicitationByUserId();
  }

  onTabClick(event: MatTabChangeEvent) {
    console.log('TESTE TAB CLICK');
    this.loadListSolicitationByUserId();
  }

  mensagemReceiver(e) {
    console.log('MSG RECEBIDA >>>>>>>>>>>', e);
    this.globalMensagem = e;
  }

  onSearchSolicitatio() {
    this.solicitationService.getListSolicitationByUserId(this.userId, this.globalMensagem).then(
      (success) => {
        this.solicitationResponseDtos = success;
        this.dataSource.data = new Array();
        this.solicitationResponseDtos.solicitationResponseDtos.forEach(element => {
          this.dataSource.data.push(element);
        });
        this.dataSource.paginator = this.paginator;
        if (this.dataSource.filteredData.length < 1) {
          this.mensagemService.sendMensage(EnumMesageType.WARNING, 'Não encontramos registro com essa pesquisa', this.mensagemEmitter);
        } else {
          this.mensagemService.sendMensage(EnumMesageType.SUCCESS, 'Pesquisa realizada com sucesso', this.mensagemEmitter);
        }
      }
    ).catch((error) => {
      this.mensagemService.sendMensage(EnumMesageType.WARNING, 'Registros não encontrados', this.mensagemEmitter);
    });
  }

  cleanFiler() {
    this.searchSolicitation = '';
    this.applyFilter('');
  }

  applyFilter(filterValue: string) {
    this.mensagemService.showLoader();
    this.onSearchSolicitatio();
    if (!this.baseUtils.isEmpty(filterValue)) {
      filterValue = filterValue.trim(); // Remove whitespace
      filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
      this.dataSource.filter = filterValue;
    } else {
      this.dataSource.filter = filterValue;
    }
  }

  loadListSolicitationByUserId() {
    this.mensagemService.onTopScrollPage();
    this.mensagemService.showLoader();
    this.solicitationService.getListSolicitationByUserId(this.userId, this.globalMensagem).then(
      (success) => {
        console.log('SUCCESS_LIST2 >>>>>>>>>', success);
        this.solicitationResponseDtos = success;
        this.solicitationResponseDtos.solicitationResponseDtos.forEach(element => {
          this.dataSource.data.push(element);
        });
        // this.mensagemService.sendMensage(EnumMesageType.SUCCESS, 'Solicitações encontrada com sucesso', this.mensagemEmitter);
        this.mensagemService.hideLoader();
        this.dataSource.paginator = this.paginator;
      }
    ).catch((error) => {
      this.mensagemService.hideLoader();
      this.mensagemService.sendMensage(EnumMesageType.WARNING, 'As solicitações não foram encontradas', this.mensagemEmitter);
    });
  }

  editSolicitation(solicit: any) {
    console.log('SOLICITATION : ', solicit);
    this.mensagemService.showLoader();
    this.solicitationService.getSolicitationBySolicitationId(solicit.id, this.globalMensagem)
      .then((success) => {
        console.log('SOLICITATION RESPOSEN: ', success);
        solicit = success;
        this.tabSelected = 2;
        this.tabSelectedEmmiter.emit(this.tabSelected);
        this.solicitationEditEmitter.emit(solicit);
        this.mensagemService.hideLoader();
        this.disabledSolicitationEditEmitter.emit(this.onDidabledSolicitationEdit(solicit));
      }).catch(() => {
        this.mensagemService.hideLoader();
        this.mensagemService.sendMensage(EnumMesageType.ERROR, 'Erro ao recuperar solicitação', this.mensagemEmitter);
      });

  }

  onDidabledSolicitationEdit(solicit: SolicitationResponseEntity): Boolean {
    if (!this.baseUtils.isEmpty(solicit.solicitationStatus) &&
        solicit.solicitationStatus !== SolicitationStatus.SOLICITATION_STATUS_OPEN &&
        solicit.solicitationStatus !== SolicitationStatus.SOLICITATION_STATUS_PENDING) {
      return true;
    }
    return false;
  }

  getSolicitationStatusLabel(statusId: string) {
    return this.enumService.getSolicitationStatusLabel(statusId);
  }

  stepsSolicitation(solicit: SolicitationResponseEntity) {
    this.mensagemService.showLoader();
    this.solicitationService.getSolicitationBySolicitationId(solicit.id, this.globalMensagem)
      .then((ss) => {
        console.log('SOLICITATION RESPOSEN: ', ss);
        solicit = ss;
        this.solicitationService.getListSolicitationFindLogById(solicit.id, this.globalMensagem)
          .then((success) => {
            console.log('SUCCESS LOG>>>>>>>>', success);
            if (success.solicitationResponseDtos.length > 0) {
            success.solicitationResponseDtos.push(solicit);
            this.stepDto.listSteps = this.solicitationService.changeListSteps(success);
            this.index = this.stepDto.listSteps.length;
            this.stepDtoEmitter.emit(this.stepDto);
            this.tabSelected = 3;
            this.tabSelectedEmmiter.emit(this.tabSelected);
            this.solicitationEditEmitter.emit(solicit);
            this.mensagemService.hideLoader();
            } else {
              this.mensagemService.hideLoader();
              this.mensagemService.sendMensage(EnumMesageType.WARNING,
                'Não encontramos status para essa solicitação', this.mensagemEmitter);
            }
          }).catch((error) => {
            this.mensagemService.hideLoader();
            this.mensagemService.sendMensage(EnumMesageType.WARNING, 'Não encontramos status para essa solicitação', this.mensagemEmitter);
          });
      }).catch(() => {
        this.mensagemService.hideLoader();
        this.mensagemService.sendMensage(EnumMesageType.ERROR, 'Erro ao recuperar solicitação', this.mensagemEmitter);
      });
  }


}
