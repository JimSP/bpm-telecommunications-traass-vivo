package br.com.interfile.vivo.traass.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.interfile.vivo.traass.domain.Solicitation;
import br.com.interfile.vivo.traass.dto.StepSolicitationResponseDto;

@Component
public class SolicitationToStepSolicitationResponseDto implements Converter<Solicitation, StepSolicitationResponseDto> {

	@Override
	public StepSolicitationResponseDto convert(final Solicitation solicitation) {
		return StepSolicitationResponseDto //
				.builder() //
				.comment(solicitation.getComment()) //
				.status(solicitation.getSolicitationStatus().name()) //
				.stepDate(solicitation.getCreationDate()) //
				.build();
	}
}
