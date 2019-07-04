package br.com.lcc.jpa.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import br.com.lcc.domain.Lucas;
import br.com.lcc.jpa.entities.LucasEntity;
import br.com.lcc.jpa.repositories.LucasJpaRepository;

@Component
public class LucasService {

	@Autowired
	private LucasJpaRepository jpaRepository;

	@Autowired
	public ModelMapper modelMapper;

/*
	public Optional<Lucas> methodName(final Lucas domain) {
		return Optional.of(modelMapper.map(jpaRepository.findById(domain.getId()), Lucas.class));
	}
*/
}
