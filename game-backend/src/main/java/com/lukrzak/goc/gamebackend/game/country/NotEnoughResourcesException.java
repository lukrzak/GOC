package com.lukrzak.goc.gamebackend.game.country;

public class NotEnoughResourcesException extends Exception {

	public NotEnoughResourcesException(Resources resource) {
		super("Not enough resource: " + resource);
	}

}
