package br.com.cotiinformatica.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.cotiinformatica.dtos.ClientesDTO;
import br.com.cotiinformatica.entities.Cliente;
import br.com.cotiinformatica.repositories.IClienteRepository;
import br.com.cotiinformatica.requests.ClientesPostRequest;
import br.com.cotiinformatica.requests.ClientesPutRequest;
import io.swagger.annotations.ApiOperation;

@Transactional
@Controller
public class ClientesController {

	@Autowired
	private IClienteRepository clienteRepository;

	@ApiOperation("Serviço para cadastro de cliente.")
	@RequestMapping(value = "/api/clientes", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody ClientesPostRequest request) {

		try {
			
			Cliente cliente = new Cliente();
			
			cliente.setNome(request.getNome());
			cliente.setCpf(request.getCpf());
			cliente.setTelefone(request.getTelefone());
			
			if(clienteRepository.findByCpf(cliente.getCpf()) != null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("O Cpf informado já foi cadastrado para outro cliente.");
				
			clienteRepository.save(cliente);
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Cliente cadastrado com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao cadastrar cliente: " + e.getMessage());
		}
		
	}
	
	@ApiOperation("Serviço para atualização de cliente.")
	@RequestMapping(value = "/api/clientes", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody ClientesPutRequest request) {
	
		try {
			
			Optional<Cliente> optional = clienteRepository.findById(request.getIdCliente());
			
			if(optional.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Cliente não foi encontrado.");
				
			Cliente cliente = optional.get();
			
			Cliente clienteByCpf = clienteRepository.findByCpf(request.getCpf());
			if(clienteByCpf != null && clienteByCpf.getIdCliente() != cliente.getIdCliente())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("O Cpf informado já está cadastrado para outro cliente.");
			
			cliente.setNome(request.getNome());
			cliente.setCpf(request.getCpf());
			cliente.setTelefone(request.getTelefone());
			
			clienteRepository.save(cliente);
			
			return ResponseEntity.status(HttpStatus.OK)
					.body("Cliente atualizado com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao atualizar cliente: " + e.getMessage());
		}
	}	
	
	@ApiOperation("Serviço para exclusão de cliente.")
	@RequestMapping(value = "/api/clientes/{idCliente}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable("idCliente") Integer idCliente) {
		
		try {
			
			Optional<Cliente> optional = clienteRepository.findById(idCliente);
			
			if(optional.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Cliente não foi encontrado.");
			
			Cliente cliente = optional.get();
			
			clienteRepository.delete(cliente);
			
			return ResponseEntity.status(HttpStatus.OK)
					.body("Cliente excluído com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao excluir cliente: " + e.getMessage());
		}
	}
	
	@ApiOperation("Serviço para consulta de clientes.")
	@RequestMapping(value = "/api/clientes", method = RequestMethod.GET)
	public ResponseEntity<List<ClientesDTO>> getAll() {
		
		try {
			
			List<Cliente> clientes = (List<Cliente>) clienteRepository.findAll();
			
			List<ClientesDTO> lista = new ArrayList<ClientesDTO>();
			for(Cliente cliente : clientes) {
				
				ClientesDTO dto = new ClientesDTO();
				
				dto.setIdCliente(cliente.getIdCliente());
				dto.setNome(cliente.getNome());
				dto.setCpf(cliente.getCpf());
				dto.setTelefone(cliente.getTelefone());
				
				lista.add(dto);
			}
			
			return ResponseEntity.status(HttpStatus.OK)
					.body(lista);
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}		
	}
	
	@ApiOperation("Serviço para obter 1 cliente baseado no ID.")
	@RequestMapping(value = "/api/clientes/{idCliente}", method = RequestMethod.GET)
	public ResponseEntity<ClientesDTO> getById(@PathVariable("idCliente") Integer idCliente) {
		
		try {
			
			Optional<Cliente> optional = clienteRepository.findById(idCliente);
			
			if(optional.isEmpty())
				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body(null);
			
			Cliente cliente = optional.get();
			ClientesDTO dto = new ClientesDTO();
			
			dto.setIdCliente(cliente.getIdCliente());
			dto.setNome(cliente.getNome());
			dto.setCpf(cliente.getCpf());
			dto.setTelefone(cliente.getTelefone());
						
			return ResponseEntity.status(HttpStatus.OK)
					.body(dto);
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}		
	}
	
}











