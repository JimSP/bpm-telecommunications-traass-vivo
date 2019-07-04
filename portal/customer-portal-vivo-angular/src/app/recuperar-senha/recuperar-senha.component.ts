import { RecoveryEmailDto } from './../models/recovery-email-dto';
import { ListUserResponseDto } from './../models/list-user-respponse-dto';
import { BaseUtilsService } from './../utils/base-utils.service';
import { Component, OnInit } from '@angular/core';
import { Mensagem } from '../models/mensagem';
import { UserService } from '../services/user.service';
import { MensagemGlobalService } from '../services/mensagem-global.service';
import { EnumMesageType } from '../enumerators/enum-mesage-type.enum';
import { UserEntity } from '../models/user-entity';

@Component({
  selector: 'recuperar-senha',
  templateUrl: './recuperar-senha.component.html',
  styleUrls: ['./recuperar-senha.component.css']
})
export class RecuperarSenhaComponent implements OnInit {

  globalMensagem: Mensagem = new Mensagem();

  recoveryEmail: RecoveryEmailDto = new RecoveryEmailDto();
  user: UserEntity;

  constructor(
    private userService: UserService,
    private mesageService: MensagemGlobalService,
    private baseUtils: BaseUtilsService
  ) { }

  ngOnInit() {
  }

  onRecoveryPassword() {
    if (this.baseUtils.isEmpty(this.recoveryEmail.email)) {
      this.mesageService.sendLocalMensage(EnumMesageType.ERROR, 'Por favor, entre com seu e-mail!', this.globalMensagem);
      setTimeout(() => {
        this.globalMensagem = new Mensagem();
      }, 10000);
    } else {
      this.userService.recoveryPasswordByEmail(this.recoveryEmail, this.globalMensagem);
      this.mesageService.sendLocalMensage(this.globalMensagem.tipo, this.globalMensagem.msg, this.globalMensagem);
      setTimeout(() => {
        this.globalMensagem = new Mensagem();
      }, 10000);
    }
  }

}
