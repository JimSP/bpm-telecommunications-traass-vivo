import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NovaSolicitacaoComponent } from './nova-solicitacao.component';

describe('NovaSolicitacaoComponent', () => {
  let component: NovaSolicitacaoComponent;
  let fixture: ComponentFixture<NovaSolicitacaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NovaSolicitacaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NovaSolicitacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
