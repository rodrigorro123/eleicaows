package br.com.rodrigo.eleicaows.application.model.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.rodrigo.eleicaows.application.model.ApuracaoDto;
import br.com.rodrigo.eleicaows.application.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PautaResponse {
	
	private Long id;

	private String nome;

	private StatusEnum status;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	private LocalDateTime dtCriacao;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	private LocalDateTime dtEncerramento;
	
	private List<ApuracaoDto> apuracoes;
}
