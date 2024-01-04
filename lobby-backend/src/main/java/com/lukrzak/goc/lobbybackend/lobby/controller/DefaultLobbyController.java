package com.lukrzak.goc.lobbybackend.lobby.controller;

import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbiesResponse;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbyResponse;
import com.lukrzak.goc.lobbybackend.lobby.dto.LobbyResponse;
import com.lukrzak.goc.lobbybackend.lobby.exception.LobbyDoesNotExist;
import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.lobby.service.LobbyService;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.lukrzak.goc.lobbybackend.LobbyBackendApplication.BASE_URL;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(BASE_URL)
public class DefaultLobbyController implements LobbyController{

	private final LobbyService lobbyService;
	private final HttpHeaders plainTextHeader = new HttpHeaders();

	public DefaultLobbyController(LobbyService lobbyService) {
		this.lobbyService = lobbyService;
		plainTextHeader.setContentType(MediaType.TEXT_PLAIN);
	}

	@Override
	public ResponseEntity<GetLobbiesResponse> getLobbies() {
		List<Lobby> lobbies = lobbyService.getLobbies();
		List<LobbyResponse> response = lobbies.stream()
				.map(l -> new LobbyResponse(
						l.getId().toString(),
						l.getName(),
						l.isPasswordProtected(),
						l.getPlayers().size())
				)
				.toList();

		return new ResponseEntity<>(new GetLobbiesResponse(response), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GetLobbyResponse> getLobby(UUID id) {
		GetLobbyResponse response;
		try {
			Lobby lobby = lobbyService.getLobby(id);
			response = new GetLobbyResponse(lobby.getName(), lobby.getPlayers(), lobby.getAdmin());
		}
		catch (LobbyDoesNotExist e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<LobbyResponse> createLobby(CreateLobbyRequest createLobbyRequest) {
		Lobby lobby = lobbyService.createLobby(createLobbyRequest);
		LobbyResponse response = new LobbyResponse(lobby.getId().toString(), lobby.getName(), lobby.isPasswordProtected(), lobby.getPlayers().size());

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> joinLobby(Player player, UUID id) {
		try {
			lobbyService.joinLobby(player, id);
		}
		catch (TooManyPlayersInLobbyException | LobbyDoesNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Joined lobby " + id.toString(), plainTextHeader, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> leaveLobby(Player player, UUID id) {
		try {
			lobbyService.leaveLobby(player, id);
		}
		catch (LobbyDoesNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Left lobby " + id.toString(), plainTextHeader, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deleteLobby(UUID id) {
		lobbyService.deleteLobby(id);

		return new ResponseEntity<>("Deleted lobby " + id.toString(), plainTextHeader, HttpStatus.OK);
	}

}
