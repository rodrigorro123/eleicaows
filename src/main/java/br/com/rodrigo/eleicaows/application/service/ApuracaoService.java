package br.com.rodrigo.eleicaows.application.service;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.request.VotoRequest;

public interface ApuracaoService {

	Boolean registrarVoto(VotoRequest voto) throws ApiException;

}
