package com.lukrzak.goc.lobbybackend.lobby;

import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.player.Player;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

	public static List<Lobby> generateLobbies(int amount) {
		List<Lobby> lobbies = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			String lobbyName = "lobby-" + i;
			boolean isProtected = i % 2 == 0;
			lobbies.add(new Lobby(lobbyName, isProtected, new Player("admin")));
		}

		return lobbies;
	}

}
