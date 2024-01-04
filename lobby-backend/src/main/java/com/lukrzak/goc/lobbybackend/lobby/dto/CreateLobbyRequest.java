package com.lukrzak.goc.lobbybackend.lobby.dto;

import com.lukrzak.goc.lobbybackend.player.Player;

public record CreateLobbyRequest(String name, boolean passwordProtected, Player admin) {
}
