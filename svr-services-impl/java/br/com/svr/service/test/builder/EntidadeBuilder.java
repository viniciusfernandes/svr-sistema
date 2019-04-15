package br.com.svr.service.test.builder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.svr.service.constante.FormaMaterial;
import br.com.svr.service.constante.SituacaoPedido;
import br.com.svr.service.constante.TipoAcesso;
import br.com.svr.service.constante.TipoCliente;
import br.com.svr.service.constante.TipoEntrega;
import br.com.svr.service.constante.TipoFinalidadePedido;
import br.com.svr.service.constante.TipoLogradouro;
import br.com.svr.service.constante.TipoPagamento;
import br.com.svr.service.constante.TipoRelacionamento;
import br.com.svr.service.constante.TipoVenda;
import br.com.svr.service.entity.Bairro;
import br.com.svr.service.entity.Cidade;
import br.com.svr.service.entity.Cliente;
import br.com.svr.service.entity.Contato;
import br.com.svr.service.entity.ContatoCliente;
import br.com.svr.service.entity.Endereco;
import br.com.svr.service.entity.ItemEstoque;
import br.com.svr.service.entity.ItemPedido;
import br.com.svr.service.entity.Logradouro;
import br.com.svr.service.entity.LogradouroCliente;
import br.com.svr.service.entity.LogradouroEndereco;
import br.com.svr.service.entity.LogradouroPedido;
import br.com.svr.service.entity.LogradouroRepresentada;
import br.com.svr.service.entity.LogradouroTransportadora;
import br.com.svr.service.entity.LogradouroUsuario;
import br.com.svr.service.entity.Material;
import br.com.svr.service.entity.Pagamento;
import br.com.svr.service.entity.Pais;
import br.com.svr.service.entity.Pedido;
import br.com.svr.service.entity.PerfilAcesso;
import br.com.svr.service.entity.RamoAtividade;
import br.com.svr.service.entity.Representada;
import br.com.svr.service.entity.Transportadora;
import br.com.svr.service.entity.Usuario;
import br.com.svr.service.nfe.constante.TipoModalidadeFrete;
import br.com.svr.service.test.TestUtils;
import br.com.svr.service.validacao.ValidadorDocumento;

public class EntidadeBuilder {
	private static final EntidadeBuilder builder = new EntidadeBuilder();

	public static EntidadeBuilder getInstance() {
		return builder;
	}

	private EntidadeRepository repositorio = EntidadeRepository.getInstance();

	private int sequencia = 0;

	private EntidadeBuilder() {
	}

	public Cliente buildCliente() {
		Cliente cliente = new Cliente();
		double no = gerarSequencia();
		cliente.addLogradouro(buildLogradouroCliente(TipoLogradouro.FATURAMENTO));
		cliente.addLogradouro(buildLogradouroCliente(TipoLogradouro.ENTREGA));
		cliente.addLogradouro(buildLogradouroCliente(TipoLogradouro.COBRANCA));
		cliente.setRazaoSocial("Exercito Brasileiro " + no);
		cliente.setNomeFantasia("Exercito Brasileiro " + no);
		cliente.setCnpj(ValidadorDocumento.gerarCNPJ());
		cliente.setEmail(cliente.getCnpj() + "@cliente.com.br");
		cliente.setTipoCliente(TipoCliente.NORMAL);
		return cliente;
	}

	public Cliente buildClienteOrcamento() {
		Cliente cliente = new Cliente();
		cliente.setNomeFantasia("Exercito Brasileiro");
		cliente.setTipoCliente(TipoCliente.NORMAL);

		ContatoCliente contato = new ContatoCliente();
		contato.setNome("Andre Marcondes");
		contato.setEmail("andre@gmail.com");
		contato.setTelefone("999999999");
		contato.setDdd("11");
		contato.setRamal("4444");
		contato.setFax("88888888");

		cliente.addContato(contato);

		return cliente;
	}

	public Cliente buildClienteRevendedor() {
		Cliente cliente = buildCliente();
		double no = gerarSequencia();
		cliente.setCnpj(ValidadorDocumento.gerarCNPJ());
		cliente.setRazaoSocial("Revendedor de Plasticos" + no);
		cliente.setNomeFantasia("Revendedor de Plasticos LTDA" + no);
		cliente.setEmail("revendplastcios@gmail.com.br");
		cliente.setTipoCliente(TipoCliente.REVENDEDOR);
		return cliente;
	}

	public Cliente buildClienteVendedor() {
		Cliente c = buildCliente();
		c.setVendedor(buildUsuario());
		return c;
	}

	public Endereco buildEndereco() {
		Bairro bairro = new Bairro();
		bairro.setDescricao("Centro");

		Cidade cidade = new Cidade();
		cidade.setDescricao("Sao Paulo");
		cidade.setUf("SP");

		Pais pais = new Pais();
		pais.setDescricao("Brasil");

		Endereco endereco = new Endereco(bairro, cidade, pais);
		endereco.setCep("09910456");
		endereco.setDescricao("Av Paulista");

		repositorio.inserirEntidade(bairro);
		repositorio.inserirEntidade(cidade);
		repositorio.inserirEntidade(pais);
		repositorio.inserirEntidade(endereco);
		return endereco;
	}

	public Representada buildFornecedor() {
		Representada representada = buildRepresentada();
		representada.setNomeFantasia("Fornecedor Plastico");
		representada.setEmail("fornecedorplastico@cobex.com.br");
		representada.setRazaoSocial("Fornecedor Plastico LTDA");
		representada.setTipoRelacionamento(TipoRelacionamento.FORNECIMENTO);
		return representada;
	}

	public ItemEstoque buildItemEstoque() {
		ItemEstoque item = new ItemEstoque();
		item.setFormaMaterial(FormaMaterial.CH);
		item.setMedidaExterna(200.0);
		item.setMedidaInterna(100.0);
		item.setComprimento(1000.0);
		item.setPrecoMedio(100.0);
		item.setQuantidade(10);
		return item;
	}

	public ItemEstoque buildItemEstoquePeca() {
		ItemEstoque item = buildItemEstoque();
		item.setFormaMaterial(FormaMaterial.PC);

		item.setDescricaoPeca("ENGRENAGEM PARA TRATOR");

		item.setMedidaExterna(null);
		item.setMedidaInterna(null);
		item.setComprimento(null);

		item.setMaterial(buildMaterial());
		item.setAliquotaIPI(0.1d);
		return item;

	}

	public ItemPedido buildItemPedido() {
		ItemPedido i = new ItemPedido();
		i.setAliquotaIPI(0.11d);
		i.setAliquotaICMS(0.06d);
		i.setMaterial(buildMaterial());
		i.setFormaMaterial(FormaMaterial.TB);
		i.setQuantidade(2);
		i.setMedidaExterna(120d);
		i.setMedidaInterna(100d);
		i.setComprimento(1000d);
		i.setTipoVenda(TipoVenda.KILO);
		i.setPrecoVenda(60d);
		i.setNcm("36.39.90.90");
		return i;
	}

	public ItemPedido buildItemPedidoPeca() {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setAliquotaIPI(11.1d);
		itemPedido.setAliquotaICMS(0.06d);
		itemPedido.setMaterial(buildMaterial());
		itemPedido.setFormaMaterial(FormaMaterial.PC);
		itemPedido.setQuantidade(2);
		itemPedido.setMedidaExterna(null);
		itemPedido.setMedidaInterna(null);
		itemPedido.setComprimento(null);
		itemPedido.setTipoVenda(TipoVenda.PECA);
		itemPedido.setDescricaoPeca("ENGRENAGEM DE TESTE");
		itemPedido.setPrecoVenda(60d);
		return itemPedido;
	}

	public List<PerfilAcesso> buildListaPerfilAcesso() {
		List<PerfilAcesso> l = new LinkedList<PerfilAcesso>();
		for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
			PerfilAcesso p = new PerfilAcesso();
			p.setDescricao(tipoAcesso.toString());
			l.add(p);
		}
		return l;
	}

	private <T extends Logradouro> T buildLogradouro(Class<T> classe, TipoLogradouro tipo) {
		T l;
		try {
			l = classe.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		l.setBairro("Centro");
		l.setCep("09910470");
		l.setCidade("Diadema");
		l.setCodificado(true);
		l.setCodigoMunicipio("321654");
		l.setComplemento("Fundos");
		l.setEndereco("Rua Viscondo do Rio Branco");
		l.setNumero("3345");
		l.setPais("Brasil");
		l.setTipoLogradouro(tipo);
		l.setUf("SP");

		return l;
	}

	public LogradouroCliente buildLogradouroCliente(TipoLogradouro tipo) {
		return buildLogradouro(LogradouroCliente.class, tipo);
	}

	public LogradouroEndereco buildLogradouroEndereco(TipoLogradouro tipoLogradouro) {
		LogradouroEndereco logradouro = new LogradouroEndereco(buildEndereco());
		logradouro.setTipoLogradouro(tipoLogradouro);
		return logradouro;
	}

	public LogradouroPedido buildLogradouroPedido(TipoLogradouro tipo) {
		return buildLogradouro(LogradouroPedido.class, tipo);
	}

	public LogradouroRepresentada buildLogradouroRepresentada(TipoLogradouro tipo) {
		return buildLogradouro(LogradouroRepresentada.class, tipo);
	}

	public LogradouroTransportadora buildLogradouroTransportadora(TipoLogradouro tipo) {
		return buildLogradouro(LogradouroTransportadora.class, tipo);
	}

	public LogradouroUsuario buildLogradouroUsuario(TipoLogradouro tipo) {
		return buildLogradouro(LogradouroUsuario.class, tipo);
	}

	public Material buildMaterial() {
		Material material = new Material();
		material.setSigla("PLAST");
		material.setDescricao("PLASTICO DURO");
		material.setPesoEspecifico(0.33);
		return material;
	}

	public Pagamento buildPagamento() {
		Pagamento p = new Pagamento();
		p.setDataRecebimento(new Date());
		p.setDataVencimento(new Date());
		p.setDescricao("SALARIO DE VENDEDOR");
		p.setModalidadeFrete(Integer.parseInt(TipoModalidadeFrete.SEM_FRETE.getCodigo()));
		p.setParcela(1);
		p.setTipoPagamento(TipoPagamento.FOLHA_PAGAMENTO);
		p.setTotalParcelas(1);
		p.setValor(2000.0);
		return p;
	}

	public Pagamento buildPagamentoNF() {
		Pagamento p = buildPagamento();
		p.setTotalParcelas(3);
		p.setNumeroNF(1234);
		return p;
	}

	public Pedido buildPedido() {
		Usuario vendedor = buildUsuario();
		Cliente cliente = buildCliente();
		cliente.setVendedor(vendedor);

		Representada representada = buildRepresentada();
		Contato contato = new Contato();
		contato.setNome("Adriano");
		contato.setDdd("11");
		contato.setDdi("55");
		contato.setTelefone("99996321");
		contato.setEmail("adriano@plastecno.com.br");

		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setRepresentada(representada);
		pedido.setProprietario(vendedor);
		pedido.setSituacaoPedido(SituacaoPedido.DIGITACAO);
		pedido.setFinalidadePedido(TipoFinalidadePedido.CONSUMO);
		pedido.setContato(contato);
		pedido.setTipoEntrega(TipoEntrega.CIF);
		pedido.setDataEntrega(TestUtils.gerarDataAmanha());
		pedido.setFormaPagamento("30 / 40 dias uteis");
		return pedido;
	}

	public Pedido buildPedidoRevenda() {
		Pedido pedido = buildPedido();
		pedido.setRepresentada(buildRepresentadaRevendedora());
		return pedido;
	}

	public RamoAtividade buildRamoAtividade() {
		RamoAtividade ramoAtividade = new RamoAtividade();
		ramoAtividade.setAtivo(true);
		ramoAtividade.setDescricao("Industria Belica");
		ramoAtividade.setSigla("IB");
		return ramoAtividade;
	}

	public Representada buildRepresentada() {
		Representada r = new Representada(null, "COBEX");
		r.setAtivo(true);
		r.setNomeFantasia(r.getNomeFantasia() + " " + gerarSequencia());
		r.setRazaoSocial("COBEX LTDA");
		r.setEmail("vendas@cobex.com.br");
		r.setComissao(0.05);
		r.setTipoRelacionamento(TipoRelacionamento.REPRESENTACAO);
		r.setAliquotaICMS(0.18);
		r.setCnpj(ValidadorDocumento.gerarCNPJ());
		r.setInscricaoEstadual("123456789");

		LogradouroRepresentada l = buildLogradouroRepresentada(TipoLogradouro.FATURAMENTO);
		l.setCep("09910345");
		l.setEndereco("Rua Parnamirim");
		l.setNumero("432");
		l.setCidade("Diadema");
		l.setComplemento("Conjunto 330");
		r.setLogradouro(l);

		return r;
	}

	public Representada buildRepresentadaRevendedora() {
		Representada r = buildRepresentada();
		r.setNomeFantasia("Revendedor Plastico");
		r.setEmail("revendedorplastico@gmail.com.br");
		r.setRazaoSocial("Revendedor Plastico LTDA");
		r.setTipoRelacionamento(TipoRelacionamento.REVENDA);
		return r;
	}

	public Cliente buildRevendedor() {
		Cliente cliente = buildCliente();
		double no = gerarSequencia();
		cliente.setNomeFantasia("Acr�licos Merediano No " + no);
		cliente.setRazaoSocial("Acr�licos Merediano LTDA " + no);
		cliente.setTipoCliente(TipoCliente.REVENDEDOR);
		return cliente;
	}

	public Transportadora buildTransportadora() {
		Transportadora t = new Transportadora();
		t.setAtivo(true);
		t.setCnpj("03233998000162");
		t.setInscricaoEstadual("1234567");
		t.setLogradouro(buildLogradouroTransportadora(TipoLogradouro.COMERCIAL));
		t.setNomeFantasia("Transport Fim");
		t.setRazaoSocial("Transport Fim LTDA");
		return t;
	}

	public Usuario buildUsuario() {
		int i = gerarSequencia();
		Usuario vendedor = new Usuario(null, "Vinicius " + i, "Apolonio");
		vendedor.setEmail("vendedor_" + i + "@teste.com.br");
		vendedor.setEmailCopia("vendedorcopia_" + i + "@hotmail.com.br;compradorcopia_" + i + "@hotmail.com.br");
		vendedor.setSenha("1234567");
		vendedor.setAtivo(true);
		return vendedor;
	}

	private int gerarSequencia() {
		return sequencia++;
	}

}
