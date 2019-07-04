package br.com.interfile.vivo.traass.integration.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class ViaCepIntegrationDto {

	private final String cep;
	private final String logradouro;
	private final String complemento;
	private final String bairro;
	private final String localidade;
	private final String uf;
	private final String unidade;
	private final String ibge;
	private final String gia;

	@JsonCreator
	public ViaCepIntegrationDto( //
			@JsonProperty("cep") final String cep, //
			@JsonProperty("logradouro") final String logradouro, //
			@JsonProperty("complemento") final String complemento, //
			@JsonProperty("bairro") final String bairro, //
			@JsonProperty("localidade") final String localidade, //
			@JsonProperty("uf") final String uf, //
			@JsonProperty("unidade") final String unidade, //
			@JsonProperty("ibge") final String ibge, //
			@JsonProperty("gia") final String gia) {
		this.cep = cep;
		this.logradouro = logradouro;
		this.complemento = complemento;
		this.bairro = bairro;
		this.localidade = localidade;
		this.uf = uf;
		this.unidade = unidade;
		this.ibge = ibge;
		this.gia = gia;
	}
}
