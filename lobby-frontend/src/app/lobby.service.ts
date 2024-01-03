import { Injectable } from '@angular/core';
import { Lobby } from './lobby';
import { Lobbies } from './lobbies';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Player } from './player';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  private http: HttpClient;
  private lobbies: Lobby[] = [];
  private LOBBIES_URI = "http://localhost:8080/api/v1/lobbies";

  constructor(http: HttpClient) {
    this.http = http;
  }

  getLobbies(): Observable<Lobbies> {
    console.log("Sending getLobbies request");
    return this.http.get<Lobbies>(this.LOBBIES_URI);
  }

  joinLobby(name: string, lobbyId: string): Observable<any> {
    const player: Player = {
      name: name
    };
    const requestUri = this.LOBBIES_URI + "/join/" + lobbyId;
    console.log("Sending join lobby request:" + requestUri);

    return this.http.post(requestUri, player, {responseType: 'text' as 'json'});
  }

  getLobby(id: string): Lobby | undefined {
    return this.lobbies.find(lobby => lobby.id === id);
  }

}
