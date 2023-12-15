import { Component, inject } from '@angular/core';
import { Lobby } from '../lobby';
import { LobbyService } from '../lobby.service';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-lobby-list',
  standalone: true,
  imports: [NgFor, NgIf],
  templateUrl: './lobby-list.component.html',
  styleUrl: './lobby-list.component.css'
})
export class LobbyListComponent {
  
  service: LobbyService = inject(LobbyService);
  lobbies: Lobby[] = [];

  constructor() {
    this.lobbies = this.service.getLobbies();
  }
}
