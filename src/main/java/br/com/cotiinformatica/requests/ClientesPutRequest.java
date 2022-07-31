package br.com.cotiinformatica.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientesPutRequest {

	private Integer idCliente;
	private String nome;
	private String telefone;
	private String cpf;

}
