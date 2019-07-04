package br.com.interfile.vivo.traass.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder=true)
@Data
public class Transferee {

	private final Long id;
	private final String transfereeName;
	private final Document document;
	private final String transfereeEmail;
	private final Phone transfereePhone;
	private final List<Address> transfereeAddresses;
}
