package br.com.rodrigo.eleicaows.application.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.AgrupamentoApuracao;
import br.com.rodrigo.eleicaows.application.model.enums.StatusEnum;
import br.com.rodrigo.eleicaows.application.model.request.VotoRequest;
import br.com.rodrigo.eleicaows.application.model.response.ResultadoApuracaoResponse;
import br.com.rodrigo.eleicaows.application.service.ApuracaoService;
import br.com.rodrigo.eleicaows.application.service.amqp.RabbitMQSender;
import br.com.rodrigo.eleicaows.application.service.client.ValidaCpfClient;
import br.com.rodrigo.eleicaows.domain.entity.Apuracao;
import br.com.rodrigo.eleicaows.domain.entity.Pauta;
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
	private final RabbitMQSender mq;

	private static final String PAUTA_INVALIDA = "PAUTA_INVALIDA";
	
	ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.registerModule(new JavaTimeModule());

	/**
	 * registra o voto no banco apos validado
	 */
	@Override
	public String registrarVoto(VotoRequest voto) throws ApiException {
		try {

			var pauta = preRequisitoVotacao(voto);
			apuracaoRepository.save(Apuracao.builder()
					.cpf(voto.cpf())
					.pauta(pauta)
					.dtCriacao(LocalDateTime.now())
					.voto(voto.voto())
					.build());
			
		} catch (RuntimeException e) {
			log.error("Falha ao registrar voto: {}", e.getMessage());
			throw ApiException.internalError("ERRO_INTERNO", "Erro ao registrar voto");
		}
		return "Voto realizado com sucesso";
	}
	
	/**
	 * Analisa os pre-requisitos da votacao
	 * @param voto
	 * @return
	 * @throws ApiException
	 */
	private Pauta preRequisitoVotacao(VotoRequest voto) throws ApiException {
		if (Objects.isNull(voto.pauta())) {
			throw ApiException.preconditionFailed(PAUTA_INVALIDA, "Pauta nao informada");
		}
		if (Objects.isNull(voto.voto())) {
			throw ApiException.preconditionFailed("VOTO_INVALIDO", "Tipo de voto nao informado");
		}
		if (Objects.isNull(voto.cpf()) || Boolean.FALSE.equals(cpfClient.validarCpf(voto.cpf()))) {
			throw ApiException.preconditionFailed("CPF_INVALIDO", "Favor informar um CPF correto");
		}
		
		return persitirVoto(voto);
	}
	
	/**
	 * persite o voto no banco analisando situação da pauta e validacao do primeiro voto  
	 * @param voto
	 * @return
	 * @throws ApiException
	 */
	private Pauta persitirVoto(VotoRequest voto) throws ApiException {
		var pauta = persistirPauta(voto.pauta()).stream().findFirst() ;

		if(LocalDateTime.now().isAfter(
									pauta.get().getDtEncerramento()) ) {
			throw ApiException.preconditionFailed(PAUTA_INVALIDA, "Pauta Encerrada");
		}
		var apuracao = apuracaoRepository.findByCpfAndPauta(voto.cpf(), pauta.get());
		if (!apuracao.isEmpty()) {
			throw ApiException.preconditionFailed("VOTO_EFETUADO", "Voto ja efetuado");
		}
		return pauta.get();
	}

	/**
	 * persistir pauta
	 * @param nomePauta
	 * @return
	 * @throws ApiException
	 */
	private List<Pauta> persistirPauta(String nomePauta) throws ApiException {
		if (Objects.isNull(nomePauta)) {
			throw ApiException.preconditionFailed(PAUTA_INVALIDA, "Pauta nao informada");
		}
		List<Pauta> pautas = pautaRepository.findByNome(nomePauta);
		if (Objects.isNull(pautas)) {
			throw ApiException.preconditionFailed(PAUTA_INVALIDA, "Pauta nao Localizada");
		}
		return pautas;
	}
	
	/**
	 * realizar o calculo da qtde de votos para cada pauta
	 * @param pautas
	 * @return
	 * @throws ApiException
	 */
	private ResultadoApuracaoResponse calcularResultadoVotacao(List<Pauta> pautas) {
		 ResultadoApuracaoResponse resultadoFinal = new ResultadoApuracaoResponse(
	                pautas.stream()
	                        .flatMap(pauta -> pauta.getApuracoes().stream()
	                                .map(apuracao -> new AgrupamentoApuracao(
	                                        apuracao.getVoto(),
	                                        pauta.getNome(),
	                                        pauta.getStatus(),
	                                        pauta.getDtCriacao(),
	                                        pauta.getDtEncerramento()
	                                ))
	                        )
	                        .collect(Collectors.groupingBy(
	                                Function.identity(), 
	                                Collectors.counting()
	                        ))
	        		);
		 return resultadoFinal;
	}
	
	/**
	 * Gera a estatistica de votos por pauta selecionada
	 */
	@Override
	public ResultadoApuracaoResponse consultarResultadoApuracao(String nomePauta) throws ApiException {

		List<Pauta> pautas = persistirPauta(nomePauta);

		return calcularResultadoVotacao(pautas);
	}

	/**
	 * verifica as pautas abertas caso tenham sido expiradas, envia para a fila
	 */
	public void enviarMsgPautaFechada() throws ApiException {

		pautaRepository.findByStatus(StatusEnum.ABERTO)
				.stream()
				.filter( pauta -> LocalDateTime.now().isAfter(pauta.getDtEncerramento()))
				.forEach( pauta -> {
					pauta.setStatus(StatusEnum.FECHADO);
					pautaRepository.save(pauta);
					if(!pauta.getApuracoes().isEmpty()) {// notifica apenas pautas com votos
						mq.sendMessage(calcularResultadoVotacao(Arrays.asList(pauta)));	
					}
				});
	}
}
