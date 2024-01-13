package com.lukrzak.goc.gamebackend.game.building;

import com.lukrzak.goc.gamebackend.game.country.Resources;

import java.util.EnumMap;

public record BuildingEffect(
		Float fundsChange,
		Integer populationChange,
		EnumMap<Resources, Float> resourcesChange
		) {
}
