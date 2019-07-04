import { TestBed, inject } from '@angular/core/testing';

import { SolicitationService } from './solicitation.service';

describe('SolicitationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SolicitationService]
    });
  });

  it('should be created', inject([SolicitationService], (service: SolicitationService) => {
    expect(service).toBeTruthy();
  }));
});
