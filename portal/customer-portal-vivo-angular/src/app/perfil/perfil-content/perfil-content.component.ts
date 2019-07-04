import { AddressEntity } from './../../models/address-entity';
import { Validar } from '../../validators/validator';
import { BaseUtilsService } from './../../utils/base-utils.service';
import { UserService } from './../../services/user.service';
import { UserEntity } from './../../models/user-entity';
import { MensagemGlobalService } from './../../services/mensagem-global.service';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Mensagem } from '../../models/mensagem';
import { EnumMesageType } from '../../enumerators/enum-mesage-type.enum';
import { Router, ActivatedRoute } from '@angular/router';
import { PhoneEntity } from '../../models/phone-entity';
import { EnumTypeService } from '../../services/enum-type.service';
import { DocumentType } from '../../enumerators/document-type.enum';
import { AddressType } from '../../enumerators/address-type.enum';
import { PhoneType } from '../../enumerators/phone-type.enum';
import { RoadType } from '../../enumerators/road-type.enum';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'perfil-content',
  templateUrl: './perfil-content.component.html',
  styleUrls: ['./perfil-content.component.css']
})
export class PerfilContentComponent implements OnInit {

  // ================== REMOVER MOCK =========================
  addressTesteMock: AddressEntity;
  phoneTestMock: PhoneEntity;
  // ================== REMOVER MOCK =========================

  @Output() mensagemEmitter = new EventEmitter<Mensagem>();

  @Output() userLoggedInEmitter = new EventEmitter<UserEntity>();

  changePassword = false;
  oldPassword: string;
  oldPasswordConfirm: string;
  newPassword: string;
  newPasswordConfirm: string;

  globalMensagem: Mensagem = new Mensagem();

  user: UserEntity = new UserEntity();
  userId = 0;

  emailFormControl = new FormControl(
    Validators.required,
    Validators.email
  );

  enderecoPessoaFisica: AddressEntity = new AddressEntity();

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private mensagemService: MensagemGlobalService,
    private enumService: EnumTypeService,
    private baseUtils: BaseUtilsService
  ) {
    this.userId = this.route.snapshot.params['userId'];
    console.log('USER_ID: ', this.userId);
  }

  listAddressType = this.enumService.listAddressType;
  listPhoneType = this.enumService.listPhoneType;
  listRoadType = this.enumService.listRoadType;

  onShowChangePassword() {
    this.oldPassword = this.user.password;
    this.changePassword = true;
  }

  cancelChangePassword() {
    this.changePassword = false;
    this.oldPasswordConfirm = '';
    this.newPassword = '';
    this.newPasswordConfirm = '';
  }

  onChangePassword() {
    this.mensagemService.onTopScrollPage();
    this.mensagemService.showLoader();
    const messageValidator = this.validatorChangePassword();
    if (messageValidator === 'OK') {
      this.user.password = this.newPassword;
      this.user.confirmPassword = this.newPasswordConfirm;
      this.changePassword = false;
      this.oldPasswordConfirm = '';
      this.newPassword = '';
      this.newPasswordConfirm = '';
      this.userService.updateUser(this.user).then(() => {
        this.mensagemService.hideLoader();
        this.mensagemService.sendMensage(EnumMesageType.INFO, 'Senha foi alterada', this.mensagemEmitter);
      }).catch(() => {
        this.mensagemService.hideLoader();
        this.mensagemService.sendMensage(EnumMesageType.ERROR, 'Erro a alterar senha', this.mensagemEmitter);
      });
    } else {
      this.mensagemService.hideLoader();
      this.mensagemService.sendMensage(EnumMesageType.ERROR, messageValidator, this.mensagemEmitter);
    }
  }

  validatorChangePassword() {
    if (this.baseUtils.isEmpty(this.oldPasswordConfirm)) {
      return 'Por favor, entre com a sua antiga senha';
    }
    if (this.baseUtils.isEmpty(this.newPassword)) {
      return 'Por favor, entre com a sua nova senha';
    }
    if (this.baseUtils.isEmpty(this.newPasswordConfirm)) {
      return 'Por favor, confirme sua nova senha';
    }
    if (this.oldPasswordConfirm !== this.oldPassword) {
      return 'A antiga senha pode estar errada';
    }
    if (this.newPasswordConfirm !== this.newPassword) {
      return 'As senhas não são iguais';
    }
    if (this.newPassword === this.oldPassword) {
      return 'Sua nova senha não pode ser a mesma que a antiga';
    }
    return 'OK';
  }

  getUsuario() {
    return this.user;
  }

  setUsuario(usuario: any) {
    this.user = usuario;
  }

  ngOnInit() {
    this.mensagemService.showLoader();
    this.newUserEntity();
    if (this.userId !== 0 && this.userId !== undefined) {
      this.userService.getUserById(this.userId).then((success) => {
        console.log('User GET >>>>>>', success);
        this.user = success;
        this.userLoggedInEmitter.emit(this.user);
        this.mensagemService.hideLoader();
      });
    } else {
      this.mensagemService.hideLoader();
    }
  }

  newUserEntity() {
    this.user = new UserEntity();
    this.user.addresses = new Array();
    this.user.phones = new Array();

    if (environment.gatewayUrl !== 'http://localhost:8091') {
      this.user.addresses.push(new AddressEntity());
      this.user.phones.push(new PhoneEntity());
    }

    // ================== REMOVER MOCK =========================
    if (environment.gatewayUrl === 'http://localhost:8091') {
      this.newUserMockTest();
    }
    // ================== REMOVER MOCK =========================
  }

  onAddAddress(listAddress: any) {
    console.log('ADD ENDERECO...');
    listAddress.push(new AddressEntity());
  }

  onRemoveAddress(listAddress: any) {
    console.log('REMOVE ENDERECO...');
    listAddress.pop();
  }

  onAddPhone(listPhone: any) {
    console.log('ADD PHONE...');
    listPhone.push(new PhoneEntity());
  }

  onRemovePhone(listPhone: any) {
    console.log('REMOVE PHONE...');
    listPhone.pop();
  }

  onSaveUser(user: any) {
    this.mensagemService.onTopScrollPage();
    this.mensagemService.showLoader();
    const messageValidator = this.validatorUser(user);
    if (messageValidator === 'OK') {
      this.userService.validatorCnpj(user)
        .then((msgCnpjValidator) => {
          console.log('msgCnpjValidator', msgCnpjValidator);
          if (msgCnpjValidator === 'OK') {
            if (user.id === null || user.id === undefined || user.id === 0) {
              this.userService.addUser(user, this.globalMensagem)
                .then(() => {
                  // this.newUserEntity();
                  this.mensagemService.hideLoader();
                  this.mensagemService.sendMensage(this.globalMensagem.tipo, this.globalMensagem.msg, this.mensagemEmitter);
                })
                .catch(() => {
                  this.mensagemService.hideLoader();
                  this.mensagemService.sendMensage(this.globalMensagem.tipo, this.globalMensagem.msg, this.mensagemEmitter);
                });
            } else {
              user.confirmPassword = user.password;
              this.userService.updateUser(user)
                .then((resp) => {
                  this.mensagemService.hideLoader();
                  this.mensagemService.sendMensage(EnumMesageType.SUCCESS,
                    `Cadastro atualizado com sucesso!`, this.mensagemEmitter);
                })
                .catch((erro) => {
                  console.log('ERROR: >>>>>>> ', erro);
                  this.mensagemService.hideLoader();
                  this.mensagemService.sendMensage(EnumMesageType.ERROR,
                    `Erro inesperado do serviço: ${erro.message}`, this.mensagemEmitter);
                });
            }
          } else {
            this.mensagemService.hideLoader();
            this.mensagemService.sendMensage(EnumMesageType.ERROR, msgCnpjValidator, this.mensagemEmitter);
          }
        });
    } else {
      this.mensagemService.hideLoader();
      this.mensagemService.sendMensage(EnumMesageType.ERROR, messageValidator, this.mensagemEmitter);
    }
  }

  validatorUser(user: any) {
    if (this.baseUtils.isEmpty(user.documentType)) {
      return 'Por favor escolha Pessoa Física ou Jurídica!';
    }
    if (user.documentType === DocumentType.DOCUMENT_TYPE_CPF) {
      if (this.baseUtils.isEmpty(user.documentValue)) {
        return 'Por favor entre com o CPF!';
      }
      if (user.documentValue.length < 11) {
        return 'O CPF deve conter 11 digitos!';
      }
      if (!Validar.validarCPF(user.documentValue)) {
        return 'O CPF pode estar errado!';
      }
    }
    if (user.documentType === DocumentType.DOCUMENT_TYPE_CNPJ) {
      if (this.baseUtils.isEmpty(user.documentValue)) {
        return 'Por favor entre com o CNPJ!';
      }
      if (user.documentValue.length !== 14) {
        return 'O CNPJ deve conter 14 digitos!';
      }
    }
    for (let i = 0; i < user.addresses.length; i++) {
      if (this.baseUtils.isEmpty(user.addresses[i].addressType)) {
        return 'Por favor entre com o Tipo de Endereço';
      }
      if (this.baseUtils.isEmpty(user.addresses[i].zipCode)) {
        return 'Por favor entre com o CEP';
      }
      if (user.addresses[i].zipCode.length !== 8) {
        return 'Por favor, o CEP deve conter 8 caracteres';
      }
      if (this.baseUtils.isEmpty(user.addresses[i].streetName)) {
        return 'Por favor entre com o Logradouro';
      }
      if (this.baseUtils.isEmpty(user.addresses[i].streetNumber)) {
        return 'Por favor entre com o Número do Endereço';
      }
      if (this.baseUtils.isEmpty(user.addresses[i].city)) {
        return 'Por favor entre com a Cidade';
      }
      if (this.baseUtils.isEmpty(user.addresses[i].province)) {
        return 'Por favor entre com o Estado';
      }
      if (this.baseUtils.isEmpty(user.addresses[i].country)) {
        return 'Por favor entre com o País';
      }
    }
    if (this.baseUtils.isEmpty(user.name)) {
      return 'Por favor entre com o Nome Completo!';
    }
    if (this.baseUtils.isEmpty(user.email)) {
      return 'Por favor entre com o E-mail!';
    }
    if (this.baseUtils.isEmpty(user.id)) {
      if (this.baseUtils.isEmpty(user.password)) {
        return 'Por favor entre com a Nova Senha';
      }
      if (this.baseUtils.isEmpty(user.confirmPassword)) {
        return 'Por favor confirme a Senha!';
      }
      if (user.confirmPassword !== user.password) {
        return 'As senhas são diferentes';
      }
    }
    for (let j = 0; j < user.phones.length; j++) {
      if (this.baseUtils.isEmpty(user.phones[j].phoneType)) {
        return 'Por favor entre o Tipo de Telefone';
      }
      if (this.baseUtils.isEmpty(user.phones[j].countryCode)) {
        return 'Por favor entre o Código do País';
      }
      if (this.baseUtils.isEmpty(user.phones[j].areaCode)) {
        return 'Por favor entre o DDD';
      }
      if (this.baseUtils.isEmpty(user.phones[j].phoneNumber)) {
        return 'Por favor entre o Número do Telefone';
      }
    }
    return 'OK';
  }

  onCancelRegister() {
    this.newUserEntity();
    this.mensagemService.sendMensage(EnumMesageType.SUCCESS, 'Cadastro cancelado com sucesso', this.mensagemEmitter);
    this.router.navigate(['/login']);
  }

  onInputOnliNumber(address: AddressEntity) {
    address.zipCode = this.onliNumber(address.zipCode);
    address.streetNumber = this.onliNumber(address.streetNumber);
  }

  onInputOnliNumberToPhone(phone: PhoneEntity) {
    phone.countryCode = this.onliNumber(phone.countryCode);
    phone.areaCode = this.onliNumber(phone.areaCode);
    phone.phoneNumber = this.onliNumber(phone.phoneNumber);
  }

  onliNumber(value) {
    if (!this.baseUtils.isEmpty(value)) {
      value = String(value);
      value = Number(value.replace(/[^\d.-]/g, ''));
    }
    return value;
  }

  onRemoveSpecialCharacterAndNumber(user: UserEntity) {
    user.name = this.onRemoveSpecialCharacter(user.name);
  }

  onRemoveSpecialCharacter(value: string) {
    const list: string[] = value.split(' ');
    let newValew = '';
    for (let i = 0; i < list.length; i++) {
      list[i] = list[i].replace(/[^a-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]/gi, '');
      if (list.length > 1 && i > 0) {
        newValew += ` ${list[i]}`;
      } else {
        newValew += list[i];
      }
    }
    return newValew;
  }

  onMaxLengthPhoneNumber(phone: PhoneEntity) {
    const value = String(phone.phoneNumber).replace(/[^\d.-]/g, '');
    if (value.length > 9) {
      phone.phoneNumber = Number(value.substring(0, 9));
    }
  }

  onMaxLengthPhoneAux(phone: PhoneEntity) {
    const country = String(phone.countryCode).replace(/[^\d.-]/g, '');
    if (country.length > 2) {
      phone.countryCode = Number(country.substring(0, 2));
    }
    const area = String(phone.areaCode).replace(/[^\d.-]/g, '');
    if (area.length > 3) {
      phone.areaCode = Number(area.substring(0, 2));
    }
  }

  // ================== REMOVER MOCK =========================
  newUserMockTest() {
    this.user.documentType = 'Cpf',
    this.user.documentValue = '43787701052',
    this.user.name = 'Lucas Mantovani',
    this.addressTesteMock = new AddressEntity();
    this.phoneTestMock = new PhoneEntity();
    this.addressTesteMock.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    this.addressTesteMock.zipCode = '12345678';
    this.addressTesteMock.roadType = RoadType.TIPO_LOGRADOURO_AVENIDA_ID;
    this.addressTesteMock.streetName = 'Princesa Januária';
    this.addressTesteMock.city = 'São Bernardo do Campo';
    this.addressTesteMock.streetNumber = 46;
    this.addressTesteMock.complement = 'T2 Ap17';
    this.addressTesteMock.province = 'SP';
    this.addressTesteMock.neighborhood = 'Nova petrópolis';
    this.user.email = 'lamantovani@gmail.com';
    this.addressTesteMock.country = 'Brasil';
    this.user.addresses.push(this.addressTesteMock);
    this.phoneTestMock.areaCode = 11;
    this.phoneTestMock.countryCode = 55;
    this.phoneTestMock.phoneNumber = 949493227;
    this.phoneTestMock.phoneType = PhoneType.PHONE_CELULAR_ID;
    this.user.phones.push(this.phoneTestMock);
    this.user.password = '123456';
    this.user.confirmPassword = '123456';
  }
  // ================== REMOVER MOCK =========================

}
