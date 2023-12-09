package br.com.rodrigo.eleicaows.application.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.request.VotoRequest;
import br.com.rodrigo.eleicaows.application.service.ApuracaoService;
import br.com.rodrigo.eleicaows.application.service.client.ValidaCpfClient;
import br.com.rodrigo.eleicaows.domain.entity.Apuracao;
import br.com.rodrigo.eleicaows.domain.repository.ApuracaoRepository;
import br.com.rodrigo.eleicaows.domain.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApuracaoServiceImpl implements ApuracaoService {

	private final ApuracaoRepository apuracaoRepository;
	private final PautaRepository pautaRepository;
	private final ValidaCpfClient cpfClient;

	ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.registerModule(new JavaTimeModule());

	@Override
	public Boolean registrarVoto(VotoRequest voto) throws ApiException {
		validarVoto(voto);
		try {
			apuracaoRepository.save(Apuracao.builder()
					.cpf(voto.cpf())
					.dtCriacao(LocalDateTime.now())
					.voto(voto.voto())
					.build());
			
		} catch (Exception e) {
			log.error("Falha ao registrar voto: {}", e.getMessage());
			throw ApiException.internalError("ERRO_INTERNO", "Erro ao registrar voto");
		}
		return true;
	}
	private Boolean validarVoto(VotoRequest voto) throws ApiException {
		if (Boolean.FALSE.equals(cpfClient.validarCpf(voto.cpf()))) {
			throw ApiException.preconditionFailed("CPF_INVALIDO", "Cpf informado Invalido");
		}
		
		var pauta = pautaRepository.findByNome(voto.pauta());
		var xx = pauta.stream().findFirst() ;
		if(xx.isEmpty()|| LocalDateTime.now().isAfter(
									xx.get().getDtEncerramento()) ) {
			throw ApiException.preconditionFailed("PAUTA_INVALIDA", "Pauta Encerrada");
		}
		var apuracao = apuracaoRepository.findByCpfAndPauta(voto.cpf(), xx.get());
		if (apuracao.isPresent()) {
			throw ApiException.preconditionFailed("VOTO_EFETUADO", "Voto ja efetuado");
		}
		
		return true;
	}

}
