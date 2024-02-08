package com.lukrzak.goc.gamebackend.game.country;

import com.lukrzak.goc.gamebackend.game.GameUtils;
import com.lukrzak.goc.gamebackend.game.building.Building;
import com.lukrzak.goc.gamebackend.game.building.BuildingEffect;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
public class Country {

	private int id;
	private String name;
	private float funds;
	private float fundsChange;
	private float taxRate;
	private final List<Region> regions = new ArrayList<>();
	private final EnumMap<Resources, Float> resources = new EnumMap<>(Resources.class);
	private EnumMap<Resources, Float> resourcesChange = GameUtils.getDefaultResourcesMap();

	public Country(int id, String name, float startingFunds) {
		this.id = id;
		this.name = name;
		this.funds = startingFunds;
		this.fundsChange = 0.0f;
		this.taxRate = 0.1f;

		initializeStartingResources();
	}

	public void assignRegionToCountry(Region region) {
		this.regions.add(region);
		region.setCountry(this);
	}

	public void update() {
		funds += fundsChange;
		resourcesChange.forEach((k, v) -> resources.merge(k, v, Float::sum));

		int totalPopulation = 0;
		for (Region region: regions) {
			int regionNewPopulation = (int) (Math.floor(region.getPopulationChange()) + region.getPopulation());
			region.setPopulation(regionNewPopulation);
			totalPopulation += regionNewPopulation;
		}
		funds += totalPopulation * taxRate;
	}

	public void consumeResources(float fundsToConsume, EnumMap<Resources, Float> resourcesToConsume)
			throws NotEnoughResourcesException {
		EnumMap<Resources, Float> changedResources = GameUtils.getDefaultResourcesMap();
		for (Map.Entry<Resources, Float> resource: resourcesToConsume.entrySet()) {
			float updatedResourceAmount = resources.get(resource.getKey()) - resource.getValue();
			if (updatedResourceAmount < 0) throw new NotEnoughResourcesException(resource.getKey());
			changedResources.put(resource.getKey(), updatedResourceAmount);
		}

		funds -= fundsToConsume;
		resources.putAll(changedResources);
	}

	public void recalculateResourceChanges() {
		EnumMap<Resources, Float> newResourceChange = GameUtils.getDefaultResourcesMap();
		float newFundsChange = 0.0f;

		for (Region region: regions) {
			int regionPopulationChange = 0;
			for (Building building: region.getBuildings()) {
				insertBuildingEffectResourceChange(building.getBuildingEffect(), newResourceChange);
				newFundsChange += building.getBuildingEffect().getFundsChange();
				regionPopulationChange += building.getBuildingEffect().getPopulationChange();
			}
			region.setPopulationChange(region.getPopulationChange() + regionPopulationChange);
		}

		this.fundsChange = newFundsChange;
		resourcesChange.putAll(newResourceChange);
	}

	private void insertBuildingEffectResourceChange(
			BuildingEffect effect, EnumMap<Resources, Float> recalculatedResourceChange) {
		for (Map.Entry<Resources, Float> resourceEntry: effect.getResourcesChange().entrySet()) {
			float newResourceValue = recalculatedResourceChange.get(resourceEntry.getKey()) + resourceEntry.getValue();
			recalculatedResourceChange.put(resourceEntry.getKey(), newResourceValue);
		}
	}

	private void initializeStartingResources() {
		Random random = new Random();
		final float MIN_RESOURCES_FOR_COUNTRY = 20.0f;
		final float MAX_RESOURCES_FOR_COUNTRY = 50.0f;
		for (Resources resource: Resources.values()) {
			float generatedResourceAmount = random.nextFloat(MIN_RESOURCES_FOR_COUNTRY, MAX_RESOURCES_FOR_COUNTRY);
			resources.put(resource, generatedResourceAmount);
		}
	}

}
