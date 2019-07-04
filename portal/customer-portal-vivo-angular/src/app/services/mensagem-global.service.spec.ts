import { TestBed, inject } from '@angular/core/testing';

import { MensagemGlobalService } from './mensagem-global.service';

describe('MensagemGlobalService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MensagemGlobalService]
    });
  });

  it('should be created', inject([MensagemGlobalService], (service: MensagemGlobalService) => {
    expect(service).toBeTruthy();
  }));
});
