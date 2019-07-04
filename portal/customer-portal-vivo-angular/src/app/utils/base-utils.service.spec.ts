import { TestBed, inject } from '@angular/core/testing';

import { BaseUtilsService } from './base-utils.service';

describe('BaseUtilsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BaseUtilsService]
    });
  });

  it('should be created', inject([BaseUtilsService], (service: BaseUtilsService) => {
    expect(service).toBeTruthy();
  }));
});
