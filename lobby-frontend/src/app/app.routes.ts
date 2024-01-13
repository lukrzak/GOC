import { Routes } from '@angular/router';
import { LobbyListComponent } from './lobby-list/lobby-list.component';
import { LobbyViewComponent } from './lobby-view/lobby-view.component';

export const routes: Routes = [
    {path: '', component: LobbyListComponent, title: 'GOC - Lobbies'},
    {path: 'lobby/:id', component: LobbyViewComponent}
];
