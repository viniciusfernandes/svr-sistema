package br.com.svr.service.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.svr.service.constante.TipoAcesso;
import br.com.svr.service.validacao.InformacaoValidavel;
import br.com.svr.service.validacao.TipoDocumento;

@Entity
@Table(name = "tb_usuario", schema = "vendas")
@InformacaoValidavel
public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1885114358725849134L;

	private boolean ativo;

	@Column(name = "comissionado_simples")
	private boolean comissionadoSimples;

	@InformacaoValidavel(tipoDocumento = TipoDocumento.CPF, nomeExibicao = "CPF")
	private String cpf;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 50 }, nomeExibicao = "Email do usuario")
	private String email;

	@Column(name = "email_copia")
	@InformacaoValidavel(intervaloComprimento = { 0, 250 }, nomeExibicao = "Lista email para c�pia do usu�rio")
	private String emailCopia;

	@Id
	@SequenceGenerator(name = "usuarioSequence", sequenceName = "vendas.seq_usuario_id", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioSequence")
	private Integer id;

	@OneToMany(mappedBy = "vendedor", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	private List<Cliente> listaCliente;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@InformacaoValidavel(iteravel = true, nomeExibicao = "Lista de contato do usuario")
	private List<ContatoUsuario> listaContato;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_usuario_tb_perfil_acesso", schema = "vendas", joinColumns = { @JoinColumn(name = "id_usuario") }, inverseJoinColumns = { @JoinColumn(name = "id_perfil_acesso") })
	@InformacaoValidavel(nomeExibicao = "Perfil do usuario")
	private List<PerfilAcesso> listaPerfilAcesso;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_logradouro_usuario")
	@InformacaoValidavel(cascata = true, nomeExibicao = "Logradouro da usuario")
	private LogradouroUsuario logradouro;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 20 }, nomeExibicao = "Nome do usuario")
	private String nome;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 6, 30 }, nomeExibicao = "Senha do usuario")
	private String senha;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 40 }, nomeExibicao = "Sobrenome do usuario")
	private String sobrenome;

	public Usuario() {
	}

	public Usuario(Integer id) {
		this.id = id;
	}

	public Usuario(Integer id, String nome) {
		this(id);
		this.nome = nome;
	}

	public Usuario(Integer id, String nome, String sobrenome) {
		this(id, nome);
		this.sobrenome = sobrenome;
	}

	public Usuario(Integer id, String nome, String sobrenome, String email) {
		this(id, nome, sobrenome);
		this.email = email;
	}

	public void addCliente(Cliente cliente) {
		if (this.listaCliente == null) {
			this.listaCliente = new ArrayList<Cliente>();
		}
		cliente.setVendedor(this);
		this.listaCliente.add(cliente);
	}

	public void addContato(ContatoUsuario contato) {
		if (this.listaContato == null) {
			this.listaContato = new ArrayList<ContatoUsuario>();
		}
		this.listaContato.add(contato);
		contato.setUsuario(this);
	}

	public void addContato(List<ContatoUsuario> listaContato) {
		for (ContatoUsuario contato : listaContato) {
			this.addContato(contato);
		}
	}

	public void addPerfilAcesso(List<PerfilAcesso> listaPerfilAcesso) {
		if (this.listaPerfilAcesso == null) {
			this.listaPerfilAcesso = new ArrayList<PerfilAcesso>();
		}
		for (PerfilAcesso perfilAcesso : listaPerfilAcesso) {
			this.listaPerfilAcesso.add(perfilAcesso);
		}
	}

	public void addPerfilAcesso(PerfilAcesso perfilAcesso) {
		if (this.listaPerfilAcesso == null) {
			this.listaPerfilAcesso = new ArrayList<PerfilAcesso>();
		}
		this.listaPerfilAcesso.add(perfilAcesso);
	}

	public String getCpf() {
		return cpf;
	}

	public String getEmail() {
		return email;
	}

	public String getEmailCopia() {
		return emailCopia;
	}

	public Integer getId() {
		return id;
	}

	public List<Cliente> getListaCliente() {
		return listaCliente;
	}

	public List<ContatoUsuario> getListaContato() {
		return listaContato;
	}

	public List<PerfilAcesso> getListaPerfilAcesso() {
		return listaPerfilAcesso;
	}

	public LogradouroUsuario getLogradouro() {
		return logradouro;
	}

	public String getNome() {
		return nome;
	}

	public String getNomeCompleto() {
		return this.nome + " " + this.sobrenome;
	}

	public String getSenha() {
		return senha;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	private boolean isAcessoPermitido(TipoAcesso tipoAcesso) {
		if (listaPerfilAcesso == null) {
			return false;
		}
		for (PerfilAcesso perfilAcesso : listaPerfilAcesso) {
			if (tipoAcesso.toString().equals(perfilAcesso.getDescricao())) {
				return true;
			}
		}
		return false;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public boolean isComissionadoSimples() {
		return comissionadoSimples;
	}

	public boolean isComprador() {
		return isAcessoPermitido(TipoAcesso.CADASTRO_PEDIDO_COMPRA);
	}

	public boolean isVendedor() {
		return isAcessoPermitido(TipoAcesso.CADASTRO_PEDIDO_VENDAS);
	}

	public void limparListaCliente() {
		if (this.listaCliente != null) {
			this.listaCliente.clear();
		}
	}

	public void removerListaCliente(List<Cliente> listaCliente) {
		if (this.listaCliente != null) {
			this.listaCliente.removeAll(listaCliente);
		}
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public void setComissionadoSimples(boolean comissionadoSimples) {
		this.comissionadoSimples = comissionadoSimples;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEmailCopia(String emailCopia) {
		this.emailCopia = emailCopia;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	void setListaCliente(List<Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}

	public void setListaContato(List<ContatoUsuario> listaContato) {
		this.listaContato = listaContato;
	}

	public void setListaPerfilAcesso(List<PerfilAcesso> listaPerfilAcesso) {
		this.listaPerfilAcesso = listaPerfilAcesso;
	}

	public void setLogradouro(LogradouroUsuario logradouro) {
		this.logradouro = logradouro;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
}
