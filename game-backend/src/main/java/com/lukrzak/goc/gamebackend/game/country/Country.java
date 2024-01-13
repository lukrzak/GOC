package com.lukrzak.goc.gamebackend.game.country;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class Country {

	private long id;
	private String name;
	private final List<Region> regions = new ArrayList<>();
	private final EnumMap<Resources, Long> resources = new EnumMap<>(Resources.class);

}
