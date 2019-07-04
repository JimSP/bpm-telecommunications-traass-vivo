import { ValidatorCnpj } from './../../validators/validator-cnpj';
import { UserService } from './../../services/user.service';
import { BaseUtilsService } from './../../utils/base-utils.service';
import { MensagemGlobalService } from './../../services/mensagem-global.service';
import { Mensagem } from './../../models/mensagem';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { EnumMesageType } from '../../enumerators/enum-mesage-type.enum';
import { SolicitationRequestEntity } from '../../models/solicitation-request-entity';
import { PhoneEntity } from '../../models/phone-entity';
import { AddressEntity } from '../../models/address-entity';
import { DigitalDocumentEntity } from '../../models/digital-document-entity';
import { PhoneType } from '../../enumerators/phone-type.enum';
import { EnumTypeService } from '../../services/enum-type.service';
import { AddressType } from '../../enumerators/address-type.enum';
import { DocumentType } from '../../enumerators/document-type.enum';
import { SolicitationService } from '../../services/solicitation.service';
import { UserEntity } from '../../models/user-entity';
import { ActivatedRoute } from '@angular/router';
import { SolicitationResponseEntity } from '../../models/solicitation-response-entity';
import { ConverterService } from '../../converters/converter.service';
import { MatTabChangeEvent } from '@angular/material';
import { Validar } from '../../validators/validator';
import { environment } from '../../../environments/environment';
import { LoaderService } from '../../loader/loader.service';
import { SolicitationStatus } from '../../enumerators/solicitation-status.enum';

@Component({
  selector: 'nova-solicitacao',
  templateUrl: './nova-solicitacao.component.html',
  styleUrls: ['./nova-solicitacao.component.css']
})
export class NovaSolicitacaoComponent implements OnInit {

  @Input() tabSelected: number;

  @Input() btDeleteDisabled = true;

  @Input() disabledSolicitationEdit: Boolean;

  @Input() solicitation: SolicitationRequestEntity;

  @Input() solicitationId: number;

  @Input() listDigitalDocument: DigitalDocumentEntity[];

  @Output() mensagemEmitter = new EventEmitter<Mensagem>();

  @Output() tabSelectedEmmiter = new EventEmitter<number>();

  userId = 0;
  step = 0;

  globalMensagem: Mensagem = new Mensagem();
  userLoggedIn: UserEntity = new UserEntity();
  listAddressType = this.enumService.listAddressType;
  listPhoneType = this.enumService.listPhoneType;
  listRoadType = this.enumService.listRoadType;
  digitalDocument: DigitalDocumentEntity = new DigitalDocumentEntity;

  constructor(
    private route: ActivatedRoute,
    private enumService: EnumTypeService,
    private mensagemService: MensagemGlobalService,
    private baseUtils: BaseUtilsService,
    private solicitationService: SolicitationService,
    private userService: UserService,
    private converterService: ConverterService,
    private loaderService: LoaderService,
  ) {
    this.userId = this.route.snapshot.params['userId'];
    console.log('USER_ID: ', this.userId);
    this.userLoggedIn.id = this.userId;
  }

  messgeReceiver(e) {
    console.log('Mensagem recebida');
    this.globalMensagem = e;
    this.mensagemService.sendMensage(this.globalMensagem.tipo, this.globalMensagem.msg, this.mensagemEmitter);
  }

  listDigitalDocumentEmitter(e) {
    console.log('LISTA DE DOCUMENTO RECEBIDA', e);
    this.listDigitalDocument = e;
    this.solicitation.digitalDocuments = this.listDigitalDocument;
  }

  loadSolicitationEdit() {
    if (this.tabSelected === 2) {
      console.log('EDITAR SOLICITAÇÃO');
      // this.solicitation = this.converterService.converterSolicitationResponseToRequest(this.solicitationEdit);
    } else {
      console.log('NOVA SOLICITAÇÃO');
      this.newSolicitation();
    }
  }

  ngOnInit() {
    this.loadSolicitationEdit();
  }

  newSolicitation() {
    this.solicitation = new SolicitationRequestEntity();
    this.solicitation.donnorDocumentType = 'Cpf';
    this.solicitation.donnorPhone = new PhoneEntity();
    this.solicitation.donnorPhone.phoneType = PhoneType.PHONE_TELEFONE_CONTATO_ID;
    this.solicitation.donnorAddresses = new Array();
    const donnorAddressInitial = new AddressEntity;
    donnorAddressInitial.addressType = AddressType.ENDERECO_CORRESPONDENCIA_ID;
    this.solicitation.donnorAddresses.push(donnorAddressInitial);

    this.solicitation.transfereePhone = new PhoneEntity();
    this.solicitation.transfereePhone.phoneType = PhoneType.PHONE_TELEFONE_CONTATO_ID;
    this.solicitation.transfereeAddresses = new Array();
    const transfereeAddressInitial = new AddressEntity;
    transfereeAddressInitial.addressType = AddressType.ENDERECO_CORRESPONDENCIA_ID;
    this.solicitation.transfereeAddresses.push(transfereeAddressInitial);
    const solicitationAddressInitial = new AddressEntity;
    solicitationAddressInitial.addressType = AddressType.ENDERECO_INSTALACAO_ID;
    this.solicitation.solicitationAddress = solicitationAddressInitial;
    this.solicitation.channelReception = '3';
    this.solicitation.entryMailbox = '12345678';

    this.solicitation.digitalDocuments = new Array();

    this.onLoadUser(this.solicitation);
    this.solicitation.donnorEmail = this.userLoggedIn.email;
    // ================== REMOVER MOCK =========================
    if (environment.gatewayUrl === 'http://localhost:8091') {
      this.baseUtils.newSolicitationRequestMockTest(this.solicitation);
    }
    // ================== REMOVER MOCK =========================
  }

  onLoadUser(solicit: SolicitationRequestEntity) {
    this.userService.getUserById(this.userId)
      .then((success) => {
        this.userLoggedIn = success;
        solicit.userId = this.userLoggedIn.id;
        solicit.donnorEmail = this.userLoggedIn.email;
      });
  }

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  onCancelSolicitation() {
    this.newSolicitation();
    this.digitalDocument = new DigitalDocumentEntity();
    this.tabSelected = 0;
    this.tabSelectedEmmiter.emit(this.tabSelected);
  }

  onSetTabIndex(index: number) {
    this.tabSelected = index;
    this.tabSelectedEmmiter.emit(this.tabSelected);
  }

  saveSolicitacao(solicit: SolicitationRequestEntity) {
    this.mensagemService.onTopScrollPage();
    this.mensagemService.showLoader();
    if (this.onPreValidator(solicit) !== 'OK') {
      this.mensagemService.hideLoader();
      this.mensagemService.sendMensage(EnumMesageType.ERROR, this.onPreValidator(solicit), this.mensagemEmitter);
    } else {
      this.solicitationValidator(solicit).then(((msgValidator) => {
        console.log('Solicitacao', solicit);
        if (msgValidator === 'OK') {
          if (!this.baseUtils.isEmpty(this.solicitationId) && this.solicitationId > 0) {
            console.log('MODO EDIÇÃO DE SOLICITAÇÃO...', this.solicitationId);
            solicit.userId = this.userId;
            this.solicitationService.editSolicitation(solicit, this.solicitationId, this.globalMensagem).then(() => {
              this.mensagemService.sendMensage(this.globalMensagem.tipo, this.globalMensagem.msg, this.mensagemEmitter);
              solicit = new SolicitationRequestEntity();
              this.digitalDocument = new DigitalDocumentEntity();
              this.onSetTabIndex(0);
              this.mensagemService.hideLoader();
            }).catch(() => {
              this.mensagemService.hideLoader();
              this.mensagemService.sendMensage(EnumMesageType.ERROR,
                'Erro, serviço não responde corretamente, por favor tente mais tarde.', this.mensagemEmitter);
            });
          } else {
            console.log('MODO ADD SOLICITAÇÃO...');
            this.solicitationService.addSolicitation(solicit, this.globalMensagem).then(() => {
              this.mensagemService.sendMensage(this.globalMensagem.tipo, this.globalMensagem.msg, this.mensagemEmitter);
              this.newSolicitation();
              this.digitalDocument = new DigitalDocumentEntity();
              this.onSetTabIndex(0);
              this.mensagemService.hideLoader();
            }).catch(() => {
              this.mensagemService.hideLoader();
              this.mensagemService.sendMensage(EnumMesageType.ERROR,
                'Erro, serviço não responde corretamente, por favor tente mais tarde.', this.mensagemEmitter);
            });
          }
        } else {
          this.mensagemService.hideLoader();
          this.mensagemService.sendMensage(EnumMesageType.ERROR, msgValidator, this.mensagemEmitter);
        }
      }));
    }
  }

  onNormalizeSolicitationDigitalDocumentId(solicit: SolicitationRequestEntity): SolicitationRequestEntity {
    solicit.digitalDocuments.forEach((document) => {
      delete document.id;
    });
    return solicit;
  }

  onPreValidator(solicit: any) {
    if (this.baseUtils.isEmpty(solicit.donnorName)) {
      return 'Por favor entre com Nome do Cedente!';
    }

    if (this.baseUtils.isEmpty(solicit.donnorDocumentType)) {
      return 'Por favor escolha Pessoa Fisíca ou Jurídica do Cedente!';
    }

    if (solicit.donnorDocumentType === DocumentType.DOCUMENT_TYPE_CPF) {
      if (this.baseUtils.isEmpty(solicit.donnorDocumentValue)) {
        return 'Por favor entre com o CPF do Cedente!';
      } else if (!Validar.validarCPF(solicit.donnorDocumentValue)) {
        return 'O CPF do cedente pode estar errado!';
      }
    }

    if (solicit.donnorDocumentType === DocumentType.DOCUMENT_TYPE_CNPJ) {
      if (this.baseUtils.isEmpty(solicit.donnorDocumentValue)) {
        return 'Por favor entre com o CNPJ do Cedente!';
      }
    }

    if (this.baseUtils.isEmpty(solicit.donnorRg)) {
      return 'Por favor entre o RG do Cedente';
    } else if (solicit.donnorRg.length <= 0) {
      return 'O RG do Cedente pode estar errado';
    }
    //  else if (!Validar.validatorRg(solicit.donnorRg)) {
    //   return 'O RG do Cedente pode estar errado';
    // }

    if (this.baseUtils.isEmpty(solicit.donnorPhone.phoneType)) {
      return 'Por favor entre o Tipo de Telefone do Cedente';
    }

    if (this.baseUtils.isEmpty(solicit.donnorPhone.countryCode)) {
      return 'Por favor entre o Código do País do Cedente';
    }

    if (this.baseUtils.isEmpty(solicit.donnorPhone.areaCode)) {
      return 'Por favor entre o DDD do Cedente';
    }

    if (this.baseUtils.isEmpty(solicit.donnorPhone.phoneNumber)) {
      return 'Por favor entre o Número do Telefone do Cedente';
    }

    for (let i = 0; i < solicit.donnorAddresses.length; i++) {
      if (this.baseUtils.isEmpty(solicit.donnorAddresses[i].zipCode)) {
        return 'Por favor entre com o CEP do Cedente';
      }
      if (solicit.donnorAddresses[i].zipCode.length !== 8) {
        return 'Por favor, o CEP do Cedente deve conter 8 caracteres';
      }
      if (this.baseUtils.isEmpty(solicit.donnorAddresses[i].streetName)) {
        return 'Por favor entre com o Logradouro  do Cedente';
      }
      if (this.baseUtils.isEmpty(solicit.donnorAddresses[i].streetNumber)) {
        return 'Por favor entre com o Número do Endereço do Cedente';
      }
      if (this.baseUtils.isEmpty(solicit.donnorAddresses[i].city)) {
        return 'Por favor entre com a Cidade do Cedente';
      }
      if (this.baseUtils.isEmpty(solicit.donnorAddresses[i].province)) {
        return 'Por favor entre com o Estado do Cedente';
      }
      if (this.baseUtils.isEmpty(solicit.donnorAddresses[i].country)) {
        return 'Por favor entre com o País do Cedente';
      }
    }

    if (this.baseUtils.isEmpty(solicit.transfereeName)) {
      return 'Por favor entre com Nome do Cessionário!';
    }

    if (this.baseUtils.isEmpty(solicit.transfereeDocumentType)) {
      return 'Por favor escolha Pessoa Fisíca ou Jurídica do Cessionário!';
    }

    if (solicit.transfereeDocumentType === DocumentType.DOCUMENT_TYPE_CPF) {
      if (this.baseUtils.isEmpty(solicit.transfereeDocumentValue)) {
        return 'Por favor entre com o CPF do Cessionário!';
      }
      if (!Validar.validarCPF(solicit.transfereeDocumentValue)) {
        return 'O CPF do cessionário pode estar errado!';
      }
    }

    if (solicit.transfereeDocumentType === DocumentType.DOCUMENT_TYPE_CNPJ) {
      if (this.baseUtils.isEmpty(solicit.transfereeDocumentValue)) {
        return 'Por favor entre com o CNPJ do Cessionário!';
      }
    }

    if (this.baseUtils.isEmpty(solicit.transfereePhone.phoneType)) {
      return 'Por favor entre o Tipo de Telefone do Cessionário';
    }

    if (this.baseUtils.isEmpty(solicit.transfereePhone.countryCode)) {
      return 'Por favor entre o Código do País do Cessionário';
    }

    if (this.baseUtils.isEmpty(solicit.transfereePhone.areaCode)) {
      return 'Por favor entre o DDD do Cessionário';
    }

    if (this.baseUtils.isEmpty(solicit.transfereePhone.phoneNumber)) {
      return 'Por favor entre o Número do Telefone do Cessionário';
    }

    for (let i = 0; i < solicit.transfereeAddresses.length; i++) {
      if (this.baseUtils.isEmpty(solicit.transfereeAddresses[i].zipCode)) {
        return 'Por favor entre com o CEP do Cessionário';
      }
      if (solicit.transfereeAddresses[i].zipCode.length !== 8) {
        return 'Por favor, o CEP do Cessionário deve conter 8 caracteres';
      }
      if (this.baseUtils.isEmpty(solicit.transfereeAddresses[i].streetName)) {
        return 'Por favor entre com o Logradouro  do Cessionário';
      }
      if (this.baseUtils.isEmpty(solicit.transfereeAddresses[i].streetNumber)) {
        return 'Por favor entre com o Número do Endereço do Cessionário';
      }
      if (this.baseUtils.isEmpty(solicit.transfereeAddresses[i].city)) {
        return 'Por favor entre com a Cidade do Cessionário';
      }
      if (this.baseUtils.isEmpty(solicit.transfereeAddresses[i].province)) {
        return 'Por favor entre com o Estado do Cessionário';
      }
      if (this.baseUtils.isEmpty(solicit.transfereeAddresses[i].country)) {
        return 'Por favor entre com o País do Cessionário';
      }
    }

    if (this.baseUtils.isEmpty(solicit.solicitationAddress.zipCode)) {
      return 'Por favor entre com o CEP da Mudança de Endereço';
    }
    if (solicit.solicitationAddress.zipCode.length !== 8) {
      return 'Por favor, o CEP da Mudança de Endereço deve conter 8 caracteres';
    }
    if (this.baseUtils.isEmpty(solicit.solicitationAddress.streetName)) {
      return 'Por favor entre com o Logradouro da Mudança de Endereço';
    }
    if (this.baseUtils.isEmpty(solicit.solicitationAddress.streetNumber)) {
      return 'Por favor entre com o Número do Endereço da Mudança de Endereço';
    }
    if (this.baseUtils.isEmpty(solicit.solicitationAddress.city)) {
      return 'Por favor entre com a Cidade da Mudança de Endereço';
    }
    if (this.baseUtils.isEmpty(solicit.solicitationAddress.province)) {
      return 'Por favor entre com o Estado da Mudança de Endereço';
    }
    if (this.baseUtils.isEmpty(solicit.solicitationAddress.country)) {
      return 'Por favor entre com o País da Mudança de Endereço';
    }
    console.log('LISTA_DOCUMENTS: ', solicit.digitalDocuments);
    if (this.baseUtils.isEmpty(solicit.digitalDocuments) || solicit.digitalDocuments.length === 0) {
      return 'Os documentos devem ser anexados na solicitação';
    } else {
      for (let i = 0; i < solicit.digitalDocuments.length; i++) {
        if (this.baseUtils.isEmpty(solicit.digitalDocuments[i].referenceName)) {
          return 'Os documentos devem ser anexados na solicitação';
        }
      }
    }

    return 'OK';
  }

  solicitationValidator(solicit: any) {

    return this.userService.getValidatorCpfOrCnpj(solicit.donnorDocumentType.toLowerCase(), solicit.donnorDocumentValue)
      .then((resultDonnorDocument) => {

        return this.userService.getValidatorCpfOrCnpj(solicit.transfereeDocumentType.toLowerCase(), solicit.transfereeDocumentValue)
          .then((resultTransfereeDocument) => {

            if (solicit.donnorDocumentType === DocumentType.DOCUMENT_TYPE_CNPJ) {
              if (!resultDonnorDocument) {
                return 'O CNPJ do Cedente pode estar errado';
              }
            }

            if (solicit.transfereeDocumentType === DocumentType.DOCUMENT_TYPE_CNPJ) {
              if (!resultTransfereeDocument) {
                return 'O CNPJ do Cessionário pode estar errado';
              }
            }

            return 'OK';

          }).catch(() => {
            return 'error';
          });

      }).catch(() => {
        return 'error';
      });
  }

  onValidatorCnpj(solicit) {
    if (solicit.donnorDocumentType === DocumentType.DOCUMENT_TYPE_CNPJ) {
      if (this.baseUtils.isEmpty(solicit.donnorDocumentValue)) {
        return 'Por favor entre com o CNPJ do Cedente!';
      } else if (!this.onValidatorCnpj(solicit)) {
        return 'O CNPJ do Cedente pode estar errado';
      }
    }
    return this.userService.getValidatorCpfOrCnpj(solicit.donnorDocumentType.toLowerCase(), solicit.donnorDocumentValue);
  }

  onInputOnliNumber(address: AddressEntity) {
    address.zipCode = address.zipCode.replace(/[^\d.-]/g, '');
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

  onRemoveNumber(value: string) {
    value = value.replace(/[0-9]/g, '');
  }

  onRemoveSpecialCharacterAndNukber(solicit: SolicitationRequestEntity) {
    if (!this.baseUtils.isEmpty(solicit.donnorName)) {
      solicit.donnorName = this.onRemoveSpecialCharacter(solicit.donnorName);
    }
    if (!this.baseUtils.isEmpty(solicit.transfereeName)) {
      solicit.transfereeName = this.onRemoveSpecialCharacter(solicit.transfereeName);
    }
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

  onInputOnliNumberAddress(address: AddressEntity) {
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

  private showLoader(): void {
    this.loaderService.show();
  }
  private hideLoader(): void {
    this.loaderService.hide();
  }

}
