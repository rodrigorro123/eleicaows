package br.com.rodrigo.eleicaows.application.model.request;

import br.com.rodrigo.eleicaows.application.model.enums.VotoEnum;

public record VotoRequest(
		String pauta, 
		String cpf, 
		VotoEnum voto) {
}
