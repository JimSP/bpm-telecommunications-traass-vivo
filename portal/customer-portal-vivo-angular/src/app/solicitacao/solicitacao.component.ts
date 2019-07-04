import { StepDto } from './../models/step-dto';
import { ConverterService } from './../converters/converter.service';
import { UserService } from './../services/user.service';
import { Mensagem } from './../models/mensagem';
import { Component, OnInit, ViewChild, ElementRef, Input, Output, EventEmitter } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material';
import { UserEntity } from '../models/user-entity';
import { ActivatedRoute } from '@angular/router';
import { SolicitationRequestEntity } from '../models/solicitation-request-entity';
import { StepEntity } from '../models/step-entity';
import { SolicitationStatus } from '../enumerators/solicitation-status.enum';

@Component({
  selector: 'solicitacao',
  templateUrl: './solicitacao.component.html',
  styleUrls: ['./solicitacao.component.css']
})
export class SolicitacaoComponent implements OnInit {

  // @ViewChild('tabSolicitation') tabSolicitation: ElementRef;

  @Input() globalMensagem: Mensagem = new Mensagem();

  @Input() tabSelected = 0;

  @Output() userLoggedInEmitter = new EventEmitter<UserEntity>();

  @Input() index: number;

  solicitationId: number;
  solicitation: SolicitationRequestEntity;
  stepDto: StepDto = new StepDto();
  userLoggedIn: UserEntity = new UserEntity();
  userId = 0;

  public btDeleteDisabled = true;
  public disabledSolicitationEdit = false;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private converterService: ConverterService
  ) {
    this.userId = this.route.snapshot.params['userId'];
    console.log('USER_ID: ', this.userId);
    this.userLoggedIn.id = this.userId;
  }

  mensagemEmitter(e) {
    this.globalMensagem.tipo = e.tipo;
    this.globalMensagem.msg = e.msg;
  }

  tabSelectedEmitter(e) {
    this.tabSelected = e;
  }

  solicitationEditReceiver(e) {
    this.solicitationId = e.id;
    this.solicitation = this.converterService.converterSolicitationResponseToRequest(e);
  }

  disabledSolicitationEditReceiber(e) {
    console.log('disabledSolicitationEditReceiber RECEBIDO', e);
    this.disabledSolicitationEdit = e;
  }

  stepDtoReceiver(e) {
    this.stepDto = e;
    this.stepDto.index = this.stepDto.listSteps.length - 1;
    this.index = this.stepDto.index;
    console.log('INDEX RECEBIDO:', this.index);
  }

  onTabClick(event: MatTabChangeEvent) {
    this.tabSelected = event.index;
    if (this.tabSelected === 1) {
      this.btDeleteDisabled = true;
      this.disabledSolicitationEdit = false;
    } else {
      this.btDeleteDisabled = false;
    }
  }

  onTabSelected(tabIndex: number) {
    this.tabSelected = tabIndex;
  }

  ngOnInit() {
    this.userService.getUserById(this.userId).then((succes) => {
      this.userLoggedIn = succes;
    });
  }

}
