package br.com.rodrigo.eleicaows.application.model.response;

import java.util.Map;

import br.com.rodrigo.eleicaows.application.model.AgrupamentoApuracao;
import lombok.Data;

@Data
public class ResultadoApuracaoResponse {

	private Map<AgrupamentoApuracao, Long> resultados;

	public ResultadoApuracaoResponse(Map<AgrupamentoApuracao, Long> resultados) {
		this.resultados = resultados;
	}

	public Map<AgrupamentoApuracao, Long> getResultados() {
		return resultados;
	}

}
