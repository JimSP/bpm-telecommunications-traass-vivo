package br.com.interfile.vivo.traass.interfaces;

public enum Env {

	DEV("http://localhost:8080"), HOMOLOG("http://bpm-hml.interfile.com.br/vt_user"), PROD("");

	private final String url;

	private Env(final String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
