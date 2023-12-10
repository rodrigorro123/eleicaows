package br.com.rodrigo.eleicaows.application.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.request.VotoRequest;
import br.com.rodrigo.eleicaows.application.model.response.ResultadoApuracaoResponse;
import br.com.rodrigo.eleicaows.application.service.ApuracaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voto")
public class ApuracaoController {

	private final ApuracaoService service;
	 
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "realizar votação")
	@Validated
	public ResponseEntity<String> salvar(@RequestBody VotoRequest voto) throws ApiException {
		var result = service.registrarVoto(voto);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}
	
	@GetMapping(value = "/resultado" ,produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "resultado da votação")
	@Validated
	public ResponseEntity<ResultadoApuracaoResponse> consultarResultado(
			@RequestParam(name = "pauta", required = true) String pauta ) throws ApiException {
		var result = service.consultarResultadoApuracao(pauta);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
