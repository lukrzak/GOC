package com.lukrzak.goc.lobbybackend.lobby.repository;

import com.lukrzak.goc.lobbybackend.lobby.model.Lobby;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class LobbyRepositoryImpl implements LobbyRepository{

	private List<Lobby> existingLobbies = new ArrayList<>();

	public Optional<Lobby> getLobby(UUID id) {
		return existingLobbies.stream()
				.filter(l -> l.getId().equals(id))
				.findFirst();
	}

	public List<Lobby> getLobbies() {
		return new ArrayList<>(existingLobbies);
	}

	public void addLobby(Lobby lobby) {
		existingLobbies.add(lobby);
	}

	public void deleteLobby(UUID id) {
		Optional<Lobby> lobbyToDelete = existingLobbies.stream()
				.filter(l -> l.getId().equals(id))
				.findFirst();

		lobbyToDelete.ifPresent(lobby -> existingLobbies.remove(lobby));
	}

	public void clearLobbies() {
		existingLobbies.clear();
	}
}
