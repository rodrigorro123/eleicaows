package br.com.rodrigo.eleicaows.application.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.service.ApuracaoService;
import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class Schedule {

	private final ApuracaoService apuracaoService;

	@Scheduled(cron = "0 * * * * *")
	public void teste() throws ApiException {
		
		apuracaoService.enviarMsgPautaFechada();
	}
}
