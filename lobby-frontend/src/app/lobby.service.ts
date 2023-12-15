import { Injectable } from '@angular/core';
import { Lobby } from './lobby';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  lobbies: Lobby[] = [
    {id: 1, name: "testLobby1", numberOfPlayers: 3, passwordProtected: false},
    {id: 2, name: "testLobby2", numberOfPlayers: 0, passwordProtected: true}
  ];

  constructor() { }

  getLobbies(): Lobby[] {
    return this.lobbies;
  }

  getLobby(id: number): Lobby | undefined {
    return this.lobbies.find(lobby => lobby.id === id);
  }
}
