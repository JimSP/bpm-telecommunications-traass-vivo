package br.com.interfile.vivo.traass.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.interfile.vivo.traass.converter.SolicitationLogToSolicitationResponseDtoConverter;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.dto.ListSolicitationResponseDto;
import br.com.interfile.vivo.traass.dto.SolicitationResponseDto;
import br.com.interfile.vivo.traass.facade.SolicitationFacade;

@RestController
public class ReportRestController {

	private static Optional<Future<ListSolicitationResponseDto.ListSolicitationResponseDtoBuilder>> $( //
			final Predicate<String> predicate, //
			final String predicateValue, //
			final Function<Pair<Date, Date>, List<Solicitation>> facadeMethod, //
			final Converter<Solicitation, SolicitationResponseDto> converter, final Pair<Date, Date> pairDate) {

		final ForkJoinPool forkJoinPool = new ForkJoinPool(8);

		return Optional //
				.ofNullable( //
						forkJoinPool //
								.submit(() -> //
						ListSolicitationResponseDto //
								.builder()//
								.solicitationResponseDtos(//
										facadeMethod //
												.apply(pairDate) //
												.parallelStream() //
												.map(mapper -> converter //
														.convert(mapper)) //
												.collect( //
														Collectors.toList()))));
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportRestController.class);

	@Autowired
	private SolicitationFacade facade;

	@Autowired
	private SolicitationLogToSolicitationResponseDtoConverter solicitationLogToSolicitationResponseDto;

	@GetMapping(value = "/vt-report-ms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String, ListSolicitationResponseDto> get(
			@RequestParam(name = "aberto", required = false, defaultValue = "true") final String aberto,
			@RequestParam(name = "aprovado", required = false, defaultValue = "true") final String aprovado,
			@RequestParam(name = "reprovado", required = false, defaultValue = "true") final String reprovado,
			@RequestParam(name = "pendencia", required = false, defaultValue = "true") final String pendencia,
			@RequestParam(name = "dtinicio", required = true) final String dtInicio,
			@RequestParam(name = "dtFim", required = true) final String dtFim) throws ParseException {
		LOGGER.debug(MarkerFactory.getMarker("REST"),
				"m=get, aprovado={}, reprovado={}, pendencia={}, dtinicio={}, dtFim={}", aprovado,
				reprovado, dtInicio, dtFim);

		final Date inicio = new SimpleDateFormat("yyyyMMdd").parse(dtInicio);
		final Date fim = new SimpleDateFormat("yyyyMMdd").parse(dtFim);

		final Map<String, ListSolicitationResponseDto> result = Collections.synchronizedMap(new HashMap<>());

		getAberto(aberto, inicio, fim, result);
		getAprovados(aprovado, inicio, fim, result);
		getReprovados(reprovado, inicio, fim, result);
		getPendentes(pendencia, inicio, fim, result);

		return result;
	}
	
	private void getAberto(final String aberto, final Date dtInicio, final Date dtFim,
			final Map<String, ListSolicitationResponseDto> result) {
		$(predicate -> Boolean.parseBoolean(predicate), aberto,
				(datePair) -> facade.findAberto(datePair),
				solicitationLogToSolicitationResponseDto, Pair.of(dtInicio, dtFim))
		.ifPresent(consumer->{
			try {
				result.put("abertos", consumer.get().build());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
	}


	private void getAprovados(final String aprovado, final Date dtInicio, final Date dtFim,
			final Map<String, ListSolicitationResponseDto> result) {
		$(predicate -> Boolean.parseBoolean(predicate), aprovado,
				(datePair) -> facade.findAprovados(datePair),
				solicitationLogToSolicitationResponseDto, Pair.of(dtInicio, dtFim))
		.ifPresent(consumer->{
			try {
				result.put("aprovados", consumer.get().build());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private void getReprovados(final String aprovado, final Date dtInicio, final Date dtFim,
			final Map<String, ListSolicitationResponseDto> result) {
		$(predicate -> Boolean.parseBoolean(predicate), aprovado,
				(datePair) -> facade.findReprovados(datePair),
				solicitationLogToSolicitationResponseDto, Pair.of(dtInicio, dtFim))
		.ifPresent(consumer->{
			try {
				result.put("reprovados", consumer.get().build());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private void getPendentes(final String aprovado, final Date dtInicio, final Date dtFim,
			final Map<String, ListSolicitationResponseDto> result) {
		
		$(predicate -> Boolean.parseBoolean(predicate), aprovado,
				(datePair) -> facade.findPendentesVivo(datePair),
				solicitationLogToSolicitationResponseDto, Pair.of(dtInicio, dtFim))
		.ifPresent(consumer->{
			try {
				result.put("pendentes", consumer.get().build());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
