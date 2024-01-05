import { Injectable } from '@angular/core';
import { Lobby } from './lobby';
import { Lobbies } from './lobbies';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Player } from './player';
import { LobbyDetails } from './lobby-details';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  private http: HttpClient;
  private LOBBIES_URL = "http://localhost:8080/api/v1/lobbies";

  constructor(http: HttpClient) {
    this.http = http;
  }

  getLobbies(): Observable<Lobbies> {
    console.log("Sending get lobbies request: " + this.LOBBIES_URL);

    return this.http.get<Lobbies>(this.LOBBIES_URL);
  }

  getLobby(id: string): Observable<LobbyDetails> {
    const GET_LOBBY_URL = this.LOBBIES_URL + "/" + id;
    console.log("Sending get lobby request: " + GET_LOBBY_URL);

    return this.http.get<LobbyDetails>(GET_LOBBY_URL);
  }

  createLobby(lobbyName: string, passwordProtected: boolean, username: string): Observable<Lobby> {
    console.log("Sending create lobby request: " + this.LOBBIES_URL);
    const body = {
      name: lobbyName,
      passwordProtected: passwordProtected,
      admin: username
    }

    return this.http.post<Lobby>(this.LOBBIES_URL, body);
  }

  joinLobby(playerName: string, lobbyId: string): Observable<any> {
    const player: Player = {
      name: playerName
    };
    const JOIN_REQUEST_URL = this.LOBBIES_URL + "/join/" + lobbyId;
    console.log("Sending join lobby request:" + JOIN_REQUEST_URL);

    return this.http.post(JOIN_REQUEST_URL, player, {responseType: 'text' as 'json'});
  }

  leaveLobby(playerName: string, lobbyId: string): Observable<any> {
    const player: Player = {
      name: playerName
    };
    const LEAVE_REQUEST_URL = this.LOBBIES_URL + "/leave/" + lobbyId;
    console.log("Sending leave lobby request: " + LEAVE_REQUEST_URL);

    return this.http.post(LEAVE_REQUEST_URL, player, {responseType: 'text' as 'json'});
  }

  deleteLobby(lobbyId: string): Observable<any> {
    const DELETE_LOBBY_URL = this.LOBBIES_URL + "/" + lobbyId;
    console.log("Sending delete lobby request: " + DELETE_LOBBY_URL);

    return this.http.delete(DELETE_LOBBY_URL, {responseType: 'text' as 'json'});
  }

}
