import { Mensagem } from './../models/mensagem';
import { Component, OnInit, Input } from '@angular/core';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'mensagem-sistem',
  templateUrl: './mensagem-sistem.component.html',
  styleUrls: ['./mensagem-sistem.component.css']
})
export class MensagemSistemComponent implements OnInit {

  @Input() mensagem: Mensagem = new Mensagem();

  constructor(public snackBar: MatSnackBar) {}

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'end',
      panelClass: ['msg-success']
    });
  }

  ngOnInit() {
    // this.mensagem.msg = 'Teste Felipe ';
    // this.mensagem.tipo = 'warning';
  }

  onCloseMessage() {
    this.mensagem.tipo = '';
  }

}
