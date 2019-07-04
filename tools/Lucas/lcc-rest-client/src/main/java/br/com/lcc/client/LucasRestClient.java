package br.com.lcc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.lcc.dto.LucasRequestDto;
import br.com.lcc.dto.LucasResponseDto;

@Component
public class LucasRestClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(LucasRestClient.class);

	public void post() {
		final RestTemplate restTemplate = new RestTemplate();

		final ResponseEntity<LucasResponseDto> responseEntity = restTemplate //
				.postForEntity("http://localhost:8080/lcc", //
						LucasResponseDto //
								.builder() //
								.build(),
						LucasResponseDto.class);

		LOGGER.info(MarkerFactory.getMarker("CLIENT"), "m=doLogin, responseEntity={}", responseEntity);
	}
	
	public void get() {
		final RestTemplate restTemplate = new RestTemplate();

		final Map<String, String> uriVariable = Collections.emptyMap();
		
		final ResponseEntity<LucasResponseDto> responseEntity = restTemplate //
				.getForEntity("http://localhost:8080/user", //
						LucasResponseDto.class, uriVariable);

		LOGGER.info(MarkerFactory.getMarker("CLIENT"), "m=doLogin, responseEntity={}", responseEntity);
	}
}
