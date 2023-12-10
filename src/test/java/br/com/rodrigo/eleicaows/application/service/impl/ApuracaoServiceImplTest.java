package br.com.rodrigo.eleicaows.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.enums.StatusEnum;
import br.com.rodrigo.eleicaows.application.model.enums.VotoEnum;
import br.com.rodrigo.eleicaows.application.model.request.VotoRequest;
import br.com.rodrigo.eleicaows.application.model.response.ResultadoApuracaoResponse;
import br.com.rodrigo.eleicaows.application.service.amqp.RabbitMQSender;
import br.com.rodrigo.eleicaows.application.service.client.ValidaCpfClient;
import br.com.rodrigo.eleicaows.domain.entity.Apuracao;
import br.com.rodrigo.eleicaows.domain.entity.Pauta;
import br.com.rodrigo.eleicaows.domain.repository.ApuracaoRepository;
import br.com.rodrigo.eleicaows.domain.repository.PautaRepository;

@SpringBootTest
class ApuracaoServiceImplTest {

	@InjectMocks
	private ApuracaoServiceImpl apuracaoService;

	@Mock
	private ApuracaoRepository apuracaoRepository;

	@Mock
	private PautaRepository pautaRepository;

	@Mock
	private ValidaCpfClient cpfClient;

	@Mock
	private RabbitMQSender mq;

	@Test
	void testRegistrarVoto_Ok() throws ApiException {

		VotoRequest votoRequest = new VotoRequest("Teste 123", "123456", VotoEnum.SIM);
		Pauta pautaAberta = new Pauta(1L, "Teste 123", StatusEnum.ABERTO, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(30), null);

		when(pautaRepository.findByNome(votoRequest.pauta())).thenReturn(List.of(pautaAberta));
		when(cpfClient.validarCpf(votoRequest.cpf())).thenReturn(true);
		when(apuracaoRepository.save(any(Apuracao.class))).thenReturn(new Apuracao());
		when(apuracaoRepository.findByCpfAndPauta(anyString(), any())).thenReturn(Optional.empty());

		var resultado = apuracaoService.registrarVoto(votoRequest);

		assertEquals("Voto realizado com sucesso", resultado);
		verify(pautaRepository, times(1)).findByNome(votoRequest.pauta());
		verify(cpfClient, times(1)).validarCpf(votoRequest.cpf());
		verify(apuracaoRepository, times(1)).save(any(Apuracao.class));
	}

	@Test
	void testRegistrarVoto_PautaInvalida() throws ApiException {

		VotoRequest votoRequest = new VotoRequest(null, "12345678901", VotoEnum.SIM);
		assertThrows(ApiException.class, () -> apuracaoService.registrarVoto(votoRequest));
	}

	@Test
	void testRegistrarVoto_VotoInvalido() throws ApiException {

		VotoRequest votoRequest = new VotoRequest("Teste 123", "12345678901", null);
		assertThrows(ApiException.class, () -> apuracaoService.registrarVoto(votoRequest));
	}

	@Test
	void testRegistrarVoto_CpfInvalido() throws ApiException {

		VotoRequest votoRequest = new VotoRequest("Teste 123", "cpf_invalido", VotoEnum.SIM);

		when(pautaRepository.findByNome(votoRequest.pauta())).thenReturn(List.of(new Pauta()));
		when(cpfClient.validarCpf(votoRequest.cpf())).thenReturn(false);

		assertThrows(ApiException.class, () -> apuracaoService.registrarVoto(votoRequest));
		verify(cpfClient, times(1)).validarCpf(votoRequest.cpf());

	}

	@Test
	void testRegistrarVoto_Fail() throws ApiException {

		VotoRequest votoRequest = new VotoRequest("Teste 123", "123456", VotoEnum.SIM);

		when(pautaRepository.findByNome(votoRequest.pauta())).thenReturn(List.of(new Pauta()));
		when(cpfClient.validarCpf(votoRequest.cpf())).thenReturn(true);
		when(apuracaoRepository.save(any(Apuracao.class))).thenThrow(new RuntimeException("falha ao salvar"));

		assertThrows(ApiException.class, () -> apuracaoService.registrarVoto(votoRequest));
		verify(pautaRepository, times(1)).findByNome(votoRequest.pauta());
		verify(cpfClient, times(1)).validarCpf(votoRequest.cpf());

	}

	@Test
	void testConsultarResultadoApuracao_PautaInvalida() throws ApiException {

		String nomePauta = null;
		assertThrows(ApiException.class, () -> apuracaoService.consultarResultadoApuracao(nomePauta));
	}

	@Test
	void testEnviarMsgPautaFechada_Ok() throws ApiException {
		var nomePauta = "Teste 123";
		Pauta pautaAberta = new Pauta(1L, nomePauta, StatusEnum.ABERTO, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(30), null);
		
		var apuracao = Apuracao.builder()
				.cpf("123456")
				.voto(VotoEnum.SIM)
				.dtCriacao(LocalDateTime.now())
				.pauta(pautaAberta)
				.build();
		pautaAberta.setApuracoes(Collections.singletonList(apuracao));
		
		when(pautaRepository.findByStatus(StatusEnum.ABERTO)).thenReturn(Arrays.asList(pautaAberta));
		doNothing().when(mq).sendMessage(any(ResultadoApuracaoResponse.class));

		apuracaoService.enviarMsgPautaFechada();
		verify(pautaRepository, times(1)).findByStatus(StatusEnum.ABERTO);
	}

	@Test
	void testEnviarMsgPautaFechada_NenhumaPautaAberta() throws ApiException {
	    
	    when(pautaRepository.findByStatus(StatusEnum.ABERTO)).thenReturn(Collections.emptyList());
	    
	    apuracaoService.enviarMsgPautaFechada();
	    verify(pautaRepository, times(1)).findByStatus(StatusEnum.ABERTO);
	    verify(mq, never()).sendMessage(any(ResultadoApuracaoResponse.class));
	}

	@Test
	void testConsultarResultadoApuracao_Ok() throws ApiException {

		var apuracao = Apuracao.builder().cpf("123456").voto(VotoEnum.SIM).dtCriacao(LocalDateTime.now()).build();
		var nomePauta = "Teste 123";
		Pauta pautaAberta = new Pauta(1L, nomePauta, StatusEnum.ABERTO, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(30), Collections.singletonList(apuracao));

		when(pautaRepository.findByNome(nomePauta)).thenReturn(List.of(pautaAberta));

		ResultadoApuracaoResponse resultado = apuracaoService.consultarResultadoApuracao(nomePauta);

		assertNotNull(resultado);
		verify(pautaRepository, times(1)).findByNome(nomePauta);
	}

	@Test
	void testRegistrarVoto_PautaNaoLocalizada() throws ApiException {

		VotoRequest votoRequest = new VotoRequest("Teste 123", "12965", VotoEnum.SIM);

		when(pautaRepository.findByNome(anyString())).thenReturn(null);
		when(cpfClient.validarCpf(votoRequest.cpf())).thenReturn(true);

		ApiException apiException = assertThrows(ApiException.class, () -> apuracaoService.registrarVoto(votoRequest));
		assertEquals(Integer.valueOf(HttpStatus.PRECONDITION_FAILED.value()), apiException.getStatusCode());
	}
	
	@Test
	void testRegistrarVoto_PautaEncerrada() throws ApiException {

		VotoRequest votoRequest = new VotoRequest("Teste 123", "123456", VotoEnum.SIM);
		Pauta pautaAberta = new Pauta(1L, "Teste 123", StatusEnum.ABERTO, LocalDateTime.now(),
				LocalDateTime.now().minusMinutes(30), null);

		when(pautaRepository.findByNome(votoRequest.pauta())).thenReturn(List.of(pautaAberta));
		when(cpfClient.validarCpf(votoRequest.cpf())).thenReturn(true);

		ApiException apiException = assertThrows(ApiException.class, () -> apuracaoService.registrarVoto(votoRequest));
		assertEquals(Integer.valueOf(HttpStatus.PRECONDITION_FAILED.value()), apiException.getStatusCode());
	}
	
	@Test
	void testRegistrarVoto_VotoDuplicado() throws ApiException {

		Apuracao apuracao = Apuracao.builder().cpf("123456").build();
		VotoRequest votoRequest = new VotoRequest("Teste 123", "123456", VotoEnum.SIM);
		Pauta pautaAberta = new Pauta(1L, "Teste 123", StatusEnum.ABERTO, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(30), null);

		when(pautaRepository.findByNome(votoRequest.pauta())).thenReturn(List.of(pautaAberta));
		when(cpfClient.validarCpf(votoRequest.cpf())).thenReturn(true);
		when(apuracaoRepository.findByCpfAndPauta(anyString(), any())).thenReturn(Optional.of(apuracao));

		ApiException apiException = assertThrows(ApiException.class, () -> apuracaoService.registrarVoto(votoRequest));
		assertEquals(Integer.valueOf(HttpStatus.PRECONDITION_FAILED.value()), apiException.getStatusCode());
	}

}
