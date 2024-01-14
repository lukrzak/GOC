package com.lukrzak.goc.gamebackend.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukrzak.goc.gamebackend.game.country.Resources;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

public class GameUtils {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	public static JsonNode getConfigNode(String configFileName, String... nodeKeyPath) throws IOException {
		JsonNode node = objectMapper.readTree(new File(configFileName));

		for (String key: nodeKeyPath) {
			node = node.get(key);
		}

		return node;
	}

	public static EnumMap<Resources, Float> jsonNodeToResourcesMap(JsonNode node) {
		EnumMap<Resources, Float> resourcesMap = new EnumMap<>(Resources.class);
		Iterator<Map.Entry<String, JsonNode>> elements = node.fields();

		while(elements.hasNext()) {
			Map.Entry<String, JsonNode> entry = elements.next();
			Resources resource = Resources.valueOf(entry.getKey());
			float amount = entry.getValue().floatValue();
			resourcesMap.put(resource, amount);
		}

		return resourcesMap;
	}
}
