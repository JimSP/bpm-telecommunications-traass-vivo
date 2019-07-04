import { StepDto } from './../../models/step-dto';
import { Component, OnInit, Renderer2, ViewChild, ElementRef, AfterViewInit, Input, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Renderer3 } from '@angular/core/src/render3/interfaces/renderer';
import { StepEntity } from '../../models/step-entity';
import { EnumTypeService } from '../../services/enum-type.service';

@Component({
  selector: 'steps',
  templateUrl: './steps.component.html',
  styleUrls: ['./steps.component.css']
})
export class StepsComponent implements OnInit, AfterViewInit {

  @ViewChild('stepper') stepper: ElementRef;

  isLinear = false;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  tabSelected = 0;

  gridContentSteps = {text: 'One', cols: 4, rows: 5, color: 'none'};

  @Input() stepDto: StepDto = new StepDto();
  @Input() public index: number;
  @Output() tabSelectedEmmiter = new EventEmitter<number>();

  constructor(
    private _formBuilder: FormBuilder,
    private _renderer: Renderer2,
    private enumService: EnumTypeService
  ) { }

  ngOnInit() {
    this.ngAfterViewInit();
    this.index = this.stepDto.index;
    this.stepDto.listSteps = new Array();

    this.firstFormGroup = this._formBuilder.group({
      firstCtrl: ['', Validators.required]
    });
    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required]
    });
  }

  getIndex(): number {
    return this.stepDto.index;
  }

  ngAfterViewInit(): void {
     console.log('STEPPER', this.stepper);
  }

  getSolicitationStatusLabel(statusId: string) {
    return this.enumService.getSolicitationStatusLabel(statusId);
  }

  onPreviewPage() {
    this.tabSelected = 0;
    this.tabSelectedEmmiter.emit(this.tabSelected);
  }

}
