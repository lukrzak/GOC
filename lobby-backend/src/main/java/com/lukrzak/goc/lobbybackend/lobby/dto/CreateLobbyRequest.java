package com.lukrzak.goc.lobbybackend.lobby.dto;

public record CreateLobbyRequest(String name, boolean passwordProtected) {
}
