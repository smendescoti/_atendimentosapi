package br.com.cotiinformatica.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.cotiinformatica.entities.Atendimento;

public interface IAtendimentoRepository extends CrudRepository<Atendimento, Integer> {

	@Query("select a from Atendimento a join a.cliente c join a.servicos s order by a.data")
	List<Atendimento> findAll();

	@Query("select a from Atendimento a join a.cliente c join a.servicos s where a.idAtendimento = :param")
	Optional<Atendimento> findById(@Param("param") Integer idAtendimento);
}
