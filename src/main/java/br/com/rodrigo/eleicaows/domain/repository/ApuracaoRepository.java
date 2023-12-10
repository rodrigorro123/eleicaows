package br.com.rodrigo.eleicaows.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.eleicaows.domain.entity.Apuracao;
import br.com.rodrigo.eleicaows.domain.entity.Pauta;

@Repository
public interface ApuracaoRepository extends JpaRepository<Apuracao, Long> {

	 Optional<Apuracao> findByCpfAndPauta(String cpf, Pauta pauta) ;
}
