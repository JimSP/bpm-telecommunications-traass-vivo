package br.com.interfile.vivo.traass.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Builder(toBuilder=true)
@Data
@ToString(exclude = { "password" })
public class User {
	
	private final Long id;
	private final String name;
	private final List<Document> documents;
	private final String email;
	private final List<Phone> phones;
	private final List<Address> addresses;
	private final String password;
	private final Boolean verified;
	
}
