package br.com.svr.service.nfe;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.svr.service.validacao.InformacaoValidavel;

@InformacaoValidavel(campoIdentificacao = "codigo")
@XmlType(propOrder = { "codigo", "descricao", "ncm", "cest", "EXTIPI", "cfop", "unidadeComercial",
		"quantidadeComercial", "valorUnitarioComercializacao", "valorTotalBruto", "unidadeTributavel",
		"quantidadeTributavel", "valorUnitarioTributacao", "valorTotalFrete", "valorTotalSeguro", "valorDesconto",
		"outrasDespesasAcessorias", "indicadorValorTotal", "listaDeclaracaoImportacao", "listaDeclaracaoExportacao",
		"numeroPedidoCompra", "itemPedidoCompra", "fichaConteudoImportacao" })
public class ProdutoServicoNFe {
	@XmlElement(name = "CEST")
	@InformacaoValidavel(padrao = "\\d{7}", padraoExemplo = "7 d�gitos", nomeExibicao = "CEST do produtos/servi�os")
	private String cest;

	@InformacaoValidavel(obrigatorio = true, padrao = "\\d{4}", padraoExemplo = "4 digitos", nomeExibicao = "CFOP do produtos/servi�os")
	@XmlElement(name = "CFOP")
	private String cfop;

	@InformacaoValidavel(obrigatorio = true, intervaloNumerico = { 1, 60 }, nomeExibicao = "C�digo do produtos/servi�os")
	@XmlElement(name = "cProd")
	private String codigo;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 120 }, nomeExibicao = "Descri��o do produtos/servi�os")
	@XmlElement(name = "xProd")
	private String descricao;

	@InformacaoValidavel(intervaloComprimento = { 2, 3 }, nomeExibicao = "EXTIPI do produtos/servi�os")
	@XmlElement(name = "EXTIPI")
	private String EXTIPI;

	@InformacaoValidavel(tamanho = 36, nomeExibicao = "FCI de produtos/servi�os")
	@XmlElement(name = "nFCI")
	private String fichaConteudoImportacao;

	@InformacaoValidavel(obrigatorio = true, intervaloNumerico = { 0, 1 }, nomeExibicao = "Indicador de composi��o do valor total produtos/servi�os")
	@XmlElement(name = "indTot")
	private Integer indicadorValorTotal;

	@InformacaoValidavel(padrao = "\\d{6}", padraoExemplo = "6 digitos", prefixo = "0", tamanho = 6, nomeExibicao = "Item de pedido de compra de produtos/servi�os")
	@XmlElement(name = "nItemPed")
	private String itemPedidoCompra;

	@InformacaoValidavel(iteravel = true, nomeExibicao = "Declara��o de exporta��o do produto/servi�o")
	@XmlElement(name = "detExport")
	private List<DeclaracaoExportacao> listaDeclaracaoExportacao;

	@InformacaoValidavel(iteravel = true, nomeExibicao = "Declara��o de importa��o do produto/servi�o")
	@XmlElement(name = "DI")
	private List<DeclaracaoImportacao> listaDeclaracaoImportacao;

	@InformacaoValidavel(obrigatorio = true, tamanhos = { 2, 8 }, substituicao = { "\\D", "" }, nomeExibicao = "NCM do produtos/servi�os")
	@XmlElement(name = "NCM")
	private String ncm;

	@XmlElement(name = "xPed")
	@InformacaoValidavel(intervaloComprimento = { 1, 15 }, nomeExibicao = "N�mero de pedido de compra")
	private String numeroPedidoCompra;

	@InformacaoValidavel(decimal = { 15, 2 }, nomeExibicao = "Outras despesas acess�rias do produtos/servi�os")
	@XmlElement(name = "vOutro")
	private Double outrasDespesasAcessorias;

	@InformacaoValidavel(obrigatorio = true, decimal = { 13, 4 }, nomeExibicao = "Quantidade comercial do produtos/servi�os")
	@XmlElement(name = "qCom")
	private Double quantidadeComercial;

	@XmlElement(name = "qTrib")
	private Integer quantidadeTributavel;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 6 }, nomeExibicao = "Unidade comercial do produtos/servi�os")
	@XmlElement(name = "uCom")
	private String unidadeComercial;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 6 }, nomeExibicao = "Unidade tribut�vel do produtos/servi�os")
	@XmlElement(name = "uTrib")
	private String unidadeTributavel;

	@InformacaoValidavel(decimal = { 15, 2 }, nomeExibicao = "Valor total do desconto do produtos/servi�os")
	@XmlElement(name = "vDesc")
	private Double valorDesconto;

	@XmlElement(name = "vProd")
	private Double valorTotalBruto;

	@InformacaoValidavel(decimal = { 15, 2 }, nomeExibicao = "Valor total do frete do produtos/servi�os")
	@XmlElement(name = "vFrete")
	private Double valorTotalFrete;

	@InformacaoValidavel(decimal = { 15, 2 }, nomeExibicao = "Valor total do seguro do produtos/servi�os")
	@XmlElement(name = "vSeg")
	private Double valorTotalSeguro;

	@InformacaoValidavel(obrigatorio = true, decimal = { 21, 10 }, nomeExibicao = "Valor unit�rio comercial do produtos/servi�os")
	@XmlElement(name = "vUnCom")
	private Double valorUnitarioComercializacao;

	@InformacaoValidavel(obrigatorio = true, decimal = { 21, 10 }, nomeExibicao = "Valor unit�rio tributa��o do produtos/servi�os")
	@XmlElement(name = "vUnTrib")
	private Double valorUnitarioTributacao;

	public boolean contemImportacao() {
		return listaDeclaracaoImportacao != null && !listaDeclaracaoImportacao.isEmpty();
	}

	@XmlTransient
	public String getCest() {
		return cest;
	}

	@XmlTransient
	public String getCfop() {
		return cfop;
	}

	@XmlTransient
	public String getCodigo() {
		return codigo;
	}

	@XmlTransient
	public String getDescricao() {
		return descricao;
	}

	@XmlTransient
	public String getEXTIPI() {
		return EXTIPI;
	}

	@XmlTransient
	public String getFichaConteudoImportacao() {
		return fichaConteudoImportacao;
	}

	@XmlTransient
	public Integer getIndicadorValorTotal() {
		return indicadorValorTotal;
	}

	@XmlTransient
	public String getItemPedidoCompra() {
		return itemPedidoCompra;
	}

	@XmlTransient
	public List<DeclaracaoExportacao> getListaDeclaracaoExportacao() {
		return listaDeclaracaoExportacao;
	}

	@XmlTransient
	public List<DeclaracaoImportacao> getListaDeclaracaoImportacao() {
		return listaDeclaracaoImportacao;
	}

	@XmlTransient
	public List<DeclaracaoExportacao> getListaExportacao() {
		return getListaDeclaracaoExportacao();
	}

	// Metodo criado para simplificar a marcacao no .jsp
	@XmlTransient
	public List<DeclaracaoImportacao> getListaImportacao() {
		return getListaDeclaracaoImportacao();
	}

	@XmlTransient
	public String getNcm() {
		return ncm;
	}

	@XmlTransient
	public String getNumeroPedidoCompra() {
		return numeroPedidoCompra;
	}

	@XmlTransient
	public Double getOutrasDespesasAcessorias() {
		return outrasDespesasAcessorias == null ? 0 : outrasDespesasAcessorias;
	}

	@XmlTransient
	public Double getQuantidadeComercial() {
		return quantidadeComercial;
	}

	@XmlTransient
	public Integer getQuantidadeTributavel() {
		return quantidadeTributavel;
	}

	@XmlTransient
	public String getUnidadeComercial() {
		return unidadeComercial;
	}

	@XmlTransient
	public String getUnidadeTributavel() {
		return unidadeTributavel;
	}

	@XmlTransient
	public double getValorDesconto() {
		return valorDesconto == null ? 0 : valorDesconto;
	}

	@XmlTransient
	public Double getValorTotalBruto() {
		return valorTotalBruto == null ? 0 : valorTotalBruto;
	}

	@XmlTransient
	public Double getValorTotalFrete() {
		return valorTotalFrete == null ? 0 : valorTotalFrete;
	}

	@XmlTransient
	public Double getValorTotalSeguro() {
		return valorTotalSeguro == null ? 0 : valorTotalSeguro;
	}

	@XmlTransient
	public Double getValorUnitarioComercializacao() {
		return valorUnitarioComercializacao;
	}

	@XmlTransient
	public Double getValorUnitarioTributacao() {
		return valorUnitarioTributacao;
	}

	public void setCest(String cest) {
		this.cest = cest;
	}

	public void setCfop(String cfop) {
		this.cfop = cfop;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setEXTIPI(String eXTIPI) {
		EXTIPI = eXTIPI;
	}

	public void setFichaConteudoImportacao(String fichaConteudoImportacao) {
		this.fichaConteudoImportacao = fichaConteudoImportacao;
	}

	public void setIndicadorValorTotal(Integer indicadorValorTotal) {
		this.indicadorValorTotal = indicadorValorTotal;
	}

	public void setItemPedidoCompra(String itemPedidoCompra) {
		this.itemPedidoCompra = itemPedidoCompra;
	}

	public void setListaDeclaracaoExportacao(List<DeclaracaoExportacao> listaDeclaracaoExportacao) {
		this.listaDeclaracaoExportacao = listaDeclaracaoExportacao;
	}

	public void setListaDeclaracaoImportacao(List<DeclaracaoImportacao> listaDeclaracaoImportacao) {
		this.listaDeclaracaoImportacao = listaDeclaracaoImportacao;
	}

	public void setListaExportacao(List<DeclaracaoExportacao> listaDeclaracaoExportacao) {
		setListaDeclaracaoExportacao(listaDeclaracaoExportacao);
	}

	// Metodo criado para simplificar marcacao do .jsp
	public void setListaImportacao(List<DeclaracaoImportacao> listaDeclaracaoImportacao) {
		this.setListaDeclaracaoImportacao(listaDeclaracaoImportacao);
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	public void setNumeroPedidoCompra(String numeroPedidoCompra) {
		this.numeroPedidoCompra = numeroPedidoCompra;
	}

	public void setOutrasDespesasAcessorias(Double outrasDespesasAcessorias) {
		this.outrasDespesasAcessorias = outrasDespesasAcessorias;
	}

	public void setQuantidadeComercial(Double quantidadeComercial) {
		this.quantidadeComercial = quantidadeComercial;
	}

	public void setQuantidadeTributavel(Integer quantidadeTributavel) {
		this.quantidadeTributavel = quantidadeTributavel;
	}

	public void setUnidadeComercial(String unidadeComercial) {
		this.unidadeComercial = unidadeComercial;
	}

	public void setUnidadeTributavel(String unidadeTributavel) {
		this.unidadeTributavel = unidadeTributavel;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public void setValorTotalBruto(Double valorTotalBruto) {
		this.valorTotalBruto = valorTotalBruto;
	}

	public void setValorTotalFrete(Double valorTotalFrete) {
		this.valorTotalFrete = valorTotalFrete;
	}

	public void setValorTotalSeguro(Double valorTotalSeguro) {
		this.valorTotalSeguro = valorTotalSeguro;
	}

	public void setValorUnitarioComercializacao(Double valorUnitarioComercializacao) {
		this.valorUnitarioComercializacao = valorUnitarioComercializacao;
	}

	public void setValorUnitarioTributacao(Double valorUnitarioTributacao) {
		this.valorUnitarioTributacao = valorUnitarioTributacao;
	}
}