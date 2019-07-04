import { LoaderService } from './loader/loader.service';
import { Component, Input, OnInit, OnDestroy } from '@angular/core';
// tslint:disable-next-line:import-blacklist
import { Subscription } from 'rxjs';
import { LoaderState } from './loader/loader-state';
import { Mensagem } from './models/mensagem';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, OnDestroy {

  @Input() globalMensagem: Mensagem = new Mensagem();

  title = 'app';
  loading = false;
  private subscription: Subscription;

  constructor (
    private loaderService: LoaderService
  ) { }

  ngOnInit() {
    this.subscription = this.loaderService.loaderState
    .subscribe((state: LoaderState) => {
      this.loading = state.show;
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  mensagemEmitter(e) {
    console.log('MSG RECEBIDA >>>>>>>>>>>', e);
    this.globalMensagem = e;
  }


  // constructor(private router: Router) {
  //   this.router.events.subscribe(routerEvent => {
  //     if (routerEvent instanceof NavigationStart) {
  //       if (routerEvent.url === '/') {
  //         this.router.navigate(['canvas'], { skipLocationChange: true });
  //       }
  //     }
  //   });
  // }


}
