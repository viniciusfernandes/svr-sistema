package br.com.svr.service.nfe;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.svr.service.nfe.constante.TipoTributacaoICMS;
import br.com.svr.service.validacao.InformacaoValidavel;

@InformacaoValidavel(campoCondicional = "codigoSituacaoTributaria", nomeExibicaoCampoCondicional = "C�digo de situa��o tribut�ria")
@XmlType(propOrder = { "origemMercadoria", "valorBCSTRetido", "valorSTRetido", "valorBCSTUFDestino",
		"valorSTUFDestino", "codigoSituacaoTributaria", "modalidadeDeterminacaoBC", "percentualReducaoBC", "valorBC",
		"aliquota", "valorOperacao", "percentualDiferimento", "valorDiferimento", "valor", "valorDesonerado",
		"motivoDesoneracao", "modalidadeDeterminacaoBCST", "percentualMargemValorAdicionadoICMSST",
		"percentualReducaoBCST", "valorBCST", "aliquotaST", "valorST", "percentualBCOperacaoPropria", "ufDividaST" })
public class ICMSGeral {

	@XmlElement(name = "pICMS")
	@InformacaoValidavel(obrigatorio = true, decimal = { 3, 4 }, nomeExibicao = "Al�quota do ICMS")
	private Double aliquota;

	@XmlElement(name = "pICMSST")
	@InformacaoValidavel(decimal = { 3, 4 }, tiposObrigatorios = { "10", "30", "60", "70", "90", "PART" }, nomeExibicao = "Al�quota ICMS ST")
	private Double aliquotaST;

	@XmlElement(name = "CST")
	@InformacaoValidavel(obrigatorio = true, nomeExibicao = "C�gido da situa��o tribu�ria do ICMS")
	private String codigoSituacaoTributaria;

	@XmlElement(name = "modBC")
	@InformacaoValidavel(obrigatorio = true, nomeExibicao = "Modalidade de determina��o da base de c�lculo do ICMS")
	private Integer modalidadeDeterminacaoBC;

	@XmlElement(name = "modBCST")
	@InformacaoValidavel(tiposObrigatorios = { "10", "30", "60", "70", "90", "PART" }, nomeExibicao = "Modalidade de determina��o do ST do ICMS � obrigat�rio")
	private Integer modalidadeDeterminacaoBCST;

	@XmlElement(name = "motDesICMS")
	@InformacaoValidavel(tamanho = 2, nomeExibicao = "Motivo de desonera��o do ICMS")
	private String motivoDesoneracao;

	@XmlElement(name = "orig")
	@InformacaoValidavel(obrigatorio = true, padrao = { "\\d" }, padraoExemplo = "1 digito", nomeExibicao = "Origem da mercadoria do ICMS")
	private String origemMercadoria;

	@XmlElement(name = "pBCOp")
	@InformacaoValidavel(decimal = { 3, 4 }, tiposObrigatorios = { "PART" }, nomeExibicao = "Percentual BC opera��o obrigat�ria")
	private Double percentualBCOperacaoPropria;

	@XmlElement(name = "pDif")
	@InformacaoValidavel(decimal = { 3, 4 }, tiposPermitidos = { "51" }, nomeExibicao = "Percentual de diferimento do ICMS")
	private Double percentualDiferimento;

	@XmlElement(name = "pMVAST")
	@InformacaoValidavel(decimal = { 3, 4 }, tiposObrigatorios = { "30" }, nomeExibicao = "Percentual de margem do valor adicionado do ST do ICMS")
	private Double percentualMargemValorAdicionadoICMSST;

	@XmlElement(name = "pRedBC")
	@InformacaoValidavel(decimal = { 3, 4 }, tiposObrigatorios = { "20", "51", "60", "70" }, nomeExibicao = "Percentual de redu��o de BC do ICMS")
	private Double percentualReducaoBC;

	@XmlElement(name = "pRedBCST")
	@InformacaoValidavel(decimal = { 3, 4 }, nomeExibicao = "Percentual de redu��o de BC do ST do ICMS")
	private Double percentualReducaoBCST;

	@XmlElement(name = "UFST")
	@InformacaoValidavel(tiposObrigatorios = { "PART" }, nomeExibicao = "UF de partilha do ICMS")
	private String ufDividaST;

	@InformacaoValidavel(decimal = { 13, 2 }, tiposNaoPermitidos = { "40", "41", "50", "70" }, nomeExibicao = "Valor calculado do ICMS")
	@XmlElement(name = "vICMS")
	private Double valor;

	@XmlElement(name = "vBC")
	@InformacaoValidavel(obrigatorio = true, decimal = { 13, 2 }, nomeExibicao = "Valor da base de c�lculo do ICMS � obrigat�rio")
	private Double valorBC;

	@XmlElement(name = "vBCST")
	@InformacaoValidavel(decimal = { 13, 2 }, tiposObrigatorios = { "10", "30", "60", "70", "90", "PART" }, nomeExibicao = "Valor da base de c�lculo ST do ICMS")
	private Double valorBCST;

	@XmlElement(name = "vBCSTRet")
	@InformacaoValidavel(decimal = { 13, 2 }, tiposObrigatorios = { "60" }, nomeExibicao = "Valor BC ST retido")
	private Double valorBCSTRetido;

	@XmlElement(name = "vBCSTDest")
	@InformacaoValidavel(decimal = { 13, 2 }, tiposObrigatorios = { "PART" }, nomeExibicao = "Valor BC ST retido")
	private Double valorBCSTUFDestino;

	@XmlElement(name = "vICMSDeson")
	@InformacaoValidavel(decimal = { 13, 2 }, nomeExibicao = "Valor desonerado do ICMS")
	private Double valorDesonerado;

	@XmlElement(name = "vICMSDif")
	@InformacaoValidavel(decimal = { 13, 2 }, tiposPermitidos = { "51" }, nomeExibicao = "Valor de diferimento do ICMS")
	private Double valorDiferimento;

	@XmlElement(name = "vICMSOp")
	@InformacaoValidavel(decimal = { 13, 2 }, tiposPermitidos = { "51" }, nomeExibicao = "Valor da opera��o do ICMS")
	private Double valorOperacao;

	@XmlElement(name = "vICMSST")
	@InformacaoValidavel(decimal = { 13, 2 }, tiposObrigatorios = { "10", "30", "60", "70", "90", "PART" }, nomeExibicao = "Valor ICMS ST")
	private Double valorST;

	@XmlElement(name = "vICMSSTRet")
	@InformacaoValidavel(decimal = { 13, 2 }, tiposObrigatorios = { "60" }, nomeExibicao = "Valor ST retido")
	private Double valorSTRetido;

	@XmlElement(name = "vICMSSTDest")
	@InformacaoValidavel(decimal = { 13, 2 }, tiposObrigatorios = { "PART" }, nomeExibicao = "Valor ICMS ST da UF de destino")
	private Double valorSTUFDestino;

	public Double calcularValor() {
		return valorBC != null && aliquota != null ? valorBC * (aliquota / 100d) : null;
	}

	public Double calcularValorST() {
		return valorBCST != null && aliquotaST != null ? valorBCST * (aliquotaST / 100d) : null;
	}

	public ICMSGeral carregarValores() {
		valor = calcularValor();
		valorST = calcularValorST();
		return this;
	}

	@XmlTransient
	public Double getAliquota() {
		return aliquota;
	}

	@XmlTransient
	public Double getAliquotaST() {
		return aliquotaST;
	}

	@XmlTransient
	public String getCodigoSituacaoTributaria() {
		return codigoSituacaoTributaria;
	}

	@XmlTransient
	public Integer getModalidadeDeterminacaoBC() {
		return modalidadeDeterminacaoBC;
	}

	@XmlTransient
	public Integer getModalidadeDeterminacaoBCST() {
		return modalidadeDeterminacaoBCST;
	}

	@XmlTransient
	public String getMotivoDesoneracao() {
		return motivoDesoneracao;
	}

	@XmlTransient
	public String getOrigemMercadoria() {
		return origemMercadoria;
	}

	@XmlTransient
	public Double getPercentualBCOperacaoPropria() {
		return percentualBCOperacaoPropria;
	}

	@XmlTransient
	public Double getPercentualDiferimento() {
		return percentualDiferimento;
	}

	@XmlTransient
	public Double getPercentualMargemValorAdicionadoICMSST() {
		return percentualMargemValorAdicionadoICMSST;
	}

	@XmlTransient
	public Double getPercentualReducaoBC() {
		return percentualReducaoBC;
	}

	@XmlTransient
	public Double getPercentualReducaoBCST() {
		return percentualReducaoBCST;
	}

	@XmlTransient
	public TipoTributacaoICMS getTipoTributacao() {
		return TipoTributacaoICMS.getTipoTributacao(codigoSituacaoTributaria);
	}

	@XmlTransient
	public String getUfDividaST() {
		return ufDividaST;
	}

	@XmlTransient
	public Double getValor() {
		return valor == null ? 0 : valor;
	}

	@XmlTransient
	public Double getValorBC() {
		return valorBC == null ? 0 : valorBC;
	}

	@XmlTransient
	public Double getValorBCST() {
		// Nao podemos verificar se o valor eh null e retornar zero como nos
		// outros campos pois, no caso de ICMS em que nao eh substituicao
		// tributaria, o valor de aliquota zero esta sendo serializado no CML da
		// Nfe e isso nao deve ocorrer.
		return valorBCST;
	}

	@XmlTransient
	public Double getValorBCSTRetido() {
		return valorBCSTRetido;
	}

	@XmlTransient
	public Double getValorBCSTUFDestino() {
		return valorBCSTUFDestino;
	}

	@XmlTransient
	public Double getValorDesonerado() {
		return valorDesonerado == null ? 0 : valorDesonerado;
	}

	@XmlTransient
	public Double getValorDiferimento() {
		return valorDiferimento;
	}

	@XmlTransient
	public Double getValorOperacao() {
		return valorOperacao;
	}

	@XmlTransient
	public Double getValorST() {
		return valorST == null ? 0 : valorST;
	}

	@XmlTransient
	public Double getValorSTRetido() {
		return valorSTRetido;
	}

	@XmlTransient
	public Double getValorSTUFDestino() {
		return valorSTUFDestino;
	}

	public void setAliquota(Double aliquota) {
		this.aliquota = aliquota;
	}

	public void setAliquotaST(Double aliquotaST) {
		this.aliquotaST = aliquotaST;
	}

	public void setCodigoSituacaoTributaria(String codigoSituacaoTributaria) {
		this.codigoSituacaoTributaria = codigoSituacaoTributaria;
	}

	public void setModalidadeDeterminacaoBC(Integer modalidadeDeterminacaoBC) {
		this.modalidadeDeterminacaoBC = modalidadeDeterminacaoBC;
	}

	public void setModalidadeDeterminacaoBCST(Integer modalidadeDeterminacaoBCST) {
		this.modalidadeDeterminacaoBCST = modalidadeDeterminacaoBCST;
	}

	public void setMotivoDesoneracao(String motivoDesoneracao) {
		this.motivoDesoneracao = motivoDesoneracao;
	}

	public void setOrigemMercadoria(String origemMercadoria) {
		this.origemMercadoria = origemMercadoria;
	}

	public void setPercentualBCOperacaoPropria(Double percentualBCOperacaoPropria) {
		this.percentualBCOperacaoPropria = percentualBCOperacaoPropria;
	}

	public void setPercentualDiferimento(Double percentualDiferimento) {
		this.percentualDiferimento = percentualDiferimento;
	}

	public void setPercentualMargemValorAdicionadoICMSST(Double percentualMargemValorAdicionadoICMSST) {
		this.percentualMargemValorAdicionadoICMSST = percentualMargemValorAdicionadoICMSST;
	}

	public void setPercentualReducaoBC(Double percentualReducaoBC) {
		this.percentualReducaoBC = percentualReducaoBC;
	}

	public void setPercentualReducaoBCST(Double percentualReducaoBCST) {
		this.percentualReducaoBCST = percentualReducaoBCST;
	}

	public void setUfDividaST(String ufDividaST) {
		this.ufDividaST = ufDividaST;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public void setValorBC(Double valorBC) {
		this.valorBC = valorBC;
	}

	public void setValorBCST(Double valorBCST) {
		this.valorBCST = valorBCST;
	}

	public void setValorBCSTRetido(Double valorBCSTRetido) {
		this.valorBCSTRetido = valorBCSTRetido;
	}

	public void setValorBCSTUFDestino(Double valorBCSTUFDestino) {
		this.valorBCSTUFDestino = valorBCSTUFDestino;
	}

	public void setValorDesonerado(Double valorDesonerado) {
		this.valorDesonerado = valorDesonerado;
	}

	public void setValorDiferimento(Double valorDiferimento) {
		this.valorDiferimento = valorDiferimento;
	}

	public void setValorOperacao(Double valorOperacao) {
		this.valorOperacao = valorOperacao;
	}

	public void setValorST(Double valorST) {
		this.valorST = valorST;
	}

	public void setValorSTRetido(Double valorSTRetido) {
		this.valorSTRetido = valorSTRetido;
	}

	public void setValorSTUFDestino(Double valorSTUFDestino) {
		this.valorSTUFDestino = valorSTUFDestino;
	}

}