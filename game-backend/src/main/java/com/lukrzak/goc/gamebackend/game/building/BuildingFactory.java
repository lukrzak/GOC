package com.lukrzak.goc.gamebackend.game.building;

import com.fasterxml.jackson.databind.JsonNode;
import com.lukrzak.goc.gamebackend.game.GameUtils;
import com.lukrzak.goc.gamebackend.game.country.Resources;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

public class BuildingFactory {

	private static final String BUILDING_CONFIG_FILE = "building_config.json";

	static Building createBuilding(BuildingTypes type) {
		BuildingEffect buildingEffect = null;
		try {
			buildingEffect = getBuildingEffect(type);
		}
		catch (IOException e) {
			throw new RuntimeException("File " + BUILDING_CONFIG_FILE + " is missing");
		}

		String buildingName = prettifyBuildingName(type);
		return new Building(buildingName, buildingEffect);
	}

	private static BuildingEffect getBuildingEffect(BuildingTypes type) throws IOException {
		final String BUILDING_COST_NODE_NAME = "buildingCost";
		final String FUNDS_CHANGE_NODE_NAME = "fundsChange";
		final String POPULATION_CHANGE_NODE_NAME = "populationChange";
		final String RESOURCES_CHANGE_NODE_NAME = "resourcesChange";

		JsonNode node = GameUtils.getConfigNode(BUILDING_CONFIG_FILE, BUILDING_COST_NODE_NAME, type.toString());
		if (node == null) {
			System.err.println("Invalid building type");
		}

		float fundsChange = (float) node.get(FUNDS_CHANGE_NODE_NAME).asDouble();
		int populationChange = node.get(POPULATION_CHANGE_NODE_NAME).asInt();
		EnumMap<Resources, Float> resourcesChange = new EnumMap<>(Resources.class);
		Iterator<Map.Entry<String, JsonNode>> elements = node.path(RESOURCES_CHANGE_NODE_NAME).fields();

		while(elements.hasNext()) {
			Map.Entry<String, JsonNode> entry = elements.next();
			Resources resource = Resources.valueOf(entry.getKey());
			float amount = entry.getValue().floatValue();
			resourcesChange.put(resource, amount);
		}

		return new BuildingEffect(fundsChange, populationChange, resourcesChange);
	}

	private static String prettifyBuildingName(BuildingTypes type) {
		return type.toString()
				.charAt(0)
				+ type.toString()
				.substring(1)
				.replace("_", " ")
				.toLowerCase();
	}
}
