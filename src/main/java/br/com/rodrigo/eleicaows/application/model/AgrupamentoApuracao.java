package br.com.rodrigo.eleicaows.application.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.rodrigo.eleicaows.application.model.enums.StatusEnum;
import br.com.rodrigo.eleicaows.application.model.enums.VotoEnum;
import lombok.Data;

@Data
public class AgrupamentoApuracao {
    private VotoEnum voto;
    private String nomePauta;
    private StatusEnum status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDateTime dtCriacao;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDateTime dtEncerramento;


    public AgrupamentoApuracao(VotoEnum voto, String nomePauta, StatusEnum status, LocalDateTime dtCriacao, LocalDateTime dtEncerramento) {
        this.voto = voto;
        this.nomePauta = nomePauta;
        this.status = status;
        this.dtCriacao = dtCriacao;
        this.dtEncerramento = dtEncerramento;
    }
}
