import { BaseUtilsService } from './../../utils/base-utils.service';
import { DigitalDocumentEntity } from './../../models/digital-document-entity';
import { Component, OnInit, Output, EventEmitter, Input, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MensagemGlobalService } from '../../services/mensagem-global.service';
import { Mensagem } from '../../models/mensagem';
import { EnumMesageType } from '../../enumerators/enum-mesage-type.enum';
import { SolicitationRequestEntity } from '../../models/solicitation-request-entity';
import { HelpDocumentsDialogComponent } from './help-documents-dialog.component';
import { MatDialog } from '@angular/material';
import { SolicitationService } from '../../services/solicitation.service';

@Component({
  selector: 'file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  @Output() mensagemEmitter = new EventEmitter<Mensagem>();
  @Output() listDigitalDocumentEmitter = new EventEmitter<DigitalDocumentEntity[]>();
  @Input() listDigitalDocument: DigitalDocumentEntity[];
  @Input() listDigitalDocumentEdit: DigitalDocumentEntity[];
  @Input() solicitation: SolicitationRequestEntity;
  @Input() digitalDocument: DigitalDocumentEntity;
  @Input() solicitationId: number;
  @Input() disabledSolicitationEdit: Boolean;

  globalMensagem: Mensagem = new Mensagem();
  image: any;
  userId: number;
  attachedType: string;

  gridContentSteps = {text: 'One', cols: 4, rows: 3, color: 'none'};

  constructor(
    private route: ActivatedRoute,
    private baseUtils: BaseUtilsService,
    private mesageService: MensagemGlobalService,
    public dialog: MatDialog,
    private solicitationService: SolicitationService
  ) {
    this.userId = this.route.snapshot.params['userId'];
  }

  ngOnInit() {
    this.digitalDocument = new DigitalDocumentEntity();
  }

  changeListener($event): void {
    this.readThis($event.target);
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(HelpDocumentsDialogComponent, {
      width: '95%', height: '80%'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  readThis(inputValue: any): void {
    const file: File = inputValue.files[0];
    const ext = file.name.split('.');
    const nameCode = this.baseUtils.formatDate(new Date(), 'yyyymmddhhmmss');
    const type = this.digitalDocument.documentType.toUpperCase();
    const name = `DOCUMENT_ATTACHED_${type}_${this.userId}${nameCode}.${ext[1]}`;

    this.digitalDocument.referenceName = name;
    const myReader: FileReader = new FileReader();
    myReader.onloadend = () => {
      console.log('IMG_RESULT: ', myReader.result);
      const img = (myReader.result).split(';');
      this.digitalDocument.data = img[1].substring(7, img[1].length);
      if (this.baseUtils.isEmpty(this.listDigitalDocument)) {
        this.listDigitalDocument = new Array();
      }
      this.listDigitalDocument.push(this.baseUtils.copyDigitalDocument(this.digitalDocument));
      this.onCheckAndEmitterListDocument(this.listDigitalDocument);
      this.digitalDocument = new DigitalDocumentEntity();
      this.mesageService.sendMensage(EnumMesageType.INFO, 'Arquivo anexado', this.mensagemEmitter);
    };
    myReader.readAsDataURL(file);
  }

  removeAttachedItem(item) {
    this.mesageService.showLoader();
    console.log('SOLICITATION_ID: ', this.solicitationId);
    console.log('ITEM: ', item);
    if (this.baseUtils.isEmpty(item.data)) {
      this.solicitationService.deleteDigitalDocumentBySolicitationIdAndDIgitalDocumentId(
        this.solicitationId, item.id, this.globalMensagem)
        .then(() => {
          this.mesageService.hideLoader();
          this.mesageService.sendMensage(this.globalMensagem.tipo, this.globalMensagem.msg, this.mensagemEmitter);
        }).catch(() => {
          this.mesageService.hideLoader();
          this.mesageService.sendMensage(this.globalMensagem.tipo, this.globalMensagem.msg, this.mensagemEmitter);
        });
    }
    const newListDigitalDocuments = new Array;
    this.listDigitalDocument.forEach((element) => {
      if (element.referenceName !== item.referenceName) {
        newListDigitalDocuments.push(element);
      }
    });
    this.listDigitalDocument = newListDigitalDocuments;
    this.onCheckAndEmitterListDocument(this.listDigitalDocument);
    this.mesageService.hideLoader();
  }

  removeAttachedEditItem(item) {
    const newListDigitalDocuments = new Array;
    this.solicitation.digitalDocuments.forEach((element) => {
      if (element.referenceName !== item.referenceName) {
        newListDigitalDocuments.push(element);
      }
    });
    this.solicitation.digitalDocuments = newListDigitalDocuments;
  }

  addAttachedItem() {
    this.digitalDocument = new DigitalDocumentEntity();
    this.listDigitalDocument.push(new DigitalDocumentEntity());
    this.onCheckAndEmitterListDocument(this.listDigitalDocument);
  }

  onCheckAndEmitterListDocument(listDigitalDocument) {
    if (!this.baseUtils.isEmpty(listDigitalDocument)) {
      this.listDigitalDocumentEmitter.emit(listDigitalDocument);
    }
    console.log('Lista não foi emitida por estar vazia');
  }

}
