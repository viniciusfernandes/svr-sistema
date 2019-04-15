package br.com.svr.service.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.svr.service.validacao.InformacaoValidavel;

@Entity
@Table(name = "tb_ramo_atividade", schema = "vendas")
@InformacaoValidavel
public class RamoAtividade implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2973917112551905075L;
	private boolean ativo = true;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 100 }, nomeExibicao = "Descri��o do ramo de atividade")
	private String descricao;

	@Id
	@SequenceGenerator(name = "ramoAtividadeSequence", sequenceName = "vendas.seq_ramo_atividade_id", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ramoAtividadeSequence")
	private Integer id;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 10 }, nomeExibicao = "Siga do ramo de atividade")
	private String sigla;

	public String getDescricao() {
		return descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getSigla() {
		return sigla;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
}
