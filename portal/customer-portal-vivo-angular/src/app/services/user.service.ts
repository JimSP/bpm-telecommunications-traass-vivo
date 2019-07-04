import { BaseService } from './base.service';
import { Mensagem } from './../models/mensagem';
import { UserEntity } from './../models/user-entity';
import { Injectable } from '@angular/core';
import { environment } from './../../environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { AuthUserRequestDto } from '../models/auth-user-request-dto';
import { Router } from '@angular/router';
import { EnumMesageType } from '../enumerators/enum-mesage-type.enum';
import { ListUserResponseDto } from '../models/list-user-respponse-dto';
import { RecoveryEmailDto } from '../models/recovery-email-dto';
import { DocumentType } from '../enumerators/document-type.enum';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url = `${environment.gatewayUrl}/vt-user-ms`;
  private urlAuth = `${environment.gatewayUrl}/vt-user-ms/auth`;

  constructor(
    private router: Router,
    private http: HttpClient,
    private baseService: BaseService
  ) { }

  configUrl = 'assets/config.json';

  getConfig() {
    return this.http.get(this.configUrl);
  }

  addUserTest(user: UserEntity) {
    return this.http.post<UserEntity>(this.url, user).toPromise();
  }

  onAuthentication(userAuth: AuthUserRequestDto) {
    return this.http.post<UserEntity>(this.urlAuth, userAuth).toPromise();
  }

  onAuthenticationBeta(userAuth: AuthUserRequestDto, userLoggedIn: UserEntity, msg: Mensagem) {
    return this.http.post<UserEntity>(this.urlAuth, userAuth).toPromise()
      .then((success) => {
        userLoggedIn = success;
        this.router.navigate(['/solicitacao', userLoggedIn.id]);
      })
      .catch((erro) => {
        console.log('ERROR:>>>>>>', erro);
        msg.tipo = EnumMesageType.ERROR;
        msg.msg = 'E-mail e/ou senha incorreto(s)!';
      });
  }

  getUserById(id) {
    return this.http.get<UserEntity>(`${this.url}/${id}`).toPromise();
  }

  getUserByEmail(email) {
    return this.http.get<ListUserResponseDto>(`${this.url}?email=${email}`).toPromise();
  }

  getLitUserByListId(listId: string[]) {
    return this.http.get(this.baseService.mountUrlByIds(this.url, listId)).subscribe(res => res);
  }

  addUser(user, mesage: Mensagem) {
    console.log('NEW_USER', JSON.stringify(user));
    return this.http.post<UserEntity>(this.url, user).toPromise()
    .then((success) => {
      mesage.tipo = EnumMesageType.SUCCESS;
      mesage.msg = `Cadastro criado com sucesso!`;
    })
    .catch((error) => {
      console.log('ERROR: >>>>>>> ', error);
      if (error.status === 409) {
        mesage.tipo = EnumMesageType.ERROR;
        mesage.msg = 'Registro duplicado!';
      } else {
        mesage.tipo = EnumMesageType.ERROR;
        mesage.msg = `Ocorreu um erro com o servidor, por favor tente mais tarde.`;
      }
    });
  }

  updateUser(user) {
    console.log('USER-UPDATE:', JSON.stringify(user));
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.http.put<any>(this.baseService.mountUrlByParam(this.url, user.id), this.onNormalizeUserId(user), {headers}).toPromise();
  }

  deleteUser(id) {
    return this.http.delete(id).subscribe(res => res);
  }

  onChangeUserLoggedById(userId: number): any {
    this.getUserById(userId).then((success) => {
      let user = new UserEntity();
      return user = success;
    });
  }

  recoveryPassword(userId: number, mesage: Mensagem) {
    return this.http.post<any>(`${this.urlAuth}/${userId}`, userId).toPromise()
    .then(() => {
      mesage.tipo = EnumMesageType.SUCCESS;
      mesage.msg = 'E-mail de recuperação enviado com sucesso';
    }).catch((error) => {
      console.log('ERROR:', error);
      mesage.tipo = EnumMesageType.ERROR;
      mesage.msg = 'Erro ao enviar e-mail de recuperação!';
    });
  }

  recoveryPasswordByEmail(recorevyEmail: RecoveryEmailDto, mesage: Mensagem) {
    return this.http.post<RecoveryEmailDto>(`${this.urlAuth}/email/${recorevyEmail.email}`, recorevyEmail).toPromise()
    .then(() => {
      mesage.tipo = EnumMesageType.SUCCESS;
      mesage.msg = 'E-mail de recuperação enviado com sucesso';
    }).catch((error) => {
      console.log('ERROR:', error);
      if (error.status === 404) {
        mesage.tipo = EnumMesageType.WARNING;
        mesage.msg = 'E-mail não encontrado!';
      } else {
        mesage.tipo = EnumMesageType.ERROR;
        mesage.msg = 'Erro ao enviar e-mail de recuperação!';
      }
    });
  }

  onNormalizeUserId(user: UserEntity): UserEntity {
    user.addresses.forEach((address) => {
      delete address.id;
    });
    user.phones.forEach((phone) => {
      delete phone.id;
    });
    return user;
  }

  // http://localhost:8091/validator/cnpj/42229147000101
  getValidatorCpfOrCnpj(documentType: string, dsocumentValue: string) {
    return this.http.get<Boolean>(`${environment.gatewayUrl}/validator/${documentType}/${dsocumentValue}`).toPromise();
  }

  validatorCnpj(user: any) {
    console.log('USER', user);
    return this.getValidatorCpfOrCnpj(user.documentType.toLowerCase(), user.documentValue)
    .then((result) => {
      console.log('RESULT_VALIDARO_CNPJ', result);
      if (user.documentType === DocumentType.DOCUMENT_TYPE_CNPJ) {
        if (!result) {
          return 'O CNPJ pode estar errado';
        }
      }
      return 'OK';
    });
  }

  onVerifyEmail(token: string) {
    console.log('VERIFY_EMAIL_URL: ', `${this.url}/verified` , 'TOKEN: ', token);
    return this.http.put(`${this.url}/verified`, token).toPromise();
  }

}
