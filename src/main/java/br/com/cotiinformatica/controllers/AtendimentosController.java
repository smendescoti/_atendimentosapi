package br.com.cotiinformatica.controllers;

import java.text.SimpleDateFormat;
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

import br.com.cotiinformatica.dtos.AtendimentosDTO;
import br.com.cotiinformatica.dtos.ClientesDTO;
import br.com.cotiinformatica.dtos.ServicosDTO;
import br.com.cotiinformatica.entities.Atendimento;
import br.com.cotiinformatica.entities.Cliente;
import br.com.cotiinformatica.entities.Servico;
import br.com.cotiinformatica.repositories.IAtendimentoRepository;
import br.com.cotiinformatica.repositories.IClienteRepository;
import br.com.cotiinformatica.repositories.IServicoRepository;
import br.com.cotiinformatica.requests.AtendimentosPostRequest;
import br.com.cotiinformatica.requests.AtendimentosPutRequest;

@Controller
@Transactional
public class AtendimentosController {

	@Autowired
	private IAtendimentoRepository atendimentoRepository;

	@Autowired
	private IClienteRepository clienteRepository;

	@Autowired
	private IServicoRepository servicoRepository;
	
	@RequestMapping(value = "/api/atendimentos", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody AtendimentosPostRequest request) {
		
		try {
			
			Optional<Cliente> optCliente = clienteRepository.findById(request.getIdCliente());			
			if(optCliente.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Cliente não encontrado.");
			
			Cliente cliente = optCliente.get();
			
			List<Servico> servicos = new ArrayList<Servico>();
			for(Integer idServico : request.getIdsServicos()) {
				
				Optional<Servico> optServico = servicoRepository.findById(idServico);			
				if(optServico.isEmpty())
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Serviço não encontrado.");
			
				Servico servico = optServico.get();
				servicos.add(servico);
			}
			
			Atendimento atendimento = new Atendimento();
			
			atendimento.setData(new SimpleDateFormat("yyyy-MM-dd").parse(request.getData()));
			atendimento.setHora(request.getHora());
			atendimento.setObservacoes(request.getObservacoes());
			atendimento.setCliente(cliente);
			atendimento.setServicos(servicos);
			
			atendimentoRepository.save(atendimento);
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Atendimento cadastrado com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao cadastrar atendimento: " + e.getMessage());
		}		
	}
	
	@RequestMapping(value = "/api/atendimentos", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody AtendimentosPutRequest request) {
		
		try {
			
			Optional<Cliente> optCliente = clienteRepository.findById(request.getIdCliente());			
			if(optCliente.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Cliente não encontrado.");
			
			Cliente cliente = optCliente.get();
			
			List<Servico> servicos = new ArrayList<Servico>();
			for(Integer idServico : request.getIdsServicos()) {
				
				Optional<Servico> optServico = servicoRepository.findById(idServico);			
				if(optServico.isEmpty())
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Serviço não encontrado.");
			
				Servico servico = optServico.get();
				servicos.add(servico);
			}
			
			Optional<Atendimento> optAtendimento = atendimentoRepository.findById(request.getIdAtendimento());
			if(optAtendimento.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Atendimento não encontrado.");
			
			Atendimento atendimento = optAtendimento.get();
			
			atendimento.setData(new SimpleDateFormat("yyyy-MM-dd").parse(request.getData()));
			atendimento.setHora(request.getHora());
			atendimento.setObservacoes(request.getObservacoes());
			atendimento.setCliente(cliente);
			atendimento.setServicos(servicos);
			
			atendimentoRepository.save(atendimento);
			
			return ResponseEntity.status(HttpStatus.OK)
					.body("Atendimento atualizado com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao atualizar atendimento: " + e.getMessage());
		}		
	}
	
	@RequestMapping(value = "/api/atendimentos/{idAtendimento}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable("idAtendimento") Integer idAtendimento) {
		
		try {
						
			Optional<Atendimento> optAtendimento = atendimentoRepository.findById(idAtendimento);
			if(optAtendimento.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Atendimento não encontrado.");
			
			Atendimento atendimento = optAtendimento.get();
			atendimentoRepository.delete(atendimento);
			
			return ResponseEntity.status(HttpStatus.OK)
					.body("Atendimento excluído com sucesso.");
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Falha ao atualizar atendimento: " + e.getMessage());
		}		
	}
	
	@RequestMapping(value = "/api/atendimentos", method = RequestMethod.GET)
	public ResponseEntity<List<AtendimentosDTO>> getAll() {
		
		try {
			
			List<AtendimentosDTO> lista = new ArrayList<AtendimentosDTO>();
			
			//consultando os atendimentos no repositório
			for(Atendimento atendimento : atendimentoRepository.findAll()) {
				
				AtendimentosDTO dto = new AtendimentosDTO();
				dto.setCliente(new ClientesDTO());
				dto.setServicos(new ArrayList<ServicosDTO>());
				
				dto.setIdAtendimento(atendimento.getIdAtendimento());
				dto.setData(atendimento.getData());
				dto.setHora(atendimento.getHora());
				dto.setObservacoes(atendimento.getObservacoes());
				
				dto.getCliente().setIdCliente(atendimento.getCliente().getIdCliente());
				dto.getCliente().setNome(atendimento.getCliente().getNome());
				dto.getCliente().setCpf(atendimento.getCliente().getCpf());
				dto.getCliente().setTelefone(atendimento.getCliente().getTelefone());
				
				for(Servico servico : atendimento.getServicos()) {
					
					ServicosDTO servicoDto = new ServicosDTO();
					servicoDto.setIdServico(servico.getIdServico());
					servicoDto.setNome(servico.getNome());
					servicoDto.setPreco(servico.getPreco());
					
					dto.getServicos().add(servicoDto);
				}
				
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
	
	@RequestMapping(value = "/api/atendimentos/{idAtendimento}", method = RequestMethod.GET)
	public ResponseEntity<AtendimentosDTO> getById(@PathVariable("idAtendimento") Integer idAtendimento) {
		
		try {
			
			Optional<Atendimento> optAtendimento = atendimentoRepository.findById(idAtendimento);
			if(optAtendimento.isEmpty())
				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body(null);
			
			Atendimento atendimento = optAtendimento.get();
			
			AtendimentosDTO dto = new AtendimentosDTO();
			dto.setCliente(new ClientesDTO());
			dto.setServicos(new ArrayList<ServicosDTO>());
			
			dto.setIdAtendimento(atendimento.getIdAtendimento());
			dto.setData(atendimento.getData());
			dto.setHora(atendimento.getHora());
			dto.setObservacoes(atendimento.getObservacoes());
			
			dto.getCliente().setIdCliente(atendimento.getCliente().getIdCliente());
			dto.getCliente().setNome(atendimento.getCliente().getNome());
			dto.getCliente().setCpf(atendimento.getCliente().getCpf());
			dto.getCliente().setTelefone(atendimento.getCliente().getTelefone());
			
			for(Servico servico : atendimento.getServicos()) {
				
				ServicosDTO servicoDto = new ServicosDTO();
				servicoDto.setIdServico(servico.getIdServico());
				servicoDto.setNome(servico.getNome());
				servicoDto.setPreco(servico.getPreco());
				
				dto.getServicos().add(servicoDto);
			}
			
			return ResponseEntity.status(HttpStatus.OK)
					.body(dto);
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}		
	}
	
}
