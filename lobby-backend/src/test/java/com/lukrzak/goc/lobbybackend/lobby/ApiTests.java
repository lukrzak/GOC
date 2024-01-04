package com.lukrzak.goc.lobbybackend.lobby;

import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbiesResponse;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbyResponse;
import com.lukrzak.goc.lobbybackend.lobby.dto.LobbyResponse;
import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.lobby.repository.LobbyRepository;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static com.lukrzak.goc.lobbybackend.LobbyBackendApplication.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTests {

	private final String LOBBIES_URL = BASE_URL + "/lobbies";
	@Autowired
	private WebTestClient webTestClient;
	@Autowired
	private LobbyRepository lobbyRepository;

	@AfterEach
	void clean() {
		lobbyRepository.clearLobbies();
	}

	@Test
	void testGettingLobbies() {
		final int LOBBIES_TO_GENERATE = 3;
		List<Lobby> generatedLobbies = TestUtils.generateLobbies(LOBBIES_TO_GENERATE);
		List<LobbyResponse> expectedResponse = generatedLobbies.stream()
				.map(l -> new LobbyResponse(
						l.getId().toString(),
						l.getName(),
						l.isPasswordProtected(),
						l.getPlayers().size()
				))
				.toList();
		generatedLobbies.forEach(lobbyRepository::addLobby);

		GetLobbiesResponse response = (GetLobbiesResponse) sendGetRequest(LOBBIES_URL, GetLobbiesResponse.class, HttpStatus.OK);

		assertEquals(LOBBIES_TO_GENERATE, response.lobbies().size());
		assertEquals(expectedResponse, response.lobbies());
	}

	@Test
	void testGettingLobby() throws TooManyPlayersInLobbyException {
		Lobby lobby = new Lobby("lobby", false, new Player());
		List<Player> players = List.of(new Player());
		for(Player player : players) {
			lobby.addPlayer(player);
		}
		GetLobbyResponse expectedResponse = new GetLobbyResponse(lobby.getName(), lobby.getPlayers(), lobby.getAdmin());
		lobbyRepository.addLobby(lobby);
		final String GET_LOBBY_URL = LOBBIES_URL + "/" + lobby.getId().toString();

		GetLobbyResponse response = (GetLobbyResponse) sendGetRequest(GET_LOBBY_URL, GetLobbyResponse.class, HttpStatus.OK);

		assertEquals(expectedResponse, response);
	}

	@Test
	void testGettingNonExistingLobby() {
		final String GET_LOBBY_URL = LOBBIES_URL + "/" + UUID.randomUUID();

		GetLobbyResponse response = (GetLobbyResponse) sendGetRequest(GET_LOBBY_URL, GetLobbyResponse.class, HttpStatus.BAD_REQUEST);

		assertNull(response);
	}

	@Test
	void testCreatingLobby() {
		CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest("lobby-1", false, new Player());
		GetLobbyResponse expectedResponse = new GetLobbyResponse(createLobbyRequest.name(), List.of(), createLobbyRequest.admin());

		GetLobbyResponse response = (GetLobbyResponse) sendPostRequest(LOBBIES_URL, createLobbyRequest, HttpStatus.CREATED, GetLobbyResponse.class);

		assertEquals(expectedResponse, response);
		assertEquals(1, lobbyRepository.getLobbies().size());
	}

	@Test
	void testJoiningToLobby() {
		Player player = new Player("playerName");
		Lobby existingLobby = new Lobby("lobby-1", true, new Player());
		final String JOIN_LOBBY_URI = LOBBIES_URL + "/join/" + existingLobby.getId().toString();
		lobbyRepository.addLobby(existingLobby);

		String response = (String) sendPostRequest(JOIN_LOBBY_URI, player, HttpStatus.OK, String.class);

		assertTrue(existingLobby.getPlayers().contains(player));
		assertEquals(1, existingLobby.getPlayers().size());
		assertEquals("Joined lobby " + existingLobby.getId().toString(), response);
	}

	@Test
	void testLeavingLobby() throws TooManyPlayersInLobbyException {
		Player player = new Player("playerName");
		Lobby existingLobby = new Lobby("lobby-1", true, new Player());
		existingLobby.addPlayer(player);
		final String LEAVE_LOBBY_URI = LOBBIES_URL + "/leave/" + existingLobby.getId().toString();
		lobbyRepository.addLobby(existingLobby);

		String response = (String) sendPostRequest(LEAVE_LOBBY_URI, player, HttpStatus.OK, String.class);

		assertEquals(0, existingLobby.getPlayers().size());
		assertEquals("Left lobby " + existingLobby.getId().toString(), response);
	}

	@Test
	void testJoiningByTooManyPlayers() throws TooManyPlayersInLobbyException {
		Lobby lobby = new Lobby("lobby", false, new Player());
		final int MAX_PLAYERS_IN_LOBBY = 4;
		final String JOIN_LOBBY_URI = LOBBIES_URL + "/join/" + lobby.getId().toString();
		Player exceedingPlayer = new Player("player");
		lobbyRepository.addLobby(lobby);
		for (int i = 0; i < MAX_PLAYERS_IN_LOBBY; i++) {
			lobby.addPlayer(new Player("player-" + i));
		}

		String response = (String) sendPostRequest(JOIN_LOBBY_URI, exceedingPlayer, HttpStatus.BAD_REQUEST, String.class);

		assertEquals("Too many players in the lobby " + lobby.getName() + ". Cannot join", response);
	}

	@Test
	void testPerformingOnNonExistingLobby() {
		Player player = new Player("player");
		String incorrectLobbyId = UUID.randomUUID().toString();
		final String JOIN_LOBBY_URI = LOBBIES_URL + "/join/" + incorrectLobbyId;
		final String LEAVE_LOBBY_URI = LOBBIES_URL + "/leave/" + incorrectLobbyId;

		String responseOnJoin = (String) sendPostRequest(JOIN_LOBBY_URI, player, HttpStatus.BAD_REQUEST, String.class);
		String responseOnLeave = (String) sendPostRequest(LEAVE_LOBBY_URI, player, HttpStatus.BAD_REQUEST, String.class);

		assertEquals("Lobby with id: " + incorrectLobbyId + " does not exist", responseOnJoin);
		assertEquals("Lobby with id: " + incorrectLobbyId + " does not exist", responseOnLeave);
	}

	@Test
	void testLeavingLobbyByNonParticipant() {
		Player playerWithoutLobby = new Player("player");
		Lobby lobby = new Lobby("lobby", false, new Player());
		final String LEAVE_LOBBY_URI = LOBBIES_URL + "/leave/" + lobby.getId().toString();
		lobbyRepository.addLobby(lobby);

		String response = (String) sendPostRequest(LEAVE_LOBBY_URI, playerWithoutLobby, HttpStatus.OK, String.class);

		assertEquals("Left lobby " + lobby.getId().toString(), response);
	}

	@Test
	void testDeletingLobby() {
		Lobby lobby = new Lobby("lobby", false, new Player());
		final String DELETE_LOBBY_URI = LOBBIES_URL + "/" + lobby.getId().toString();
		lobbyRepository.addLobby(lobby);

		String response = sendDeleteRequest(DELETE_LOBBY_URI, HttpStatus.OK);

		assertFalse(lobbyRepository.getLobbies().contains(lobby));
		assertEquals(0, lobbyRepository.getLobbies().size());
		assertEquals("Deleted lobby " + lobby.getId().toString(), response);
	}

	@Test
	void testDeletingNonExistingLobby() {
		String incorrectLobbyId = UUID.randomUUID().toString();
		final String DELETE_LOBBY_URI = LOBBIES_URL + "/" + incorrectLobbyId;

		String response = sendDeleteRequest(DELETE_LOBBY_URI, HttpStatus.OK);

		assertEquals("Deleted lobby " + incorrectLobbyId, response);
	}

	private Object sendPostRequest(String uri, Object body, HttpStatus expectedStatus, Class<?> expectedBodyType) {
		return webTestClient.post()
				.uri(uri)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.isEqualTo(expectedStatus)
				.expectBody(expectedBodyType)
				.returnResult()
				.getResponseBody();
	}

	private Object sendGetRequest(String uri, Class<?> expectedBodyType, HttpStatus expectedStatus) {
		return webTestClient.get()
				.uri(uri)
				.exchange()
				.expectStatus()
				.isEqualTo(expectedStatus)
				.expectBody(expectedBodyType)
				.returnResult()
				.getResponseBody();
	}

	private String sendDeleteRequest(String uri, HttpStatus expectedStatus) {
		return webTestClient.delete()
				.uri(uri)
				.exchange()
				.expectStatus()
				.isEqualTo(expectedStatus)
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
	}

}
