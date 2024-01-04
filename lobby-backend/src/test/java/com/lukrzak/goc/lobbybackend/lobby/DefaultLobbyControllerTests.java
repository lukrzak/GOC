package com.lukrzak.goc.lobbybackend.lobby;

import com.lukrzak.goc.lobbybackend.lobby.controller.DefaultLobbyController;
import com.lukrzak.goc.lobbybackend.lobby.controller.LobbyController;
import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbiesResponse;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbyResponse;
import com.lukrzak.goc.lobbybackend.lobby.dto.LobbyResponse;
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
		List<LobbyResponse> lobbiesResponse = generatedLobbies.stream()
				.map(l -> new LobbyResponse(
						l.getId().toString(),
						l.getName(),
						l.isPasswordProtected(),
						l.getPlayers().size()
				))
				.toList();
		doReturn(generatedLobbies)
				.when(lobbyService)
				.getLobbies();

		ResponseEntity<GetLobbiesResponse> response = lobbyController.getLobbies();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(new GetLobbiesResponse(lobbiesResponse), response.getBody());
	}

	@Test
	void testGettingLobby() throws LobbyDoesNotExist, TooManyPlayersInLobbyException {
		Player admin = new Player();
		Lobby lobbyToReturn = new Lobby("test", false, admin);
		List<Player> players = List.of(new Player(), new Player());
		for (Player player : players) {
			lobbyToReturn.addPlayer(player);
		}
		GetLobbyResponse expectedResponse = new GetLobbyResponse(lobbyToReturn.getName(), players, admin);
		doReturn(lobbyToReturn)
				.when(lobbyService)
				.getLobby(any(UUID.class));

		ResponseEntity<GetLobbyResponse> response = lobbyController.getLobby(UUID.randomUUID());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedResponse, response.getBody());
	}

	@Test
	void testGettingNonExistingLobby() throws LobbyDoesNotExist {
		doThrow(LobbyDoesNotExist.class)
				.when(lobbyService)
				.getLobby(any(UUID.class));

		ResponseEntity<GetLobbyResponse> response = lobbyController.getLobby(UUID.randomUUID());

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testCreatingLobby() {
		CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest("lobby-name", true, new Player());
		Lobby lobbyToReturn = new Lobby(createLobbyRequest.name(), createLobbyRequest.passwordProtected(), new Player());
		GetLobbyResponse expectedResponse = new GetLobbyResponse(lobbyToReturn.getName(), lobbyToReturn.getPlayers(), lobbyToReturn.getAdmin());
		doReturn(lobbyToReturn)
				.when(lobbyService)
				.createLobby(any(CreateLobbyRequest.class));

		ResponseEntity<GetLobbyResponse> response = lobbyController.createLobby(createLobbyRequest);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expectedResponse, response.getBody());
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
