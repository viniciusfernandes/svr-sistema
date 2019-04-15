package br.com.svr.service.nfe;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.svr.service.validacao.InformacaoValidavel;

@InformacaoValidavel
@XmlType(propOrder = { "logradouro", "numero", "complemento", "bairro", "codigoMunicipio", "nomeMunicipio", "UF",
		"cep", "codigoPais", "nomePais", "telefone" })
public class EnderecoNFe {

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 2, 60 }, nomeExibicao = "Bairro do endere�o")
	@XmlElement(name = "xBairro")
	private String bairro;

	@InformacaoValidavel(tamanho = 8, nomeExibicao = "CEP do endere�o")
	@XmlElement(name = "CEP")
	private String cep;

	@InformacaoValidavel(obrigatorio = true, padrao = "\\d{7}", padraoExemplo = "7 digitos", tamanho = 7, prefixo = "0", nomeExibicao = "C�digo do munic�pio")
	@XmlElement(name = "cMun")
	private String codigoMunicipio;

	@InformacaoValidavel(intervaloNumerico = { 1, 9999 }, nomeExibicao = "Bairro do endere�o")
	@XmlElement(name = "cPais")
	private String codigoPais;

	@InformacaoValidavel(intervaloComprimento = { 0, 60 }, nomeExibicao = "Complemento do endere�o")
	@XmlElement(name = "xCpl")
	private String complemento;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 2, 60 }, nomeExibicao = "Logradouro do endere�o")
	@XmlElement(name = "xLgr")
	private String logradouro;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 2, 60 }, nomeExibicao = "Nome do munic�pio do endere�o")
	@XmlElement(name = "xMun")
	private String nomeMunicipio;

	@InformacaoValidavel(intervaloComprimento = { 1, 60 }, nomeExibicao = "C�digo do pa�s do endere�o")
	@XmlElement(name = "xPais")
	private String nomePais;

	@InformacaoValidavel(obrigatorio = true, intervaloComprimento = { 1, 60 }, nomeExibicao = "N�mero do endere�o")
	@XmlElement(name = "nro")
	private String numero;

	@InformacaoValidavel(substituicao = { "\\D", "" }, padrao = "\\d{6,14}", padraoExemplo = "6 a 14 digitos", nomeExibicao = "telefone do endere�o")
	@XmlElement(name = "fone")
	private String telefone;

	@InformacaoValidavel(obrigatorio = true, tamanho = 2, nomeExibicao = "Sigla da UF do endere�o")
	@XmlElement(name = "UF")
	private String UF;

	@XmlTransient
	public String getBairro() {
		return bairro;
	}

	@XmlTransient
	public String getCep() {
		return cep;
	}

	@XmlTransient
	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}

	@XmlTransient
	public String getCodigoPais() {
		return codigoPais;
	}

	@XmlTransient
	public String getComplemento() {
		return complemento;
	}

	@XmlTransient
	public String getLogradouro() {
		return logradouro;
	}

	@XmlTransient
	public String getNomeMunicipio() {
		return nomeMunicipio;
	}

	@XmlTransient
	public String getNomePais() {
		return nomePais;
	}

	@XmlTransient
	public String getNumero() {
		return numero;
	}

	@XmlTransient
	public String getTelefone() {
		return telefone;
	}

	@XmlTransient
	public String getUF() {
		return UF;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}

	public void setNomePais(String nomePais) {
		this.nomePais = nomePais;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public void setUF(String uF) {
		UF = uF;
	}
}