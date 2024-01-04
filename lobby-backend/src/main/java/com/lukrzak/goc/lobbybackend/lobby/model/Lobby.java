package com.lukrzak.goc.lobbybackend.lobby.model;

import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.player.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class Lobby {

	private final UUID id;
	private final String name;
	private final boolean passwordProtected;
	private final List<Player> players = new ArrayList<>();
	private final Player admin;

	public Lobby(String name, boolean passwordProtected, Player admin) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.passwordProtected = passwordProtected;
		this.admin = admin;
	}

	public void addPlayer(Player player) throws TooManyPlayersInLobbyException {
		if (players.size() >= 4) {
			throw new TooManyPlayersInLobbyException(this.name);
		}
		players.add(player);
	}

	public void removePlayer(Player player) {
		players.remove(player);
	}

}
