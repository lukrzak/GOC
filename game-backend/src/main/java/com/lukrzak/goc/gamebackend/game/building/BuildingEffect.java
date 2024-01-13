package com.lukrzak.goc.gamebackend.game.building;

import com.lukrzak.goc.gamebackend.game.country.Resources;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.EnumMap;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class BuildingEffect {

	private float fundsChange;
	private int populationChange;
	private EnumMap<Resources, Float> resourcesChange;

}
