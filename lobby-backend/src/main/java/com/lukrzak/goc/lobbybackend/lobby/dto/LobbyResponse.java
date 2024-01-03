package com.lukrzak.goc.lobbybackend.lobby.dto;

public record LobbyResponse(String id, String name, boolean passwordProtected, int numberOfPlayers) {
}
