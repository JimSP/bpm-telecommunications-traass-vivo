package br.com.interfile.vivo.traass.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import br.com.interfile.vivo.traass.dto.ListSolicitationResponseDto;
import br.com.interfile.vivo.traass.dto.SolicitationRequestDto;
import br.com.interfile.vivo.traass.dto.SolicitationResponseDto;

public final class SolicitationRestClient {

	public static final String DEFAULT_URL = "http://localhost:8092";
	private static final String RESOURCE = "/vt-solicitation-ms"; 
	
	public static SolicitationRestClient create(final String url) {
		return new SolicitationRestClient(url);
	}

	private final String url;
	private final RestTemplate restTemplate = new RestTemplate();

	private SolicitationRestClient(final String url) {
		this.url = url;
	}

	public SolicitationResponseDto get(final Long id) {
		return restTemplate.getForObject(url + "{id}", SolicitationResponseDto.class,
				Collections.singletonMap("id", String.valueOf(id)));
	}

	public List<SolicitationResponseDto> get(final List<Long> ids) {
		return restTemplate //
				.getForObject(url + RESOURCE + "list/{ids}", ListSolicitationResponseDto.class, //
						Collections.singletonMap("ids", ids //
								.stream() //
								.map(mapper -> String.valueOf(mapper)) //
								.reduce((a, b) -> a //
										.concat(",") //
										.concat(b)) //
								.orElseThrow(() -> new RuntimeException("ids is not empty.")))) //
				.getSolicitationResponseDtos();

	}

	public List<SolicitationResponseDto> getByUserId(final Long userId) {
		return restTemplate.getForObject(url + "/{userId}" + RESOURCE, ListSolicitationResponseDto.class,
				Collections.singletonMap("userId", String.valueOf(userId))).getSolicitationResponseDtos();
	}

	public SolicitationResponseDto post(final SolicitationRequestDto request) {
		return restTemplate.postForObject(url + RESOURCE, request, SolicitationResponseDto.class,
				Collections.emptyList());
	}

	public List<SolicitationResponseDto> findLogById(final Long id) {
		return restTemplate.getForObject(url + RESOURCE + "{id}/log", ListSolicitationResponseDto.class,
				Collections.singletonMap("id", id)).getSolicitationResponseDtos();
	}

	public List<String> status(final String solicitationStatus) {
		return Arrays.asList(restTemplate.getForObject(url + RESOURCE + "status/{solicitationStatus}",
				String[].class, Collections.singletonMap("solicitationStatus", solicitationStatus)));
	}

	public List<String> status(final Long id) {
		return Arrays.asList(restTemplate.getForObject(url + RESOURCE + "{id}/status", String[].class,
				Collections.singletonMap("id", id)));
	}

	public void toOpen(final Long id) {
		restTemplate.put(url + RESOURCE + "{id}/status/open", //
				null, //
				Collections.singletonMap("id", id));
	}
	
	public void toPending(final Long id) {
		restTemplate.put(url + RESOURCE + "{id}/status/pending", //
				null, //
				Collections.singletonMap("id", id));
	}
	
	public void toInAnalysis(final Long id) {
		restTemplate.put(url + RESOURCE + "{id}/status/inAnalysis", //
				null, //
				Collections.singletonMap("id", id));
	}
	
	public void toInConfiguration(final Long id) {
		restTemplate.put(url + RESOURCE + "{id}/status/inConfiguration", //
				null, //
				Collections.singletonMap("id", id));
	}
	
	public void toWithProblem(final Long id) {
		restTemplate.put(url + RESOURCE + "{id}/status/withProblem", //
				null, //
				Collections.singletonMap("id", id));
	}
	
	public void toClose(final Long id) {
		restTemplate.put(url + RESOURCE + "{id}/status/close", //
				null, //
				Collections.singletonMap("id", id));
	}
}
