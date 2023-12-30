package com.lukrzak.goc.lobbybackend.lobby.dto;

import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;

import java.util.List;

public record GetLobbiesResponse(List<Lobby> lobbies) {
}
