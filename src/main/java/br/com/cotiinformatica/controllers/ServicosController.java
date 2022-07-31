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

import br.com.cotiinformatica.dtos.ServicosDTO;
import br.com.cotiinformatica.entities.Servico;
import br.com.cotiinformatica.repositories.IServicoRepository;
import br.com.cotiinformatica.requests.ServicosPostRequest;
import br.com.cotiinformatica.requests.ServicosPutRequest;
import io.swagger.annotations.ApiOperation;

@Transactional
@Controller
public class ServicosController {

	@Autowired
	private IServicoRepository servicoRepository;
	
	@ApiOperation("Serviço para cadastro de serviços profissionais")
	@RequestMapping(value = "/api/servicos", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody ServicosPostRequest request) {
		
		try {
			
			Servico servico = new Servico();
			
			servico.setNome(request.getNome());
			servico.setPreco(request.getPreco());
			
			servicoRepository.save(servico);
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Serviço cadastrado com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao cadastrar serviço: " + e.getMessage());
		}		
	}
	
	@ApiOperation("Serviço para atualização de serviços profissionais")
	@RequestMapping(value = "/api/servicos", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody ServicosPutRequest request) {
		
		try {
			
			Optional<Servico> optional = servicoRepository.findById(request.getIdServico());
			
			if(optional.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Serviço não encontrado.");
			
			Servico servico = optional.get();
			
			servico.setNome(request.getNome());
			servico.setPreco(request.getPreco());
			
			servicoRepository.save(servico);
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Serviço atualizado com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao cadastrar serviço: " + e.getMessage());
		}
		
	}
	
	@ApiOperation("Serviço para exclusão de serviços profissionais")
	@RequestMapping(value = "/api/servicos/{idServico}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable("idServico") Integer idServico) {
		
		try {
			
			Optional<Servico> optional = servicoRepository.findById(idServico);
			
			if(optional.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Serviço não encontrado.");
			
			Servico servico = optional.get();
			
			servicoRepository.delete(servico);
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Serviço excluído com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao cadastrar serviço: " + e.getMessage());
		}		
	}
	
	@ApiOperation("Serviço para consulta de serviços profissionais")
	@RequestMapping(value = "/api/servicos", method = RequestMethod.GET)
	public ResponseEntity<List<ServicosDTO>> getAll() {
		
		try {
			
			List<Servico> servicos = (List<Servico>) servicoRepository.findAll();
			List<ServicosDTO> lista = new ArrayList<ServicosDTO>();
			
			for(Servico servico : servicos) {
				
				ServicosDTO dto = new ServicosDTO();
				dto.setIdServico(servico.getIdServico());
				dto.setNome(servico.getNome());
				dto.setPreco(servico.getPreco());
				
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
	
	@ApiOperation("Serviço para consulta de serviço profissional por ID")
	@RequestMapping(value = "/api/servicos/{idServico}", method = RequestMethod.GET)
	public ResponseEntity<ServicosDTO> getById(@PathVariable("idServico") Integer idServico) {
		
		try {
			
			Optional<Servico> optional = servicoRepository.findById(idServico);
			
			if(optional.isEmpty())
				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body(null);
			
			Servico servico = optional.get();
			
			ServicosDTO dto = new ServicosDTO();			
			dto.setIdServico(servico.getIdServico());
			dto.setNome(servico.getNome());
			dto.setPreco(servico.getPreco());
			
			return ResponseEntity.status(HttpStatus.OK)
					.body(dto); 
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}		
	}
}
