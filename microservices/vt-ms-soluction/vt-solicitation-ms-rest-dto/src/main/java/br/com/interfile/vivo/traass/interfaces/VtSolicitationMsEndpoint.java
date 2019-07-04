package br.com.interfile.vivo.traass.interfaces;

public interface VtSolicitationMsEndpoint {
	
	String DEV_ADDRESS = "http://localhost:8080/";
	String VT_SOLICITATION_MS = "vt-solicitation-ms";
	
	static String getEndpoint(final String address) {
		return address.concat(VT_SOLICITATION_MS);
	}
	
	static String getEndpoint() {
		return DEV_ADDRESS.concat(VT_SOLICITATION_MS);
	}
}
