package com.lukrzak.goc.lobbybackend.lobby.controller;

import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbiesResponse;
import com.lukrzak.goc.lobbybackend.lobby.exception.LobbyDoesNotExist;
import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.lobby.service.LobbyService;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class DefaultLobbyController implements LobbyController{

	private final LobbyService lobbyService;

	public DefaultLobbyController(LobbyService lobbyService) {
		this.lobbyService = lobbyService;
	}

	@Override
	public ResponseEntity<GetLobbiesResponse> getLobbies() {
		List<Lobby> lobbies = lobbyService.getLobbies();

		return new ResponseEntity<>(new GetLobbiesResponse(lobbies), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> createLobby(CreateLobbyRequest createLobbyRequest) {
		lobbyService.createLobby(createLobbyRequest);

		return new ResponseEntity<>("Lobby " + createLobbyRequest.name() + "created successfully", HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> joinLobby(Player player, UUID id) {
		try {
			lobbyService.joinLobby(player, id);
		}
		catch (TooManyPlayersInLobbyException | LobbyDoesNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Joined lobby " + id.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> leaveLobby(Player player, UUID id) {
		try {
			lobbyService.leaveLobby(player, id);
		}
		catch (LobbyDoesNotExist e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Left lobby " + id.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deleteLobby(UUID id) {
		lobbyService.deleteLobby(id);

		return new ResponseEntity<>("Deleted lobby " + id.toString(), HttpStatus.OK);
	}

}
