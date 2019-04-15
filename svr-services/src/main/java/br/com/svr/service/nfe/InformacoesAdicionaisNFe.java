package br.com.svr.service.nfe;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.svr.service.validacao.InformacaoValidavel;

@InformacaoValidavel
public class InformacoesAdicionaisNFe {
	@InformacaoValidavel(intervaloComprimento = { 1, 2000 }, nomeExibicao = "Informa��es adicionais de interesse do fisco")
	@XmlElement(name = "infAdFisco")
	private String informacoesAdicionaisInteresseFisco;

	@InformacaoValidavel(intervaloComprimento = { 1, 5000 }, nomeExibicao = "Informa��es adicionais de interesse do contribuinte")
	@XmlElement(name = "infCpl")
	private String informacoesComplementaresInteresseContribuinte;

	@XmlTransient
	public String getInformacoesAdicionaisInteresseFisco() {
		return informacoesAdicionaisInteresseFisco;
	}

	@XmlTransient
	public String getInformacoesComplementaresInteresseContribuinte() {
		return informacoesComplementaresInteresseContribuinte;
	}

	public void setInformacoesAdicionaisInteresseFisco(
			String informacoesAdicionaisInteresseFisco) {
		this.informacoesAdicionaisInteresseFisco = informacoesAdicionaisInteresseFisco;
	}

	public void setInformacoesComplementaresInteresseContribuinte(
			String informacoesComplementaresInteresseContribuinte) {
		this.informacoesComplementaresInteresseContribuinte = informacoesComplementaresInteresseContribuinte;
	}
}
