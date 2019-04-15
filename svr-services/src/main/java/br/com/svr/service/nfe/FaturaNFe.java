package br.com.svr.service.nfe;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.svr.service.validacao.InformacaoValidavel;

@InformacaoValidavel
@XmlType(propOrder = { "numero", "valorOriginal", "valorDesconto", "valorLiquido" })
public class FaturaNFe {
	@InformacaoValidavel(intervaloComprimento = { 1, 60 }, nomeExibicao = "N�mero da fatura")
	@XmlElement(name = "nFat")
	private String numero;

	@InformacaoValidavel(decimal = { 15, 2 }, nomeExibicao = "Valor desconto da fatura")
	@XmlElement(name = "vDesc")
	private Double valorDesconto;

	@InformacaoValidavel(decimal = { 15, 2 }, nomeExibicao = "Valor l�quido da fatura")
	@XmlElement(name = "vLiq")
	private Double valorLiquido;

	@InformacaoValidavel(decimal = { 15, 2 }, nomeExibicao = "Valor original da fatura")
	@XmlElement(name = "vOrig")
	private Double valorOriginal;

	@XmlTransient
	public String getNumero() {
		return numero;
	}

	@XmlTransient
	public Double getValorDesconto() {
		return valorDesconto;
	}

	@XmlTransient
	public Double getValorLiquido() {
		return valorLiquido;
	}

	@XmlTransient
	public Double getValorOriginal() {
		return valorOriginal;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public void setValorOriginal(Double valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

}
