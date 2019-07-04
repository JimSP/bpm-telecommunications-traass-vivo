package br.com.interfile.vivo.traass.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Link {
	private final String name;
	private final String method;
	private final String path;
}
