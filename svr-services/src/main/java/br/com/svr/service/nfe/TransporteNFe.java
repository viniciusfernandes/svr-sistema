package br.com.svr.service.nfe;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.svr.service.validacao.InformacaoValidavel;

@InformacaoValidavel
@XmlType(propOrder = { "modalidadeFrete", "transportadoraNFe", "retencaoICMS", "veiculo", "listaReboque", "listaVolume" })
public class TransporteNFe {
	@InformacaoValidavel(iteravel = true, nomeExibicao = "Reboque")
	@XmlElement(name = "reboque")
	private List<VeiculoTransporte> listaReboque;

	@InformacaoValidavel(iteravel = true, nomeExibicao = "Volumes transportados")
	@XmlElement(name = "vol")
	private List<VolumeTransportado> listaVolume;

	@InformacaoValidavel(obrigatorio = true, opcoes = { "0", "1", "2", "9" }, nomeExibicao = "Modalidade do frete da transporte")
	@XmlElement(name = "modFrete")
	private String modalidadeFrete;

	@XmlElement(name = "retTransp")
	@InformacaoValidavel(cascata = true, nomeExibicao = "Reten��o do ICMS do transporte")
	private RetencaoICMSTransporteNFe retencaoICMS;

	@InformacaoValidavel(cascata = true, nomeExibicao = "Transportadora da NFe")
	@XmlElement(name = "transporta")
	private TransportadoraNFe transportadoraNFe;

	@InformacaoValidavel(cascata = true, nomeExibicao = "Ve�culo de transporte")
	@XmlElement(name = "veicTransp")
	private VeiculoTransporte veiculo;

	@XmlTransient
	public List<VeiculoTransporte> getListaReboque() {
		return listaReboque;
	}

	@XmlTransient
	public List<VolumeTransportado> getListaVolume() {
		return listaVolume;
	}

	@XmlTransient
	public String getModalidadeFrete() {
		return modalidadeFrete;
	}

	@XmlTransient
	public RetencaoICMSTransporteNFe getRetencaoICMS() {
		return retencaoICMS;
	}

	@XmlTransient
	public TransportadoraNFe getTransportadoraNFe() {
		return transportadoraNFe;
	}

	@XmlTransient
	public VeiculoTransporte getVeiculo() {
		return veiculo;
	}

	public void setListaReboque(List<VeiculoTransporte> listaReboque) {
		this.listaReboque = listaReboque;
	}

	public void setListaVolume(List<VolumeTransportado> listaVolume) {
		this.listaVolume = listaVolume;
	}

	public void setModalidadeFrete(String modalidadeFrete) {
		this.modalidadeFrete = modalidadeFrete;
	}

	public void setRetencaoICMS(RetencaoICMSTransporteNFe retencaoICMS) {
		this.retencaoICMS = retencaoICMS;
	}

	public void setTransportadoraNFe(TransportadoraNFe transportadoraNFe) {
		this.transportadoraNFe = transportadoraNFe;
	}

	public void setVeiculo(VeiculoTransporte veiculo) {
		this.veiculo = veiculo;
	}

}
