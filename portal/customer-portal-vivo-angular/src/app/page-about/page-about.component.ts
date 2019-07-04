import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserEntity } from '../models/user-entity';
import { UserService } from '../services/user.service';
import { environment } from '../../environments/environment';
import { MensagemGlobalService } from '../services/mensagem-global.service';

@Component({
  selector: 'page-about',
  templateUrl: './page-about.component.html',
  styleUrls: ['./page-about.component.css']
})
export class PageAboutComponent implements OnInit {

  @Output() userLoggedInEmitter = new EventEmitter<UserEntity>();

  userId = 0;
  userLoggedIn: UserEntity = new UserEntity();
  version = environment.version;
  versionDate = environment.versionDate;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private mesageService: MensagemGlobalService
  ) {
    this.userId = this.route.snapshot.params['userId'];
    this.userLoggedIn.id = this.userId;
   }

  ngOnInit() {
    this.userService.getUserById(this.userId).then((succes) => {
      this.userLoggedIn = succes;
      this.userLoggedInEmitter.emit(this.userLoggedIn);
    });
  }

  onPreviewPage() {
    this.mesageService.onTopScrollPage();
    window.history.back();
  }

}
