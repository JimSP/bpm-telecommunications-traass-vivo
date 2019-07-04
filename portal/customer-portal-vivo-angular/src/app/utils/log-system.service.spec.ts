import { TestBed, inject } from '@angular/core/testing';

import { LogSystemService } from './log-system.service';

describe('LogSystemService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LogSystemService]
    });
  });

  it('should be created', inject([LogSystemService], (service: LogSystemService) => {
    expect(service).toBeTruthy();
  }));
});
