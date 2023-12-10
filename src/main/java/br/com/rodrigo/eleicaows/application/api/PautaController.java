package br.com.rodrigo.eleicaows.application.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.request.PautaRequest;
import br.com.rodrigo.eleicaows.application.model.response.PautaResponse;
import br.com.rodrigo.eleicaows.application.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pauta")
public class PautaController {

	private final PautaService service;
	 
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "criar uma pauta")
	public ResponseEntity<String> salvar( @RequestBody PautaRequest pauta) throws ApiException {
		var result = service.criarPauta(pauta);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}
	 
	@GetMapping( produces = "application/json")
	@Operation(summary = "buscar pauta")
	public ResponseEntity<List<PautaResponse>> buscarPauta(
			@RequestParam(required = false) String pauta) throws ApiException{
		
		var result = service.buscarPauta(pauta);
		return ResponseEntity.ok(result);
	}
}
