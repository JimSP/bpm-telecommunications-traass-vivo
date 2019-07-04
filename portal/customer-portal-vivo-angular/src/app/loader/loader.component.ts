import { LoaderState } from './loader-state';
import { LoaderService } from './loader.service';
import { Component, OnInit } from '@angular/core';
// tslint:disable-next-line:import-blacklist
import { Subscription } from 'rxjs';

@Component({
  selector: 'loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent implements OnInit {

  show = true;
  private subscription: Subscription;

  gridContentSteps = {text: 'One', cols: 4, rows: 8, color: 'none'};

  constructor(
    private loaderService: LoaderService
  ) { }

  ngOnInit() {
    this.subscription = this.loaderService.loaderState.subscribe(
      (state: LoaderState) => {
        this.show = state.show;
      });
  }

  ngOnDestoy() {
    this.subscription.unsubscribe();
  }

}
