import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { LobbyListComponent } from './lobby-list/lobby-list.component';
import { HeaderComponent } from './header/header.component';
import { LobbyViewComponent } from './lobby-view/lobby-view.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule, 
    RouterOutlet, 
    LobbyListComponent, 
    HeaderComponent,
    LobbyViewComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Game Of Countries - Lobby';
}
