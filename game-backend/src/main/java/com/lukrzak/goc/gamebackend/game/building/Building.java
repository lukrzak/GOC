package com.lukrzak.goc.gamebackend.game.building;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Building {

	private String name;
	private final BuildingEffect buildingEffect;

}