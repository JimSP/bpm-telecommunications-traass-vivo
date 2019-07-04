import { BaseUtilsService } from './../utils/base-utils.service';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserEntity } from '../models/user-entity';
import { UserService } from '../services/user.service';
import { MensagemGlobalService } from '../services/mensagem-global.service';
import { Mensagem } from '../models/mensagem';
import { AuthUserRequestDto } from '../models/auth-user-request-dto';
import { EnumMesageType } from '../enumerators/enum-mesage-type.enum';
import { environment } from '../../environments/environment';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  @Output() userLoggedInEmitter = new EventEmitter<UserEntity>();

  globalMensagem: Mensagem = new Mensagem();

  userLoggedIn: UserEntity;
  userAuth: AuthUserRequestDto = new AuthUserRequestDto();

  constructor(
    private userService: UserService,
    private mesageService: MensagemGlobalService,
    private baseUtils: BaseUtilsService
  ) { }

  ngOnInit() {
    if (environment.gatewayUrl === 'http://localhost:8091') {
      this.userAuth.email = 'lamantovani@gmail.com';
      this.userAuth.password = '123456';
    }
  }

  // onLogin(userAuth: AuthUserRequestDto) {
  //   if (this.validationLogin(userAuth) === 'OK') {
  //     this.userService.onAuthentication(userAuth)
  //       .then((success) => {
  //         this.userLoggedIn = success;

  //         // delete this.userLoggedIn.password;
  //         // localStorage.setItem('userLoggedIn', JSON.stringify(this.userLoggedIn));
  //         // const userLocalStorage = JSON.parse(localStorage.getItem('userLoggedIn'));

  //         this.userLoggedInEmitter.emit(this.userLoggedIn);
  //         this.sendLocalMessage(EnumMesageType.SUCCESS, 'Usuário encontrado com sucesso');
  //         this.router.navigate(['/solicitacao', this.userLoggedIn.id]);
  //       })
  //       .catch((erro) => {
  //         this.sendLocalMessage(EnumMesageType.ERROR, 'Usuário não encontrado ou usuário e senha pode estar errado!');
  //       });
  //   } else {
  //     this.sendLocalMessage(EnumMesageType.ERROR, this.validationLogin(userAuth));
  //   }
  // }

  onLogin(userAuth: AuthUserRequestDto) {
    if (this.validationLogin(userAuth) === 'OK') {
      this.userService.onAuthenticationBeta(userAuth, this.userLoggedIn, this.globalMensagem);
      this.sendLocalMessage(this.globalMensagem.tipo, this.globalMensagem.msg);
    } else {
      this.sendLocalMessage(EnumMesageType.ERROR, this.validationLogin(userAuth));
    }
  }

  validationLogin(userAuth: AuthUserRequestDto) {
    if (userAuth.email === null || userAuth.email === undefined) {
      return 'Por favor, entre com o seu e-mail!';
    } else if (!this.baseUtils.validEmail(userAuth.email)) {
      return 'Por favor, verifique seu e-mail';
    }
    if (userAuth.password === null || userAuth.password === undefined) {
      return 'Por favor, entre com sua senha!';
    }

    return 'OK';
  }

sendLocalMessage(type: string, msg: string) {
  this.mesageService.sendLocalMensage(type, msg, this.globalMensagem);
  setTimeout(() => {
    this.globalMensagem = new Mensagem();
  }, 10000);
}

}
