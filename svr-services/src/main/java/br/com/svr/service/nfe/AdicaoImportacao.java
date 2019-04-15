package br.com.svr.service.nfe;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.svr.service.validacao.InformacaoValidavel;

@InformacaoValidavel
@XmlType(propOrder = { "numero", "numeroSequencialItem", "codigoFabricante", "valorDesconto", "numeroDrawback" })
public class AdicaoImportacao {

	@InformacaoValidavel(obrigatorio = true, padrao = "\\d{1,60}", padraoExemplo = "de 1 a 60 digitos", nomeExibicao = "C�gido do fabricante da adi��o de importa��o")
	@XmlElement(name = "cFabricante")
	private String codigoFabricante;

	@InformacaoValidavel(obrigatorio = true, padrao = "\\d{1,3}", padraoExemplo = "de 1 a 3 digitos", nomeExibicao = "N�mero da adi��o de importa��o")
	@XmlElement(name = "nAdicao")
	private String numero;

	@InformacaoValidavel(padrao = "\\d{1,3}", padraoExemplo = "de 1 a 3 digitos", nomeExibicao = "N�mero da adi��o de importa��o")
	@XmlElement(name = "nDraw")
	private String numeroDrawback;

	@InformacaoValidavel(obrigatorio = true, padrao = "\\d{1,3}", padraoExemplo = "de 1 a 3 digitos", nomeExibicao = "N�mero da adi��o de importa��o")
	@XmlElement(name = "nSeqAdic")
	private String numeroSequencialItem;

	@InformacaoValidavel(decimal = { 13, 2 }, nomeExibicao = "Valor do desconto da adi��o de importa��o")
	@XmlElement(name = "vDescDI")
	private Double valorDesconto;

	@XmlTransient
	public String getCodigoFabricante() {
		return codigoFabricante;
	}

	@XmlTransient
	public String getNumero() {
		return numero;
	}

	@XmlTransient
	public String getNumeroDrawback() {
		return numeroDrawback;
	}

	@XmlTransient
	public String getNumeroSequencialItem() {
		return numeroSequencialItem;
	}

	@XmlTransient
	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setCodigoFabricante(String codigoFabricante) {
		this.codigoFabricante = codigoFabricante;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setNumeroDrawback(String numeroDrawback) {
		this.numeroDrawback = numeroDrawback;
	}

	public void setNumeroSequencialItem(String numeroSequencialItem) {
		this.numeroSequencialItem = numeroSequencialItem;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
}
