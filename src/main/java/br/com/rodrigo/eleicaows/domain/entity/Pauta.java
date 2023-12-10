package br.com.rodrigo.eleicaows.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.rodrigo.eleicaows.application.model.enums.StatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pt_pauta")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Pauta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pt_id", nullable = false)
	private Long id;
	
	@Column(name = "pt_nome")
	private String nome;
	
	@Column(name = "pt_status")
	private StatusEnum status;
	
	@Column(name = "pt_dt_criacao")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	private LocalDateTime dtCriacao;
	
	@Column(name = "pt_dt_enceramento")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	private LocalDateTime dtEncerramento;
	
	@JsonManagedReference(value="pauta-apuracao")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pauta", fetch = FetchType.EAGER)
	private List<Apuracao> apuracoes;
	
}
