import { ErrorStateMatcher, ShowOnDirtyErrorStateMatcher } from '@angular/material/core';
import { BrowserModule } from '@angular/platform-browser';

import { NgModule} from '@angular/core';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { MenuPrincipalComponent } from './menu-principal/menu-principal.component';
import { MenuPrincipalModule } from './menu-principal/menu-principal.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app.routing.module';
import { LoginComponent } from './login/login.component';
import { RodapeComponent } from './rodape/rodape.component';
import { PerfilComponent } from './perfil/perfil.component';
import { SolicitacaoComponent } from './solicitacao/solicitacao.component';
import { StepsComponent } from './solicitacao/steps/steps.component';
import { HttpClientModule } from '@angular/common/http';
import { NovaSolicitacaoComponent } from './solicitacao/nova-solicitacao/nova-solicitacao.component';
import { ListaSolicitacaoComponent } from './solicitacao/lista-solicitacao/lista-solicitacao.component';
import { MensagemSistemComponent } from './mensagem-sistem/mensagem-sistem.component';
import { PerfilContentComponent } from './perfil/perfil-content/perfil-content.component';
import { FileUploadComponent } from './solicitacao/file-upload/file-upload.component';
import { FileSelectDirective } from 'ng2-file-upload';
import { RecuperarSenhaComponent } from './recuperar-senha/recuperar-senha.component';
import { HelpDocumentsDialogComponent } from './solicitacao/file-upload/help-documents-dialog.component';
import { MatDialogModule, } from '@angular/material';
import { LoaderComponent } from './loader/loader.component';
import { AuthenticateUserComponent } from './authenticate-user/authenticate-user.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { PageHelpComponent } from './page-help/page-help.component';
import { PageAboutComponent } from './page-about/page-about.component';
import { LocationStrategy, HashLocationStrategy } from '../../node_modules/@angular/common';
@NgModule({
  exports: [MatDialogModule],
  entryComponents: [ HelpDocumentsDialogComponent ],
  declarations: [
    AppComponent,
    HomeComponent,
    MenuPrincipalComponent,
    LoginComponent,
    RodapeComponent,
    PerfilComponent,
    SolicitacaoComponent,
    StepsComponent,
    NovaSolicitacaoComponent,
    ListaSolicitacaoComponent,
    MensagemSistemComponent,
    PerfilContentComponent,
    FileUploadComponent,
    FileSelectDirective,
    RecuperarSenhaComponent,
    HelpDocumentsDialogComponent,
    LoaderComponent,
    AuthenticateUserComponent,
    PageNotFoundComponent,
    PageHelpComponent,
    PageAboutComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    MenuPrincipalModule,
    AppRoutingModule
  ],
  providers: [
    {provide: ErrorStateMatcher, useClass: ShowOnDirtyErrorStateMatcher}
    // {provide: LocationStrategy, useClass: HashLocationStrategy}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

