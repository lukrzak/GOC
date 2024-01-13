package com.lukrzak.goc.gamebackend.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class GameUtils {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	public static JsonNode getConfigNode(String configFileName, String... nodeKeyPath) throws IOException {
		JsonNode node = objectMapper.readTree(new File(configFileName));

		for (String key: nodeKeyPath) {
			node = node.get(key);
		}

		return node;
	}

}
