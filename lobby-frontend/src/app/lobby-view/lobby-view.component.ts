import { Component, inject } from '@angular/core';
import { LobbyDetails } from '../lobby-details';
import { LobbyService } from '../lobby.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgFor, NgIf } from '@angular/common';
import { Player } from '../player';

@Component({
  selector: 'app-lobby-view',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './lobby-view.component.html',
  styleUrl: './lobby-view.component.css'
})
export class LobbyViewComponent {

  lobby: LobbyDetails = {name: "lobby", players: [], admin: {name: "admin"}};
  private lobbyId = this.route.snapshot.params['id'];
  private service: LobbyService = inject(LobbyService);
  private router: Router = inject(Router);
  
  constructor(private route: ActivatedRoute) {
    this.service.getLobby(this.lobbyId).subscribe((result: LobbyDetails) => 
      this.lobby = result
    );

    window.addEventListener('popstate', () => this.leaveLobby());
    document.title = "GOC - lobby " + this.lobby.name;
  }

  leaveLobby() {
    const player = localStorage.getItem('player');
    this.service.leaveLobby(player!, this.lobbyId).subscribe({
      next: () => {
        this.router.navigate([""]);
      },
      error: () => {
        alert("Error while leaving lobby");
      }
    });
  }

  deleteLobby() {
    this.service.deleteLobby(this.lobbyId).subscribe({
      next: () => {
        this.router.navigate([""]);
      },
      error: () => {
        alert("Error while deleting lobby");
      }
    })
  }

  startGame() {
    console.log("Game started")
  }

  checkIfAdmin(player: Player): boolean {
    return this.lobby.admin.name === player.name;
  } 

}
