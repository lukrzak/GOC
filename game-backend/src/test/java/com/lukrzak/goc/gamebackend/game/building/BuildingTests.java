package com.lukrzak.goc.gamebackend.game.building;

import org.junit.jupiter.api.Test;

public class BuildingTests {

	@Test
	void testCreatingAllBuildingTypes() {
		for (BuildingTypes type: BuildingTypes.values()) {
			BuildingFactory.createBuilding(type);
		}
	}
}
