package com.lukrzak.goc.lobbybackend.lobby.dto;

import com.lukrzak.goc.lobbybackend.player.Player;

import java.util.List;

public record GetLobbyResponse(String name, List<Player> players, Player admin) {
}
