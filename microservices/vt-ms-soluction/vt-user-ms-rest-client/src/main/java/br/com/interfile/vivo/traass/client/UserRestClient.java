package br.com.interfile.vivo.traass.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.interfile.vivo.traass.dto.ListUserResponseDto;
import br.com.interfile.vivo.traass.dto.SendEmailRequestDto;
import br.com.interfile.vivo.traass.dto.UserRequestDto;
import br.com.interfile.vivo.traass.dto.UserResponseDto;
import br.com.interfile.vivo.traass.exception.ClientSideHttpException;
import br.com.interfile.vivo.traass.exception.HttpConnectionException;

@Component
public class UserRestClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestClient.class);

	@Value("${interfile.vivo.traass.vt-ms-user.url}")
	private String vtUserMsUrl;

	@Autowired
	private RestTemplate restTemplate;

	public List<UserResponseDto> get(final String documentValue, final String email) {
		try {

			final Map<String, String> uriVariables = new HashMap<>();
			uriVariables.put("documentValue", documentValue);
			uriVariables.put("email", email);

			final ResponseEntity<ListUserResponseDto> responseEntity = restTemplate.getForEntity(vtUserMsUrl, //
					ListUserResponseDto.class, uriVariables);

			LOGGER.info(MarkerFactory.getMarker("CLIENT"), "m=doLogin, responseEntity={}", responseEntity);

			return responseEntity.getBody().getUserResponseDtos();
		} catch (ResourceAccessException e) {
			throw new HttpConnectionException(e);
		} catch (RestClientException e) {
			throw new ClientSideHttpException(e);
		}
	}

	public UserResponseDto post(final UserResponseDto userResponseDto) {
		try {
			final ResponseEntity<UserResponseDto> responseEntity = restTemplate //
					.postForEntity(vtUserMsUrl, //
							userResponseDto, UserResponseDto.class);

			LOGGER.info(MarkerFactory.getMarker("CLIENT"), "m=post, responseEntity={}", responseEntity);

			return responseEntity.getBody();
		} catch (ResourceAccessException e) {
			throw new HttpConnectionException(e);
		} catch (RestClientException e) {
			throw new ClientSideHttpException(e);
		}
	}

	public void put(final Long id, final UserRequestDto createUserRequestDto) {
		try {
			final Map<String, String> uriVariables = new HashMap<>();
			uriVariables.put("id", String.valueOf(id));

			restTemplate.put(vtUserMsUrl, createUserRequestDto, uriVariables);

			LOGGER.info(MarkerFactory.getMarker("CLIENT"), "m=put, id=id");
		} catch (ResourceAccessException e) {
			throw new HttpConnectionException(e);
		} catch (RestClientException e) {
			throw new ClientSideHttpException(e);
		}
	}

	public void delete(final Long id) {
		try {
			final Map<String, String> uriVariables = new HashMap<>();
			uriVariables.put("id", String.valueOf(id));

			restTemplate.put(vtUserMsUrl, uriVariables);

			LOGGER.info(MarkerFactory.getMarker("CLIENT"), "m=delete, id=id");
		} catch (ResourceAccessException e) {
			throw new HttpConnectionException(e);
		} catch (RestClientException e) {
			throw new ClientSideHttpException(e);
		}
	}

	public void sendNotification(final Long id) {
		final SendEmailRequestDto sendEmailRequestDto = SendEmailRequestDto //
				.builder() //
				.message("Sua solicitação teve alterações, acompanhe através do Portal Vivo.") //
				.subject("Acompanhe sua solicitações VIVO.") //
				.build();

		restTemplate.postForLocation(vtUserMsUrl + "/sendEmail/{id}", sendEmailRequestDto,
				Collections.singletonMap("id", id));

		LOGGER.info(MarkerFactory.getMarker("CLIENT"), "m=sendNotification, id={}", id);
	}
}
