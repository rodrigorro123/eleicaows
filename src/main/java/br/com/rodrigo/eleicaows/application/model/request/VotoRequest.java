package br.com.rodrigo.eleicaows.application.model.request;

import br.com.rodrigo.eleicaows.application.model.enums.VotoEnum;
import io.micrometer.common.lang.NonNull;

public record VotoRequest(
		@NonNull String pauta,
		@NonNull String cpf, 
		@NonNull VotoEnum voto) {
}
