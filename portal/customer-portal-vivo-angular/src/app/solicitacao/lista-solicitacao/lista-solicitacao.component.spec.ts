import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaSolicitacaoComponent } from './lista-solicitacao.component';

describe('ListaSolicitacaoComponent', () => {
  let component: ListaSolicitacaoComponent;
  let fixture: ComponentFixture<ListaSolicitacaoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListaSolicitacaoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListaSolicitacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
