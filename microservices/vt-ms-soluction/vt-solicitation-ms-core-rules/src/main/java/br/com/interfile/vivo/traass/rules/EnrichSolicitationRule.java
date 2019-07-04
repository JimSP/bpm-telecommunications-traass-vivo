package br.com.interfile.vivo.traass.rules;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.DigitalDocument;
import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.domain.SolicitationStatus;

@Component
public class EnrichSolicitationRule {

	public Solicitation enrichAlter(final Solicitation solicitation, final SolicitationStatus newStatus) {

		return solicitation //
				.toBuilder() //
				.solicitationStatus(newStatus) //
				.build();
	}

	public Solicitation enrichToOpen(final Solicitation solicitation, final List<DigitalDocument> digitalDocuments, final String protocolNumber) {

		return solicitation //
				.toBuilder() //
				.solicitationStatus(SolicitationStatus.Open) //
				.digitalDocuments(digitalDocuments) //
				.protocolNumber(protocolNumber) //
				.build();
	}
}
