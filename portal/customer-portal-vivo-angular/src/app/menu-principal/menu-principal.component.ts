import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UserEntity } from '../models/user-entity';
import { Router } from '@angular/router';
import { MensagemGlobalService } from '../services/mensagem-global.service';

@Component({
  selector: 'menu-principal',
  templateUrl: './menu-principal.component.html',
  styleUrls: ['./menu-principal.component.css']
})
export class MenuPrincipalComponent implements OnInit {

  @Input() userLoggedIn: UserEntity = new UserEntity();

  constructor(
    private router: Router,
    private mesageService: MensagemGlobalService
  ) {}

  ngOnInit() {
    // this.userLoggedIn = JSON.parse(localStorage.getItem('userLoggedIn'));
    // console.log('>>>>>>>>>>>', this.userLoggedIn);
    if (this.userLoggedIn === undefined || this.userLoggedIn === null) {
      this.userLoggedIn = new UserEntity;
      this.userLoggedIn.id = 0;
    }
  }

  userLoggedInEmitter(event) {
    console.log('>>>>>>>>>>>', event);
    this.userLoggedIn =  event;
  }

  onLogout() {
    localStorage.removeItem('userLoggedIn');
    this.router.navigate(['/login']);
  }

  // [routerLink]="['/myProfileTab', 1, userLoggedIn.id]"
  onRouteTopPage(route: string, index: number, param) {
    this.mesageService.onTopScrollPage();
    this.router.navigate([route, index, param]);
  }

}
