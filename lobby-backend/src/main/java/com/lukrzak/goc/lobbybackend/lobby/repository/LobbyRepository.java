package com.lukrzak.goc.lobbybackend.lobby.repository;

import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LobbyRepository {

	Optional<Lobby> getLobby(UUID id);

	List<Lobby> getLobbies();

	void addLobby(Lobby lobby);

	void deleteLobby(UUID id);

}
