package br.com.cotiinformatica.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServicosPutRequest {

	private Integer idServico;
	private String nome;
	private Double preco;
	
}
