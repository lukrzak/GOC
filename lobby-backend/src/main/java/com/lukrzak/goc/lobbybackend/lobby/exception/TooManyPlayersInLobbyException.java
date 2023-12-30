package com.lukrzak.goc.lobbybackend.lobby.exception;

public class TooManyPlayersInLobbyException extends Exception {

	public TooManyPlayersInLobbyException(String lobbyName) {
		super("Too many players in the lobby " + lobbyName + ". Cannot join");
	}

}
