import { BaseUtilsService } from './../utils/base-utils.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'authenticate-user',
  templateUrl: './authenticate-user.component.html',
  styleUrls: ['./authenticate-user.component.css']
})
export class AuthenticateUserComponent implements OnInit {

  tokenAuth: string;
  autenticate: boolean;
  localUrl: any;

  constructor(
    private route: ActivatedRoute,
    private baseUtil: BaseUtilsService,
    private userService: UserService
  ) {
    this.tokenAuth = this.route.snapshot.params['tokenAuth'];
    this.localUrl = this.route.snapshot.url;
  }

  ngOnInit() {
    console.log('LOCAL_URL: ', this.localUrl);
    console.log('TOKEN_AUTH: ', this.tokenAuth);
    if (!this.baseUtil.isEmpty(this.tokenAuth)) {

      this.userService.onVerifyEmail(this.tokenAuth).then(() => {
        this.autenticate = true;
        console.log('SUCESSO');
      }).catch(() => {
        this.autenticate = false;
        console.log('ERROR');
      });

    } else {
      this.autenticate = false;
      console.log('SEM TOKEN');
    }
  }

}
