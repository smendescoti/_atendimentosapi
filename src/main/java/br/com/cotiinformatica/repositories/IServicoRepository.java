package br.com.cotiinformatica.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.cotiinformatica.entities.Servico;

public interface IServicoRepository extends CrudRepository<Servico, Integer> {

}
