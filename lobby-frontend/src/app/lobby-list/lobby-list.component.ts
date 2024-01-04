import { Component, inject } from '@angular/core';
import { Lobby } from '../lobby';
import { LobbyService } from '../lobby.service';
import { NgFor, NgIf } from '@angular/common';
import { Lobbies } from '../lobbies';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-lobby-list',
  standalone: true,
  imports: [NgFor, NgIf, RouterModule],
  templateUrl: './lobby-list.component.html',
  styleUrl: './lobby-list.component.css'
})
export class LobbyListComponent {
  
  private service: LobbyService = inject(LobbyService);
  private router: Router = inject(Router);
  lobbies: Lobby[] = [];

  constructor() {
    this.service.getLobbies().subscribe((result: Lobbies) => 
      this.lobbies = result.lobbies
    );
  }
  
  joinLobby(id: string, isPasswordProtected: boolean) {
    let username = prompt("Enter username\nTODO: Replace with the form page");
    localStorage.setItem('player', username!);
    let password;
    if (isPasswordProtected) {
      password = prompt("Enter lobby password\nTODO: Replace with the form page");
    }
    this.connectToLobby(username!, id);
  }

  createLobby() {
    const name = prompt("Enter lobby name\nTODO: Replace with the form page");
    const password = confirm("Enable password protection\nTODO: Replace with the form page");

    this.service.createLobby(name!, password).subscribe({
      next: (response) => {
        this.connectToLobby(localStorage.getItem("player")!, response.id);
      },
      error: () => {
        alert("Error while creating a lobby");
      }
    });
  }

  connectToLobby(username: string, id: string) {
    this.service.joinLobby(username!, id).subscribe({
      next: () => {
        this.router.navigate(["/lobby", id]);
      },
      error: () => {
        alert("too many players in the lobby");
      }
    });
  }

}
