package com.lukrzak.goc.gamebackend.game.country;

import com.lukrzak.goc.gamebackend.game.building.Building;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Region {

	private int id;
	private String name;
	private int population;
	private int populationChange;
	private Country country;
	private List<Building> buildings = new ArrayList<>();

	public void finishBuilding(Building building) {
		buildings.add(building);
		country.recalculateResourceChanges();
	}

}
