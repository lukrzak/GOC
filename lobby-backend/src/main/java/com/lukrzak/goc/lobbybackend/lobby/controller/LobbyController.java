package com.lukrzak.goc.lobbybackend.lobby.controller;

import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbiesResponse;
import com.lukrzak.goc.lobbybackend.lobby.dto.GetLobbyResponse;
import com.lukrzak.goc.lobbybackend.lobby.dto.LobbyResponse;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface LobbyController {

	@GetMapping("/lobbies")
	ResponseEntity<GetLobbiesResponse> getLobbies();

	@GetMapping("/lobbies/{id}")
	ResponseEntity<GetLobbyResponse> getLobby(@PathVariable UUID id);

	@PostMapping("/lobbies")
	ResponseEntity<LobbyResponse> createLobby(@RequestBody CreateLobbyRequest createLobbyRequest);

	@PostMapping("/lobbies/join/{id}")
	ResponseEntity<String> joinLobby(@RequestBody Player player, @PathVariable UUID id);

	@PostMapping("/lobbies/leave/{id}")
	ResponseEntity<String> leaveLobby(@RequestBody Player player, @PathVariable UUID id);

	@DeleteMapping("/lobbies/{id}")
	ResponseEntity<String> deleteLobby(@PathVariable UUID id);

}
