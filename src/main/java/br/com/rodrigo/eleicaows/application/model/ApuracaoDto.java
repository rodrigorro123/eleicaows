package br.com.rodrigo.eleicaows.application.model;

import java.time.LocalDateTime;

import br.com.rodrigo.eleicaows.application.model.enums.VotoEnum;
import lombok.Data;

@Data
public class ApuracaoDto {

	private Long id;
	
	private String cpf;
	
	private VotoEnum voto;
	
	private LocalDateTime dtCriacao;
	
}
