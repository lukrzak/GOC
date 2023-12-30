package com.lukrzak.goc.lobbybackend.lobby;

import com.lukrzak.goc.lobbybackend.lobby.controller.DefaultLobbyController;
import com.lukrzak.goc.lobbybackend.lobby.controller.LobbyController;
import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbiesResponse;
import com.lukrzak.goc.lobbybackend.lobby.exception.LobbyDoesNotExist;
import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.lobby.service.LobbyService;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class DefaultLobbyControllerTests {

	private final LobbyService lobbyService = mock(LobbyService.class);
	private final LobbyController lobbyController = new DefaultLobbyController(lobbyService);

	@Test
	void testGettingLobbies() {
		final int AMOUNT_OF_LOBBIES = 5;
		List<Lobby> generatedLobbies = TestUtils.generateLobbies(AMOUNT_OF_LOBBIES);
		doReturn(generatedLobbies)
				.when(lobbyService)
				.getLobbies();

		ResponseEntity<GetLobbiesResponse> response = lobbyController.getLobbies();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(new GetLobbiesResponse(generatedLobbies), response.getBody());
	}

	@Test
	void testCreatingLobby() {
		CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest("lobby-name", true);
		doNothing()
				.when(lobbyService)
				.createLobby(any(CreateLobbyRequest.class));

		ResponseEntity<String> response = lobbyController.createLobby(createLobbyRequest);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void testJoiningLobby() throws LobbyDoesNotExist, TooManyPlayersInLobbyException {
		doNothing()
				.when(lobbyService)
				.joinLobby(any(Player.class), any(UUID.class));

		ResponseEntity<String> response = lobbyController.joinLobby(new Player(), UUID.randomUUID());

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testJoiningNonExistingLobby() throws LobbyDoesNotExist, TooManyPlayersInLobbyException {
		doThrow(LobbyDoesNotExist.class)
				.when(lobbyService)
				.joinLobby(any(Player.class), any(UUID.class));

		ResponseEntity<String> response = lobbyController.joinLobby(new Player(), UUID.randomUUID());

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testJoiningLobbyWithTooManyPlayers() throws LobbyDoesNotExist, TooManyPlayersInLobbyException {
		doThrow(TooManyPlayersInLobbyException.class)
				.when(lobbyService)
				.joinLobby(any(Player.class), any(UUID.class));

		ResponseEntity<String> response = lobbyController.joinLobby(new Player(), UUID.randomUUID());

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
