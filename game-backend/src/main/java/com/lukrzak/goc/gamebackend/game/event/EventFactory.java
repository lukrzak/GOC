package com.lukrzak.goc.gamebackend.game.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.lukrzak.goc.gamebackend.game.GameUtils;
import com.lukrzak.goc.gamebackend.game.building.Building;
import com.lukrzak.goc.gamebackend.game.building.BuildingFactory;
import com.lukrzak.goc.gamebackend.game.building.BuildingTypes;
import com.lukrzak.goc.gamebackend.game.country.Region;
import com.lukrzak.goc.gamebackend.game.country.Resources;

import java.io.IOException;
import java.util.EnumMap;

public class EventFactory {

	private static final String BUILDING_CONFIG_FILE = "building_config.json";

	public static Event createBuildEvent(Region region, BuildingTypes type, int currentTime) {
		final int HOURS_IN_DAY = 24;
		final String BUILDING_COST_NODE = "buildingCost";
		final String COST_NODE = "cost";
		final String FUNDS_NODE = "funds";
		final String RESOURCES_NODE = "resources";
		final String DAYS_TO_BUILD_NODE = "daysToBuild";

		JsonNode buildingConfig = null;
		try {
			buildingConfig = GameUtils.getConfigNode(BUILDING_CONFIG_FILE,
					BUILDING_COST_NODE, type.toString());
		}
		catch (IOException e) {
			throw new RuntimeException("File " + BUILDING_CONFIG_FILE + " not found");
		}
		EnumMap<Resources, Float> resourcesToConsume
				= GameUtils.jsonNodeToResourcesMap(buildingConfig.get(COST_NODE).get(RESOURCES_NODE));
		float fundsToConsume = buildingConfig.get(COST_NODE).get(FUNDS_NODE).floatValue();
		int timeToFinish = buildingConfig.get(DAYS_TO_BUILD_NODE).asInt() * HOURS_IN_DAY;
		Building buildingOnFinish = BuildingFactory.createBuilding(type);

		Runnable onStart = () -> region.getCountry().consumeResources(fundsToConsume, resourcesToConsume);
		Runnable onFinish = () -> region.finishBuilding(buildingOnFinish);

		return new Event(onStart, onFinish, currentTime + timeToFinish);
	}

}
