package com.lukrzak.goc.lobbybackend.lobby.exception;

import java.util.UUID;

public class LobbyDoesNotExist extends Exception {

	public LobbyDoesNotExist(UUID id) {
		super("Lobby with id: " + id.toString() + " does not exist");
	}

}
