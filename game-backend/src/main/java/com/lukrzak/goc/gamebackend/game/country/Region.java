package com.lukrzak.goc.gamebackend.game.country;

import com.lukrzak.goc.gamebackend.game.building.Building;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Region {

	private int id;
	private String name;
	private int population;
	private float populationChange;
	private Country country;
	private final List<Building> buildings = new ArrayList<>();

	public Region(int id, String name, int population, float populationChange, Country country) {
		this.id = id;
		this.name = name;
		this.population = population;
		this.populationChange = populationChange;
		country.assignRegionToCountry(this);
	}

	public void finishBuilding(Building building) {
		buildings.add(building);
		country.recalculateResourceChanges();
	}

}
