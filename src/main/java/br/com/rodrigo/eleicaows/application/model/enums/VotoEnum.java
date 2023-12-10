package br.com.rodrigo.eleicaows.application.model.enums;

public enum VotoEnum {

	SIM("SIM"), NAO("NAO");

	private String voto;

	VotoEnum(String votoResp) {
		this.voto = votoResp;
	}

	public String getVoto() {
		return voto;
	}
}
