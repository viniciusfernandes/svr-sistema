package br.com.svr.service.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.svr.service.constante.TipoCliente;
import br.com.svr.service.constante.TipoLogradouro;
import br.com.svr.service.validacao.InformacaoValidavel;
import br.com.svr.service.validacao.TipoDocumento;

@Entity
@Table(name = "tb_cliente", schema = "vendas")
@InformacaoValidavel
public class Cliente implements Serializable {
	private static final long serialVersionUID = 4605031735495403363L;

	@InformacaoValidavel(intervaloComprimento = { 1, 15 }, tipoDocumento = TipoDocumento.CNPJ, nomeExibicao = "CNPJ do cliente")
	private String cnpj;

	@Transient
	private String contatoFormatado;

	@InformacaoValidavel(tipoDocumento = TipoDocumento.CPF, nomeExibicao = "CPF do cliente")
	private String cpf;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento")
	private Date dataNascimento;

	@Transient
	private String dataNascimentoFormatada;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_ultimo_contato")
	private Date dataUltimoContato;

	@Transient
	private String dataUltimoContatoFormatada;

	@Transient
	// Esse atributo foi incluido para gerar relatorio de cliente inativo
	private String dataUltimoPedidoFormatado;

	@Column(name = "documento_estrangeiro")
	@InformacaoValidavel(intervaloComprimento = { 0, 15 }, nomeExibicao = "Documento Estrangeiro")
	private String documentoEstrangeiro;

	@InformacaoValidavel(padrao = ".+@.+\\..{2,}", nomeExibicao = "Email do cliente")
	private String email;

	@Column(name = "email_cobranca")
	@InformacaoValidavel(padrao = ".+@.+\\..{2,}", nomeExibicao = "Email de cobran�a do cliente")
	private String emailCobranca;

	@Transient
	private String emailFormatado;

	@Id
	@SequenceGenerator(name = "clienteSequence", sequenceName = "vendas.seq_cliente_id", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clienteSequence")
	private Integer id;

	@Transient
	private Integer idUltimoPedido;

	@Column(name = "informacoes_adicionais")
	private String informacoesAdicionais;

	@Column(name = "insc_estadual")
	@InformacaoValidavel(intervaloComprimento = { 0, 15 }, tipoDocumento = TipoDocumento.INSCRICAO_ESTADUAL, nomeExibicao = "Inscri��o estadual do Cliente")
	private String inscricaoEstadual;

	@Column(name = "inscricao_suframa")
	@InformacaoValidavel(padrao = "\\d{8,9}", padraoExemplo = "de 8 a 9 digitos", nomeExibicao = "Inscri��o na SUFRAMA")
	private String inscricaoSUFRAMA;

	/*
	 * Tivemos que implementar como um Set pois o hibernate tem uma limitacao em
	 * fazer multiplos fetchs com o tipo list e collection. Isso apareceu na
	 * geracao do relatorio de cliente-regiao
	 */
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@InformacaoValidavel(iteravel = true, nomeExibicao = "Lista de contato do cliente")
	private Set<ContatoCliente> listaContato;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
	@InformacaoValidavel(iteravel = true, nomeExibicao = "Lista de logradouro do cliente")
	private List<LogradouroCliente> listaLogradouro;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_cliente_tb_transportadora", schema = "vendas", joinColumns = { @JoinColumn(name = "id_cliente", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "id_transportadora", referencedColumnName = "id") })
	private List<Transportadora> listaRedespacho;

	@Transient
	private Logradouro logradouro;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 150 }, nomeExibicao = "Nome fantasia do cliente")
	@Column(name = "nome_fantasia")
	private String nomeFantasia;

	@Transient
	private String nomeVendedor;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_ramo_atividade")
	@InformacaoValidavel(relacionamentoObrigatorio = true, nomeExibicao = "Ramo de atividade do cliente")
	private RamoAtividade ramoAtividade;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 150 }, nomeExibicao = "Razao social do cliente")
	@Column(name = "razao_social")
	private String razaoSocial;

	private String site;

	@Transient
	private String telefoneFormatado;

	@Enumerated
	@Column(name = "id_tipo_cliente")
	@InformacaoValidavel(obrigatorio = true, nomeExibicao = "Tipo de cliente")
	private TipoCliente tipoCliente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_vendedor", referencedColumnName = "id")
	private Usuario vendedor;

	public Cliente() {
	}

	// Construturo utilizado no relatorio de clientes por ramo de atividades
	public Cliente(ContatoCliente contatoCliente, String nomeVendedor, String razaoSocial, String sobrenomeVendedor) {
		addContato(contatoCliente);
		this.nomeVendedor = nomeVendedor + " " + sobrenomeVendedor;
		this.razaoSocial = razaoSocial;
	}

	public Cliente(Integer id) {
		this.id = id;
	}

	/*
	 * Construtor para popular o picklist da tela de vendedor
	 */
	public Cliente(Integer id, String nomeFantasia) {
		this.id = id;
		this.nomeFantasia = nomeFantasia;
	}

	public Cliente(Integer id, String nomeFantasia, String razaoSozial) {
		this(id, nomeFantasia);
		this.razaoSocial = razaoSozial;
	}

	public Cliente(Integer id, String nomeFantasia, String razaoSozial, String cnpj, String cpf,
			String inscricaoEstadual, String email) {
		this(id, nomeFantasia, razaoSozial);
		this.cnpj = cnpj;
		this.cpf = cpf;
		this.inscricaoEstadual = inscricaoEstadual;
		this.email = email;
	}

	public Cliente(Integer id, String nomeFantasia, String razaoSozial, String cnpj, String cpf,
			String inscricaoEstadual, String inscricaoSUFRAMA, String email) {
		this(id, nomeFantasia, razaoSozial, cnpj, cpf, inscricaoEstadual, email);
		this.inscricaoSUFRAMA = inscricaoSUFRAMA;
	}

	// Construturo utilizado no relatorio de clientes por ramo de atividades
	public Cliente(Set<ContatoCliente> listaContato, String nomeVendedor, String razaoSocial, String sobrenomeVendedor) {
		addContato(listaContato);
		this.nomeVendedor = nomeVendedor + " " + sobrenomeVendedor;
		this.razaoSocial = razaoSocial;
	}

	// Construturo utilizado no relatorio de clientes por ramo de atividades
	public Cliente(String dddContato, String ddiContato, String emailContato, String faxContato, String nomeContato,
			String nomeVendedor, String ramalContato, String razaoSocial, String sobreNomeVendedor,
			String telefoneContato) {

		ContatoCliente c = new ContatoCliente();
		c.setEmail(emailContato);
		c.setNome(nomeContato);
		c.setTelefone(telefoneContato);
		c.setDdd(dddContato);
		c.setDdi(ddiContato);
		c.setFax(faxContato);
		c.setRamal(ramalContato);
		addContato(c);

		this.nomeVendedor = nomeVendedor + " " + sobreNomeVendedor;
		this.razaoSocial = razaoSocial;
	}

	public void addContato(Collection<ContatoCliente> listaContato) {

		if (listaContato == null) {
			return;
		}

		for (ContatoCliente contatoCliente : listaContato) {
			this.addContato(contatoCliente);
		}
	}

	public void addContato(ContatoCliente contatoCliente) {
		if (contatoCliente == null) {
			return;
		}
		if (this.listaContato == null) {
			this.setListaContato(new HashSet<ContatoCliente>());
		}
		this.listaContato.add(contatoCliente);
		contatoCliente.setCliente(this);
	}

	public void addLogradouro(List<LogradouroCliente> listaLogradouro) {
		if (listaLogradouro == null) {
			return;
		}

		for (LogradouroCliente logradouroCliente : listaLogradouro) {
			addLogradouro(logradouroCliente);
		}
	}

	public void addLogradouro(LogradouroCliente logradouroCliente) {
		if (listaLogradouro == null) {
			setListaLogradouro(new ArrayList<LogradouroCliente>());
		}
		listaLogradouro.add(logradouroCliente);
		logradouroCliente.setCliente(this);
	}

	public void addRedespacho(List<Transportadora> listaRedespacho) {
		if (listaRedespacho == null) {
			return;
		}

		for (Transportadora transportadora : listaRedespacho) {
			addRedespacho(transportadora);
		}
	}

	public void addRedespacho(Transportadora transportadora) {
		if (this.listaRedespacho == null) {
			this.listaRedespacho = new ArrayList<Transportadora>();
		}
		this.listaRedespacho.add(transportadora);
	}

	public boolean contemContato() {
		return listaContato != null && listaContato.size() > 0;
	}

	public boolean contemLogradouro() {
		return listaLogradouro != null && listaLogradouro.size() > 0;
	}

	public void formatarContatoPrincipal() {
		Contato c = getContatoPrincipal();
		if (c == null) {
			return;
		}
		contatoFormatado = c.formatar();
	}

	public String getCnpj() {
		return cnpj;
	}

	public String getContatoFormatado() {
		return contatoFormatado;
	}

	public Contato getContatoPrincipal() {
		return isListaContatoPreenchida() ? listaContato.iterator().next() : null;
	}

	public String getCpf() {
		return cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public String getDataNascimentoFormatada() {
		return dataNascimentoFormatada;
	}

	public Date getDataUltimoContato() {
		return dataUltimoContato;
	}

	public String getDataUltimoContatoFormatada() {
		return dataUltimoContatoFormatada;
	}

	public String getDataUltimoPedidoFormatado() {
		return dataUltimoPedidoFormatado;
	}

	public String getDocumento() {
		return this.isJuridico() ? this.cnpj : this.cpf;
	}

	public String getDocumentoEstrangeiro() {
		return documentoEstrangeiro;
	}

	public String getEmail() {
		return email;
	}

	public String getEmailCobranca() {
		return emailCobranca;
	}

	public String getEmailFormatado() {
		return emailFormatado;
	}

	public Integer getId() {
		return id;
	}

	public Integer getIdUltimoPedido() {
		return idUltimoPedido;
	}

	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public String getInscricaoSUFRAMA() {
		return inscricaoSUFRAMA;
	}

	public Set<ContatoCliente> getListaContato() {
		return listaContato;
	}

	public List<LogradouroCliente> getListaLogradouro() {
		return listaLogradouro;
	}

	public List<Transportadora> getListaRedespacho() {
		return listaRedespacho;
	}

	public Logradouro getLogradouro() {
		return logradouro;
	}

	public String getNomeCompleto() {
		return this.getNomeFantasia() + " - " + this.getRazaoSocial();
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public String getNomeVendedor() {
		return nomeVendedor;
	}

	public RamoAtividade getRamoAtividade() {
		return ramoAtividade;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getSite() {
		return site;
	}

	public String getTelefoneFormatado() {
		return telefoneFormatado;
	}

	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public Usuario getVendedor() {
		return vendedor;
	}

	public boolean isJuridico() {
		return this.cnpj != null;
	}

	public boolean isListaContatoPreenchida() {
		return this.listaContato != null && !this.listaContato.isEmpty();
	}

	public boolean isListaLogradouroPreenchida() {
		return this.listaLogradouro != null && !this.listaLogradouro.isEmpty();
	}

	public boolean isNovo() {
		return id == null;
	}

	public boolean isRevendedor() {
		return TipoCliente.REVENDEDOR.equals(tipoCliente);
	}

	public void limparListaLogradouro() {
		if (this.listaLogradouro != null) {
			this.listaLogradouro.clear();
		}

	}

	private LogradouroCliente recuperarLogradouro(TipoLogradouro tipoLogradouro) {

		if (listaLogradouro == null || listaLogradouro.isEmpty() || tipoLogradouro == null) {
			return null;
		}

		for (LogradouroCliente l : listaLogradouro) {
			if (tipoLogradouro.equals(l.getTipoLogradouro())) {
				return l;
			}
		}
		return null;
	}

	public LogradouroCliente recuperarLogradouroFaturamento() {
		return recuperarLogradouro(TipoLogradouro.FATURAMENTO);
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setContatoFormatado(String contatoFormatado) {
		this.contatoFormatado = contatoFormatado;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public void setDataNascimentoFormatada(String dataNascimentoFormatada) {
		this.dataNascimentoFormatada = dataNascimentoFormatada;
	}

	public void setDataUltimoContato(Date dataUltimoContato) {
		this.dataUltimoContato = dataUltimoContato;
	}

	public void setDataUltimoContatoFormatada(String dataUltimoContatoFormatada) {
		this.dataUltimoContatoFormatada = dataUltimoContatoFormatada;
	}

	public void setDataUltimoPedidoFormatado(String dataUltimoPedidoFormatado) {
		this.dataUltimoPedidoFormatado = dataUltimoPedidoFormatado;
	}

	public void setDocumentoEstrangeiro(String documentoEstrangeiro) {
		this.documentoEstrangeiro = documentoEstrangeiro;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEmailCobranca(String emailCobranca) {
		this.emailCobranca = emailCobranca;
	}

	public void setEmailFormatado(String emailFormatado) {
		this.emailFormatado = emailFormatado;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdUltimoPedido(Integer idUltimoPedido) {
		this.idUltimoPedido = idUltimoPedido;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public void setInscricaoSUFRAMA(String inscricaoSUFRAMA) {
		this.inscricaoSUFRAMA = inscricaoSUFRAMA;
	}

	public void setListaContato(Set<ContatoCliente> listaContato) {
		this.listaContato = listaContato;
	}

	public void setListaLogradouro(List<LogradouroCliente> listalogradouro) {
		this.listaLogradouro = listalogradouro;
	}

	public void setListaRedespacho(List<Transportadora> listaRedespacho) {
		this.listaRedespacho = listaRedespacho;
	}

	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public void setNomeVendedor(String nomeVendedor) {
		this.nomeVendedor = nomeVendedor;
	}

	public void setRamoAtividade(RamoAtividade ramoAtividade) {
		this.ramoAtividade = ramoAtividade;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setTelefoneFormatado(String telefoneFormatado) {
		this.telefoneFormatado = telefoneFormatado;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}
}
