import { PageAboutComponent } from './page-about/page-about.component';
import { AuthenticateUserComponent } from './authenticate-user/authenticate-user.component';
import { SolicitacaoComponent } from './solicitacao/solicitacao.component';
import { PerfilComponent } from './perfil/perfil.component';
import { LoginComponent } from './login/login.component';

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RecuperarSenhaComponent } from './recuperar-senha/recuperar-senha.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const appRoutes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'home', component: HomeComponent},
    { path: 'login', component: LoginComponent},
    { path: 'myProfile', component: PerfilComponent},
    { path: 'myProfileTab/:tabIndex/:userId', component: PerfilComponent},
    // { path: 'solicitacao', component: SolicitacaoComponent},
    { path: 'solicitacao/:userId', component: SolicitacaoComponent},
    { path: 'novoUsuario', component: PerfilComponent},
    { path: 'recuperarSenha', component: RecuperarSenhaComponent},
    { path: 'authenticateUser', component: AuthenticateUserComponent},
    { path: 'pageAbout', component: PageAboutComponent},
    { path: 'pageAbout/:userId', component: PageAboutComponent},
    { path: 'vt-user-ms/verified/:tokenAuth', component: AuthenticateUserComponent, pathMatch: 'full'},
    { path: '**', component: PageNotFoundComponent,  pathMatch: 'full' }
    // { path: 'vt-user-ms/verified/', component: AuthenticateUserComponent },
];

// this.router.navigate(['/view'], { skipLocationChange: true });

@NgModule({
   imports: [RouterModule.forRoot(appRoutes)],
   exports: [RouterModule]
})
export class AppRoutingModule { }
