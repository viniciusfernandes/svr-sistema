package br.com.svr.service.nfe;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.svr.service.validacao.InformacaoValidavel;

@XmlRootElement(name = "nfe")
@InformacaoValidavel
public class NFe {
	@InformacaoValidavel(obrigatorio = true, cascata = true, nomeExibicao = "Dados da NFe")
	@XmlElement(name = "infNFe")
	private DadosNFe dadosNFe;

	public NFe() {
	}

	public NFe(DadosNFe dadosNFe) {
		this.dadosNFe = dadosNFe;
	}

	@XmlTransient
	public DadosNFe getDadosNFe() {
		return dadosNFe;
	}

	public void setDadosNFe(DadosNFe dadosNFe) {
		this.dadosNFe = dadosNFe;
	}
}
