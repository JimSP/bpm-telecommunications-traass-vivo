import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LogSystemService {

  constructor() { }

  public static LOGGER_DEBUGGER: true;

  public static logger(title: string, listMsg: any[]) {
    if (this.LOGGER_DEBUGGER) {
      let msg = '';
      listMsg.forEach((element) => {
        msg = `${msg} ${element}`;
      });
      console.log(`${title}: ${msg}`);
    }
  }

  public static loggerObj(title: string, obj) {
    if (this.LOGGER_DEBUGGER) {
      console.log(`${title}:`, obj);
    }
  }
}
