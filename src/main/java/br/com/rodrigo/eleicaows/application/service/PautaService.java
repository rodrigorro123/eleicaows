package br.com.rodrigo.eleicaows.application.service;

import java.util.List;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.request.PautaRequest;
import br.com.rodrigo.eleicaows.application.model.response.PautaResponse;

public interface PautaService {
	
	Boolean criarPauta(PautaRequest pauta) throws ApiException;
	
	List<PautaResponse> buscarPauta(String nome)throws ApiException;

}
