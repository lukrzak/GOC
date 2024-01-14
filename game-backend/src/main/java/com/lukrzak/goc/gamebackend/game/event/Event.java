package com.lukrzak.goc.gamebackend.game.event;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Event {

	private Runnable onStart;
	private Runnable onEnd;
	private int finishDate;

}
