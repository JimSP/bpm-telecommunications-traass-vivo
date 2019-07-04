import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MensagemSistemComponent } from './mensagem-sistem.component';

describe('MensagemSistemComponent', () => {
  let component: MensagemSistemComponent;
  let fixture: ComponentFixture<MensagemSistemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MensagemSistemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MensagemSistemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
