package br.com.rodrigo.eleicaows.application.service;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.request.VotoRequest;
import br.com.rodrigo.eleicaows.application.model.response.ResultadoApuracaoResponse;

public interface ApuracaoService {

	String registrarVoto(VotoRequest voto) throws ApiException;
	
	ResultadoApuracaoResponse consultarResultadoApuracao(String nomePauta)throws ApiException;
	
	void enviarMsgPautaFechada() throws ApiException;

}
