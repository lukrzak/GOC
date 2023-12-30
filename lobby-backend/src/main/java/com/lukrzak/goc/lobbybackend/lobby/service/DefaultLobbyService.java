package com.lukrzak.goc.lobbybackend.lobby.service;

import com.lukrzak.goc.lobbybackend.lobby.dto.CreateLobbyRequest;
import com.lukrzak.goc.lobbybackend.lobby.exception.LobbyDoesNotExist;
import com.lukrzak.goc.lobbybackend.lobby.exception.TooManyPlayersInLobbyException;
import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import com.lukrzak.goc.lobbybackend.lobby.repository.LobbyRepository;
import com.lukrzak.goc.lobbybackend.player.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DefaultLobbyService implements LobbyService {

	private final LobbyRepository lobbyRepository;

	public DefaultLobbyService(LobbyRepository lobbyRepository) {
		this.lobbyRepository = lobbyRepository;
	}

	@Override
	public List<Lobby> getLobbies() {
		return lobbyRepository.getLobbies();
	}

	@Override
	public void createLobby(CreateLobbyRequest createLobbyRequest) {
		Lobby newLobby = new Lobby(
				createLobbyRequest.name(),
				createLobbyRequest.passwordProtected()
		);

		lobbyRepository.addLobby(newLobby);
	}

	@Override
	public void joinLobby(Player player, UUID id) throws TooManyPlayersInLobbyException, LobbyDoesNotExist {
		Lobby lobbyToJoin = lobbyRepository.getLobby(id)
				.orElseThrow(() -> new LobbyDoesNotExist(id));

		lobbyToJoin.addPlayer(player);
	}

	@Override
	public void leaveLobby(Player player, UUID id) throws LobbyDoesNotExist {
		Lobby lobbyToLeave = lobbyRepository.getLobby(id)
				.orElseThrow(() -> new LobbyDoesNotExist(id));

		lobbyToLeave.removePlayer(player);
	}

	@Override
	public void deleteLobby(UUID id) {
		lobbyRepository.deleteLobby(id);
	}

}
