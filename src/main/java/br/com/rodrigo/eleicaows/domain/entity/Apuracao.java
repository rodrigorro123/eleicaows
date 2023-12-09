package br.com.rodrigo.eleicaows.domain.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.rodrigo.eleicaows.application.model.enums.VotoEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ap_apuracao")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Apuracao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ap_id", nullable = false)
	private Long id;
	
	@Column(name = "ap_cpf")
	private String cpf;
	
	@Column(name = "ap_voto")
	private VotoEnum voto;
	
	@Column(name = "ap_dt_criacao")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	private LocalDateTime dtCriacao;
	
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference(value="pauta-apuracao")
    @JoinColumn(name = "ap_pt_id", referencedColumnName = "pt_id")
    private Pauta pauta;
	
}
