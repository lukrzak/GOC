import { Player } from "./player";

export interface LobbyDetails {
    name: string,
    players: Player[],
    admin: Player
}