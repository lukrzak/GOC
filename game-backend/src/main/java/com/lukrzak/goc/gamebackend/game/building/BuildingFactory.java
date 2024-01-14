package com.lukrzak.goc.gamebackend.game.building;

import com.fasterxml.jackson.databind.JsonNode;
import com.lukrzak.goc.gamebackend.game.GameUtils;
import com.lukrzak.goc.gamebackend.game.country.Resources;

import java.io.IOException;
import java.util.EnumMap;

public class BuildingFactory {

	private static final String BUILDING_CONFIG_FILE = "building_config.json";

	public static Building createBuilding(BuildingTypes type) {
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
			throw new RuntimeException("Incorrect config json path");
		}

		float fundsChange = (float) node.get(FUNDS_CHANGE_NODE_NAME).asDouble();
		int populationChange = node.get(POPULATION_CHANGE_NODE_NAME).asInt();
		EnumMap<Resources, Float> resourcesChange
				= GameUtils.jsonNodeToResourcesMap(node.path(RESOURCES_CHANGE_NODE_NAME));

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
