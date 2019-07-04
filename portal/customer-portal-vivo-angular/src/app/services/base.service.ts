import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BaseService {

  constructor() { }

  public mountUrlByParam(url: string, param: string) {
    return `${url}/${param}`;
  }

  public mountUrlByIds(url: string, ids: string[]) {
    let urlParam = '';
    let i: 1;
    ids.forEach(element => {
      if (i === ids.length) {
        urlParam += element;
      } else {
        urlParam += `${element},`;
      }
      i++;
    });
    return `${url}/${urlParam}`;
  }

  public mountUrlByMultParams(url: string, params: string[]) {
    let urlParam = '';
    let i: 1;
    params.forEach(element => {
      if (i === params.length) {
        urlParam += element;
      } else {
        urlParam += `${element}/`;
      }
      i++;
    });
    return `${url}/${urlParam}`;
  }

  public mountUrlByIdInverter(id: string, url: string) {
    return `${id}/${url}`;
  }
}
