package br.com.interfile.vivo.traass.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Donnor {

	private final Long id;
	private final String donnorName;
	private final Document document;
	private final String donnorRg;
	private final String donnorEmail;
	private final Phone donnorPhone;
	private final List<Address> donnorAddresses;
}
