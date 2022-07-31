package br.com.cotiinformatica.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientesDTO {

	private Integer idCliente;
	private String nome;
	private String cpf;
	private String telefone;	
}
