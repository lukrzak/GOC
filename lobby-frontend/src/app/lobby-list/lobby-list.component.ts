import { Component, inject } from '@angular/core';
import { Lobby } from '../lobby';
import { LobbyService } from '../lobby.service';
import { NgFor, NgIf } from '@angular/common';
import { Lobbies } from '../lobbies';

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
    this.service.getLobbies().subscribe((result: Lobbies) => 
      this.lobbies = result.lobbies
    );
  }
  
  handleJoinButtonClick(id: string, isPasswordProtected: boolean) {
    let username = prompt("Enter username\nTODO: Replace with the form page");
    let password;
    if (isPasswordProtected) {
      password = prompt("Enter lobby password\nTODO: Replace with the form page");
    }
    this.service.joinLobby(username!, id).subscribe(() =>
      console.log(username)
    );
  }

}
