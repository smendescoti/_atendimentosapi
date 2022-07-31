package br.com.cotiinformatica.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "atendimento")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Atendimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idatendimento")
	private Integer idAtendimento;

	@Temporal(TemporalType.DATE)
	@Column(name = "data", nullable = false)
	private Date data;

	@Column(name = "hora", length = 10, nullable = false)
	private String hora;

	@Column(name = "observacoes", length = 500, nullable = false)
	private String observacoes;

	@ManyToOne // Muitos atendimentos para 1 cliente
	@JoinColumn(name = "idcliente", nullable = false) //chave estrangeira
	private Cliente cliente;

	@ManyToMany //Muitos atendimentos para muitos serviços
	@JoinTable(
				//nome da tabela no banco de dados
				name = "atendimento_servico",
				//foreign key para a classe Atendimento
				joinColumns = @JoinColumn(name = "idatendimento"),
				//foreign key para a classe Servico
				inverseJoinColumns = @JoinColumn(name = "idservico")
			)
	private List<Servico> servicos;
}
