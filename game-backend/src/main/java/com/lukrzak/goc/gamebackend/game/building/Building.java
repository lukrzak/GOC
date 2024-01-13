package com.lukrzak.goc.gamebackend.game.building;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Building {

	private String name;
	private final BuildingEffect buildingEffect;

}
