package com.lukrzak.goc.lobbybackend.lobby;

import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ModelTests {

	@Test
	void testAddingPlayer() throws TooManyPlayersInLobbyException {
		Lobby lobby = new Lobby("test", true);

		lobby.addPlayer(new Player());

		assertEquals(1, lobby.getPlayers().size());
	}

	@Test
	void testAddingTooManyPlayers() throws TooManyPlayersInLobbyException {
		Lobby lobby = new Lobby("test", true);
		final int MAX_PLAYERS_IN_LOBBY = 4;

		for (int i = 0; i < MAX_PLAYERS_IN_LOBBY; i++) {
			lobby.addPlayer(new Player());
		}

		assertThrows(TooManyPlayersInLobbyException.class, () -> lobby.addPlayer(new Player()));
	}

	@Test
	void testRemovingPlayer() throws TooManyPlayersInLobbyException {
		Lobby lobby = new Lobby("test", true);
		Player player = new Player("test");
		Player otherPlayer = new Player("other-player");

		lobby.addPlayer(player);
		lobby.addPlayer(otherPlayer);
		lobby.removePlayer(player);

		assertEquals(1, lobby.getPlayers().size());
		assertEquals(otherPlayer, lobby.getPlayers().get(0));
	}

	@Test
	void testRemovingNonExistingPlayer() {
		Lobby lobby = new Lobby("test", true);

		lobby.removePlayer(new Player("test"));

		assertEquals(0, lobby.getPlayers().size());
	}

}
