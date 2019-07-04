package br.com.lcc.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.lcc.domain.Lucas;
import br.com.lcc.dto.LucasRequestDto;
import br.com.lcc.dto.LucasResponseDto;
import br.com.lcc.facade.LucasFacade;

@RestController
public class LucasRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LucasRestController.class);

	@Autowired
	private LucasFacade facade;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/lcc")
	public @ResponseBody LucasResponseDto post(@RequestBody @Valid final LucasRequestDto request) {
		LOGGER.debug(MarkerFactory.getMarker("REST"), "m=post, request={}", request);


		return modelMapper.map(Lucas //
			.builder() //
			.build(),  //
			LucasResponseDto.class);
	}
}
