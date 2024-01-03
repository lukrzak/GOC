package com.lukrzak.goc.lobbybackend.lobby;

import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.exception.LobbyDoesNotExist;
import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.lobby.repository.LobbyRepository;
import com.lukrzak.goc.lobbybackend.lobby.repository.LobbyRepositoryImpl;
import com.lukrzak.goc.lobbybackend.lobby.service.DefaultLobbyService;
import com.lukrzak.goc.lobbybackend.lobby.service.LobbyService;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class DefaultLobbyServiceTests {

	private final LobbyRepository lobbyRepository = mock(LobbyRepositoryImpl.class);
	private final LobbyService lobbyService = new DefaultLobbyService(lobbyRepository);

	@Test
	void testGettingLobbies() {
		final int AMOUNT_OF_LOBBIES = 5;
		doReturn(TestUtils.generateLobbies(AMOUNT_OF_LOBBIES))
				.when(lobbyRepository)
				.getLobbies();

		List<Lobby> result = lobbyService.getLobbies();

		assertEquals(AMOUNT_OF_LOBBIES, result.size());
	}

	@Test
	void testGettingLobby() throws LobbyDoesNotExist {
		Lobby lobbyToReturn = new Lobby("test", false);
		doReturn(Optional.of(lobbyToReturn))
				.when(lobbyRepository)
				.getLobby(any(UUID.class));

		Lobby result = lobbyService.getLobby(UUID.randomUUID());

		assertEquals(lobbyToReturn, result);
	}

	@Test
	void testGettingNonExistingLobby() {
		doReturn(Optional.empty())
				.when(lobbyRepository)
				.getLobby(any(UUID.class));

		assertThrows(LobbyDoesNotExist.class, () -> lobbyService.getLobby(UUID.randomUUID()));
	}

	@Test
	void testCreatingLobby() {
		doNothing()
				.when(lobbyRepository)
				.addLobby(any(Lobby.class));
		CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest("lobby", true);

		lobbyService.createLobby(createLobbyRequest);
	}

	@Test
	void testJoiningLobby() throws LobbyDoesNotExist, TooManyPlayersInLobbyException {
		Lobby lobby = new Lobby("test", true);
		Player player = new Player();
		doReturn(Optional.of(lobby))
				.when(lobbyRepository)
				.getLobby(any(UUID.class));

		lobbyService.joinLobby(player, lobby.getId());
	}

	@Test
	void testJoiningToNonExistingLobby() {
		doReturn(Optional.empty())
				.when(lobbyRepository)
				.getLobby(any(UUID.class));

		assertThrows(LobbyDoesNotExist.class, () -> lobbyService.joinLobby(new Player(), UUID.randomUUID()));
	}

	@Test
	void testLeavingLobby() throws LobbyDoesNotExist {
		Lobby lobby = new Lobby("test", true);
		Player player = new Player();
		doReturn(Optional.of(lobby))
				.when(lobbyRepository)
				.getLobby(any(UUID.class));

		lobbyService.leaveLobby(player, lobby.getId());
	}

	@Test
	void testLeavingNonExistingLobby() {
		Lobby lobby = new Lobby("test", true);
		Player player = new Player();
		doReturn(Optional.empty())
				.when(lobbyRepository)
				.getLobby(any(UUID.class));

		assertThrows(LobbyDoesNotExist.class, () -> lobbyService.leaveLobby(player, lobby.getId()));
	}

	@Test
	void testDeletingLobby() {
		Lobby lobby = new Lobby("test", true);
		doNothing()
				.when(lobbyRepository)
				.deleteLobby(any(UUID.class));

		lobbyService.deleteLobby(lobby.getId());
	}

}
