package com.lukrzak.goc.gamebackend.game;

import com.lukrzak.goc.gamebackend.game.building.Building;
import com.lukrzak.goc.gamebackend.game.building.BuildingFactory;
import com.lukrzak.goc.gamebackend.game.building.BuildingTypes;
import com.lukrzak.goc.gamebackend.game.country.Country;
import com.lukrzak.goc.gamebackend.game.country.NotEnoughResourcesException;
import com.lukrzak.goc.gamebackend.game.country.Region;
import com.lukrzak.goc.gamebackend.game.country.Resources;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CountryTests {

	@Test
	void testAssigningRegion() {
		Country country = new Country(1, "country", 0.0f);
		Region region = new Region();

		country.assignRegionToCountry(region);

		assertTrue(country.getRegions().contains(region));
		assertEquals(1, country.getRegions().size());
		assertEquals(region.getCountry(), country);
	}

	@Test
	void testUpdatingCountry() {
		Country country = new Country(1, "country", 0.0f);
		Region r1 = new Region(1, "r1", 100, 2, country);
		Region r2 = new Region(2, "r2", 50, 1, country);
		EnumMap<Resources, Float> resourcesToChange = GameUtils.getDefaultResourcesMap();
		resourcesToChange.put(Resources.OIL, 1.0f);
		country.setResourcesChange(resourcesToChange);
		int expectedR1Population = 102;
		int expectedR2Population = 51;
		float expectedFundsValue = (expectedR1Population + expectedR2Population) * country.getTaxRate();
		float expectedOilQuantity = country.getResources().get(Resources.OIL) + 1.0f;

		country.update();

		assertEquals(expectedR1Population, r1.getPopulation());
		assertEquals(expectedR2Population, r2.getPopulation());
		assertEquals(expectedFundsValue, country.getFunds());
		assertEquals(expectedOilQuantity, country.getResources().get(Resources.OIL));
	}

	@Test
	void testConsumingResources() {
		BiFunction<Float, Float, Float> subtract = (a, b) -> a - b;
		float startingFunds = 100.0f;
		float fundsToConsume = 20.0f;
		float expectedFundsValue = startingFunds - fundsToConsume;
		Country country = new Country(1, "country", startingFunds);
		// Prepare enum map with resources to subtract from country
		EnumMap<Resources, Float> resourcesToConsume = GameUtils.getDefaultResourcesMap();
		resourcesToConsume.put(Resources.OIL, 2.0f);
		resourcesToConsume.put(Resources.PLASTIC, 1.0f);
		// Prepare enum map with resources, which country should have after subtracting
		EnumMap<Resources, Float> expectedResources = new EnumMap<>(Resources.class);
		expectedResources.putAll(country.getResources());
		resourcesToConsume.forEach((k, v) -> expectedResources.merge(k, v, subtract));

		try{
			country.consumeResources(fundsToConsume, resourcesToConsume);
		}
		catch (NotEnoughResourcesException e) {
			fail();
		}

		assertEquals(expectedResources, country.getResources());
		assertEquals(expectedFundsValue, country.getFunds());
	}

	@Test
	void testConsumingWithNotEnoughResources() {
		float maxResourcesForCountry = 50.0f;
		float startingFunds = 100.0f;
		float fundsToConsume = 110.0f;
		Country country = new Country(1, "country", 100.0f);
		EnumMap<Resources, Float> resourcesToConsume = GameUtils.getDefaultResourcesMap();
		resourcesToConsume.put(Resources.OIL, maxResourcesForCountry + 1.0f);
		EnumMap<Resources, Float> expectedResources = GameUtils.getDefaultResourcesMap();
		expectedResources.putAll(country.getResources());

		assertThrows(NotEnoughResourcesException.class, () -> country.consumeResources(fundsToConsume, resourcesToConsume));
		assertEquals(expectedResources, country.getResources());
		assertEquals(startingFunds, country.getFunds());
	}

	@Test
	void testRecalculatingResourceChange() {
		Country country = new Country(1, "country", 100.0f);
		Region region = new Region(1, "region", 100, 0.0f, country);
		Building farm = BuildingFactory.createBuilding(BuildingTypes.FARM);
		Building assembly = BuildingFactory.createBuilding(BuildingTypes.ASSEMBLY);
		region.getBuildings().add(farm);
		region.getBuildings().add(assembly);
		float expectedFundsChange =
				farm.getBuildingEffect().getFundsChange() + assembly.getBuildingEffect().getFundsChange();
		EnumMap<Resources, Float> expectedResourcesChange = new EnumMap<>(Resources.class);
		farm.getBuildingEffect().getResourcesChange()
				.forEach((k, v) -> expectedResourcesChange.merge(k, v, Float::sum));

		country.recalculateResourceChanges();

		assertEquals(expectedFundsChange, country.getFundsChange());
		assertEquals(expectedResourcesChange, country.getResourcesChange());
	}

}
