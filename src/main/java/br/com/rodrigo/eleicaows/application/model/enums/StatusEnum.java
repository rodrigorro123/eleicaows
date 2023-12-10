package br.com.rodrigo.eleicaows.application.model.enums;

public enum StatusEnum {

	ABERTO("ABERTO"), FECHADO("FECHADO");

	private String status;

	StatusEnum(String statusResp) {
		this.status = statusResp;
	}

	public String getStatus() {
		return status;
	}
}
