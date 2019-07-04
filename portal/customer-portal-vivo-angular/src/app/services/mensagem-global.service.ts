import { Mensagem } from './../models/mensagem';
import { Injectable } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { LoaderService } from '../loader/loader.service';

@Injectable({
  providedIn: 'root'
})
export class MensagemGlobalService {

  globalMensagem: Mensagem = new Mensagem;

  constructor(
    private router: Router,
    private loaderService: LoaderService,
  ) { }

  sendMensage(type: string, msg: string, mensagemEmitter) {
    this.globalMensagem.tipo = type;
    this.globalMensagem.msg = msg;
    this.onTopScrollPage();
    mensagemEmitter.emit(this.globalMensagem);
    setTimeout(() => {
      this.globalMensagem.tipo = '';
      mensagemEmitter.emit(this.globalMensagem);
    }, 10000);
    this.hideLoader();
  }

  sendMensageSetTime(type: string, msg: string, mensagemEmitter, timeCloseMsg: number) {
    this.globalMensagem.tipo = type;
    this.globalMensagem.msg = msg;
    this.onTopScrollPage();
    mensagemEmitter.emit(this.globalMensagem);
    setTimeout(() => {
      this.globalMensagem.tipo = '';
      mensagemEmitter.emit(this.globalMensagem);
    }, timeCloseMsg);
    this.hideLoader();
  }

  sendLocalMensage(type: string, msg: string, localMesage: Mensagem) {
    localMesage.tipo = type;
    localMesage.msg = msg;
    this.onTopScrollPage();
    setTimeout(() => {
      localMesage = new Mensagem();
    }, 10000);
    this.hideLoader();
  }

  public onTopScrollPage() {
    window.scrollTo(0, 0);
  }

  public showLoader(): void {
    this.loaderService.show();
  }
  public hideLoader(): void {
    this.loaderService.hide();
  }

  cleanMessage(mensagemEmitter) {
    mensagemEmitter.emit(new Mensagem());
  }
}
