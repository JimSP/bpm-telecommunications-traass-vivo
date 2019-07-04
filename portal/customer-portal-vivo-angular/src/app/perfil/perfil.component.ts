import { UserEntity } from './../models/user-entity';
import { UserService } from './../services/user.service';
import { FormControl, Validators, FormGroupDirective, NgForm } from '@angular/forms';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ErrorStateMatcher, MatTabChangeEvent } from '@angular/material';
import { Mensagem } from '../models/mensagem';
import { ActivatedRoute } from '@angular/router';


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {

  @Input() globalMensagem: Mensagem = new Mensagem();

  @Output() userLoggedInEmitterMaster = new EventEmitter<UserEntity>();

  @Input() userLoggedIn: UserEntity = new UserEntity();

  tabSelected = 0;
  userId = 0;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService
  ) {
    this.tabSelected = this.route.snapshot.params['tabIndex'];
    this.userId = this.route.snapshot.params['userId'];
  }

  ngOnInit() {
    if (this.userId !== 0 && this.userId !== undefined) {
      this.userService.getUserById(this.userId).then((succes) => {
        this.userLoggedIn = succes;
      });
    }
  }

  mensagemEmitter(e) {
    // console.log('>>>>>>>>>>>', e);
    this.globalMensagem.tipo = e.tipo;
    this.globalMensagem.msg = e.msg;
  }

  userLoggedInEmitter(e) {
    this.userLoggedIn = e;
    this.userLoggedInEmitterMaster.emit(this.userLoggedIn);
  }

  sendMensage(type: string, msg: string) {
    this.globalMensagem.tipo = type;
    this.globalMensagem.msg = msg;
    setTimeout(() => {
      this.globalMensagem = new Mensagem();
    }, 5000);
  }

  onTabClick(event: MatTabChangeEvent) {
    this.tabSelected = event.index;
  }

}
