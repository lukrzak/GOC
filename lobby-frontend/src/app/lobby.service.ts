import { Injectable } from '@angular/core';
import { Lobby } from './lobby';
import { Lobbies } from './lobbies';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  private lobbies: Lobby[] = [];
  private LOBBIES_URI = "http://localhost:8080/api/v1/lobbies";

  constructor() { }

  async getLobbies(): Promise<Lobbies> {
    const result = await fetch(this.LOBBIES_URI);
    return await result.json();
  }

  getLobby(id: string): Lobby | undefined {
    return this.lobbies.find(lobby => lobby.id === id);
  }
  
}
