package br.com.rodrigo.eleicaows.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.enums.StatusEnum;
import br.com.rodrigo.eleicaows.application.model.request.PautaRequest;
import br.com.rodrigo.eleicaows.application.model.response.PautaResponse;
import br.com.rodrigo.eleicaows.application.service.PautaService;
import br.com.rodrigo.eleicaows.domain.entity.Pauta;
import br.com.rodrigo.eleicaows.domain.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaServiceImpl implements PautaService {

	private final PautaRepository pautaRepository;
	
	ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
											.registerModule(new JavaTimeModule());
	
	@Override
	public Boolean criarPauta(PautaRequest pauta) throws ApiException {
		try {
			pautaRepository.save( Pauta.builder()
										.dtCriacao(LocalDateTime.now())
										.dtEncerramento(LocalDateTime.now().plusMinutes(pauta.duracao()!= null ? pauta.duracao() : 1L ))
										.nome(pauta.nome())
										.status(StatusEnum.ABERTO)
									.build() );
			return true;
		} catch (Exception e) {
			log.error("Falha ao salvar pauta {}", e.getMessage());
			return false;
		}
	}
	
	@Override
	public List<PautaResponse> buscarPauta(String nome) throws ApiException {

		try {
			List<Pauta> pautas;
			if(Objects.nonNull(nome)) {
				pautas = pautaRepository.findByNome(nome);	
			}else {
				pautas = pautaRepository.findAll();
			}
			 return pautas.stream()
		                .map(pauta -> convertToPautaResponse(pauta))
		                .toList();
			
		} catch (RuntimeException e) {
			log.error("Falha ao buscar pauta {}", e.getMessage());
			throw  ApiException.internalError("FALHA_LOCALIZAR","Falha ao buscar pauta");
		}
	}
	private PautaResponse convertToPautaResponse(Pauta pauta) {
	    return mapper.convertValue(pauta, PautaResponse.class);
	}
	
}
