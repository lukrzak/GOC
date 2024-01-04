package com.lukrzak.goc.lobbybackend.lobby;

import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.lobby.repository.LobbyRepository;
import com.lukrzak.goc.lobbybackend.lobby.repository.LobbyRepositoryImpl;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LobbyRepositoryImplTests {

	@Test
	void testAddingLobby() {
		LobbyRepository lobbyRepository = new LobbyRepositoryImpl();

		lobbyRepository.addLobby(new Lobby("lobby-1", true, new Player()));
		lobbyRepository.addLobby(new Lobby("lobby-2", true, new Player()));

		assertEquals(2, lobbyRepository.getLobbies().size());
	}

	@Test
	void testGettingLobby() {
		LobbyRepository lobbyRepository = new LobbyRepositoryImpl();
		Lobby lobbyToCompare = new Lobby("lobby-2", true, new Player());

		lobbyRepository.addLobby(new Lobby("lobby-1", true, new Player()));
		lobbyRepository.addLobby(lobbyToCompare);
		lobbyRepository.addLobby(new Lobby("lobby-3", true, new Player()));

		assertEquals(lobbyToCompare, lobbyRepository.getLobby(lobbyToCompare.getId()).get());
	}

	@Test
	void testGettingNonExistingLobby() {
		LobbyRepository lobbyRepository = new LobbyRepositoryImpl();

		lobbyRepository.addLobby(new Lobby("lobby-1", true, new Player()));

		assertEquals(Optional.empty(), lobbyRepository.getLobby(UUID.randomUUID()));
	}

	@Test
	void testDeletingLobby() {
		LobbyRepository lobbyRepository = new LobbyRepositoryImpl();
		Lobby lobby = new Lobby("lobby-1", true, new Player());
		lobbyRepository.addLobby(lobby);
		lobbyRepository.addLobby(new Lobby("lobby-2", true, new Player()));

		lobbyRepository.deleteLobby(lobby.getId());

		assertEquals(1, lobbyRepository.getLobbies().size());
	}

	@Test
	void testDeletingNonExistingLobby() {
		LobbyRepository lobbyRepository = new LobbyRepositoryImpl();
		lobbyRepository.addLobby(new Lobby("lobby-1", true, new Player()));
		lobbyRepository.addLobby(new Lobby("lobby-2", false, new Player()));

		lobbyRepository.deleteLobby(UUID.randomUUID());

		assertEquals(2, lobbyRepository.getLobbies().size());
	}

}
