import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';

@Component({
  selector: 'rodape',
  templateUrl: './rodape.component.html',
  styleUrls: ['./rodape.component.css']
})
export class RodapeComponent implements OnInit {

  currentDate: Date = new Date();
  version: string;

  constructor() { }

  ngOnInit() {
    if (environment.gatewayUrl === 'http://localhost:8091' || environment.gatewayUrl === 'http://bpm-hml.interfile.com.br/vt_user') {
      this.version = `v${environment.version}`;
    }
  }

}
