package br.com.interfile.vivo.traass.rules;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.SolicitationStatus;

@Component
public class PossibleStatusOfSolicitation {

	public List<SolicitationStatus> possibles(final SolicitationStatus solicitationStatus) {
		return solicitationStatus.possibles();
	}
}
