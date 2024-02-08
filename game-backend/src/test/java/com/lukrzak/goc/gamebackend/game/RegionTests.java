package com.lukrzak.goc.gamebackend.game;

import com.lukrzak.goc.gamebackend.game.building.Building;
import com.lukrzak.goc.gamebackend.game.building.BuildingFactory;
import com.lukrzak.goc.gamebackend.game.building.BuildingTypes;
import com.lukrzak.goc.gamebackend.game.country.Country;
import com.lukrzak.goc.gamebackend.game.country.Region;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegionTests {

	private final String BUILDING_CONFIG_FILENAME = "building_config.json";

	@Test
	void testFinishingBuilding() throws IOException {
		Country country = new Country(1, "country", 100.0f);
		Region region = new Region();
		country.assignRegionToCountry(region);
		Building building = BuildingFactory.createBuilding(BuildingTypes.FARM);
		int expectedPopulationChange = GameUtils.getConfigNode(BUILDING_CONFIG_FILENAME,
				"buildingCost", building.getName().toUpperCase(), "populationChange").asInt();
		float expectedFundsChange = GameUtils.getConfigNode(BUILDING_CONFIG_FILENAME,
				"buildingCost", building.getName().toUpperCase(), "fundsChange").floatValue();

		region.finishBuilding(building);

		assertTrue(region.getBuildings().contains(building));
		assertEquals(expectedFundsChange, country.getFundsChange());
		assertEquals(expectedPopulationChange, region.getPopulationChange());
	}

}
