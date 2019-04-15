package br.com.svr.service.nfe;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.svr.service.validacao.InformacaoValidavel;

@InformacaoValidavel
@XmlType(propOrder = { "chaveAcessoReferenciada", "identificacaoNFeReferenciada" })
public class NFeReferenciada {
	@InformacaoValidavel(padrao = { "\\d{44}" }, padraoExemplo = "44 digitos", substituicao = { "\\D", "" }, nomeExibicao = "Chave acesso  da NFe Referenciada")
	@XmlElement(name = "refNFe")
	private String chaveAcessoReferenciada;

	@XmlElement(name = "refNF")
	@InformacaoValidavel(cascata = true, nomeExibicao = "Identifica��o da NFe referenciada")
	private IdentificacaoNFeReferenciada identificacaoNFeReferenciada;

	@XmlTransient
	public String getChaveAcessoReferenciada() {
		return chaveAcessoReferenciada;
	}

	@XmlTransient
	public IdentificacaoNFeReferenciada getIdentificacaoNFeReferenciada() {
		return identificacaoNFeReferenciada;
	}

	public void setChaveAcessoReferenciada(String chaveAcessoReferenciada) {
		this.chaveAcessoReferenciada = chaveAcessoReferenciada;
	}

	public void setIdentificacaoNFeReferenciada(IdentificacaoNFeReferenciada identificacaoNFeReferenciada) {
		this.identificacaoNFeReferenciada = identificacaoNFeReferenciada;
	}
}
