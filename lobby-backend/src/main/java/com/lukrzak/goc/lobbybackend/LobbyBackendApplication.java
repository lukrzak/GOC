package com.lukrzak.goc.lobbybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LobbyBackendApplication {

	public static final String BASE_URL = "/api/v1";
	public static void main(String[] args) {
		SpringApplication.run(LobbyBackendApplication.class, args);
	}

}
