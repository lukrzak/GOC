package com.lukrzak.goc.gamebackend.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.lukrzak.goc.gamebackend.game.GameUtils;
import com.lukrzak.goc.gamebackend.game.building.Building;
import com.lukrzak.goc.gamebackend.game.building.BuildingEffect;
import com.lukrzak.goc.gamebackend.game.building.BuildingFactory;
import com.lukrzak.goc.gamebackend.game.building.BuildingTypes;
import com.lukrzak.goc.gamebackend.game.country.Resources;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuildingTests {

	@Test
	void testCreatingAllBuildingTypes() {
		for (BuildingTypes type: BuildingTypes.values()) {
			BuildingFactory.createBuilding(type);
		}
	}

	@Test
	void testCreatingWaterPump() throws IOException {
		String expectedName = "Water pump";
		JsonNode expectedEffects = GameUtils.getConfigNode("building_config.json",
				"buildingCost", "WATER_PUMP");
		EnumMap<Resources, Float> expectedResourceChange = new EnumMap<>(Resources.class);
		Iterator<Map.Entry<String, JsonNode>> elements = expectedEffects.path("resourcesChange").fields();
		while(elements.hasNext()) {
			Map.Entry<String, JsonNode> entry = elements.next();
			Resources resource = Resources.valueOf(entry.getKey());
			float amount = entry.getValue().floatValue();
			expectedResourceChange.put(resource, amount);
		}
		BuildingEffect expectedEffect = new BuildingEffect(
				expectedEffects.get("fundsChange").floatValue(),
				expectedEffects.get("populationChange").asInt(),
				expectedResourceChange
		);

		Building building  = BuildingFactory.createBuilding(BuildingTypes.WATER_PUMP);

		assertEquals(expectedName, building.getName());
		assertEquals(expectedEffect, building.getBuildingEffect());
	}
}
