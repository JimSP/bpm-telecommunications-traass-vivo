import { TestBed, inject } from '@angular/core/testing';

import { EnumTypeService } from './enum-type.service';

describe('EnumTypeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EnumTypeService]
    });
  });

  it('should be created', inject([EnumTypeService], (service: EnumTypeService) => {
    expect(service).toBeTruthy();
  }));
});
