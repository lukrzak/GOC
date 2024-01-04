package com.lukrzak.goc.lobbybackend.lobby.service;

import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.exception.LobbyDoesNotExist;
import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface LobbyService {

	List<Lobby> getLobbies();

	Lobby getLobby(UUID id) throws LobbyDoesNotExist;

	Lobby createLobby(CreateLobbyRequest createLobbyRequest);

	void joinLobby(@RequestBody Player player, @PathVariable UUID id) throws TooManyPlayersInLobbyException, LobbyDoesNotExist;

	void leaveLobby(@RequestBody Player player, @PathVariable UUID id) throws LobbyDoesNotExist;

	void deleteLobby(@PathVariable UUID id);

}
