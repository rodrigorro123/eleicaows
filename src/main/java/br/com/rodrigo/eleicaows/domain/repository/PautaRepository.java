package br.com.rodrigo.eleicaows.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rodrigo.eleicaows.domain.entity.Pauta;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

	List<Pauta> findByNome(String nome);
	

}
