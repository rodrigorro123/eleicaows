package br.com.rodrigo.eleicaows.application.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.request.VotoRequest;
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
	public ResponseEntity<Boolean> salvar(@RequestBody VotoRequest voto) throws ApiException {
		var result = service.registrarVoto(voto);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}
}
