package com.lukrzak.goc.lobbybackend.lobby.dto;

import java.util.List;

public record GetLobbiesResponse(List<LobbyResponse> lobbies) {
}
