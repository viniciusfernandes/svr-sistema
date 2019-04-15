package br.com.svr.vendas.json;

import br.com.svr.service.entity.ItemPedido;
import br.com.svr.util.NumeroUtils;

public class ItemPedidoJson {
    private final String aliquotaComissao;
    private final String aliquotaICMS;
    private final String aliquotaIPI;
    private final Double comprimento;
    private final String descricaoItemPedido;
    private final String descricaoPeca;
    private final String formaMaterial;
    private final Integer id;
    private final Integer idMaterial;
    private final Double medidaExterna;
    private final Double medidaInterna;
    private final String ncm;
    private final boolean peca;
    private final String peso;
    private final Integer prazoEntrega;
    private final String precoItem;
    private final String precoMinimo;
    private final String precoUnidade;
    private final String precoUnidadeIPI;
    private final Double precoVenda;
    private final Integer quantidade;
    private final Integer sequencial;
    private final String siglaMaterial;
    private final String tipoCST;
    private final String valorPedido;
    private final String valorPedidoIPI;
    private final String valorTotalPedidoSemFrete;
    private final boolean vendaKilo;

    public ItemPedidoJson(ItemPedido itemPedido) {
        id = itemPedido.getId();
        sequencial = itemPedido.getSequencial();
        comprimento = itemPedido.getComprimento();
        descricaoPeca = itemPedido.getDescricaoPeca();
        formaMaterial = itemPedido.getFormaMaterial() != null ? itemPedido.getFormaMaterial().toString() : "";
        idMaterial = itemPedido.getMaterial() != null ? itemPedido.getMaterial().getId() : -1;
        medidaExterna = itemPedido.getMedidaExterna();
        // Caso a barra seja quadrada ambas as medidas serao iguais
        medidaInterna = itemPedido.isMedidaExternaIgualInterna() ? itemPedido.getMedidaExterna() : itemPedido
                .getMedidaInterna();
        peca = itemPedido.isPeca();
        precoUnidadeIPI = itemPedido.getPrecoUnidadeIPIFormatado();
        precoItem = itemPedido.getPrecoItemFormatado();
        precoVenda = itemPedido.getPrecoVenda();
        precoUnidade = itemPedido.getPrecoUnidadeFormatado();
        quantidade = itemPedido.getQuantidade();
        siglaMaterial = itemPedido.getMaterial() != null ? itemPedido.getMaterial().getSigla() : "";
        vendaKilo = itemPedido.isVendaKilo();
        valorPedido = itemPedido.getValorPedidoFormatado();
        valorPedidoIPI = itemPedido.getValorPedidoIPIFormatado();
        descricaoItemPedido = itemPedido.getDescricao();
        aliquotaICMS = itemPedido.getAliquotaICMSFormatado();
        aliquotaIPI = itemPedido.getAliquotaIPIFormatado();
        precoMinimo = itemPedido.getPrecoMinimoFormatado();
        prazoEntrega = itemPedido.getPrazoEntrega();
        aliquotaComissao = itemPedido.getAliquotaComissaoFormatado();
        ncm = itemPedido.getNcm();
        tipoCST = itemPedido.getTipoCst() != null ? itemPedido.getTipoCst().toString() : "";
        peso = itemPedido.getPeso() == null ? "" : NumeroUtils.arredondarValor2Decimais(itemPedido.getPeso())
                .toString();
        valorTotalPedidoSemFrete = itemPedido.getValorTotalPedidoSemFreteFormatado();
    }

    public String getAliquotaComissao() {
        return aliquotaComissao;
    }

    public String getAliquotaICMS() {
        return aliquotaICMS;
    }

    public String getAliquotaIPI() {
        return aliquotaIPI;
    }

    public Double getComprimento() {
        return comprimento;
    }

    public String getDescricaoItemPedido() {
        return descricaoItemPedido;
    }

    public String getDescricaoPeca() {
        return descricaoPeca;
    }

    public String getFormaMaterial() {
        return formaMaterial;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdMaterial() {
        return idMaterial;
    }

    public Double getMedidaExterna() {
        return medidaExterna;
    }

    public Double getMedidaInterna() {
        return medidaInterna;
    }

    public String getNcm() {
        return ncm;
    }

    public String getPeso() {
        return peso;
    }

    public Integer getPrazoEntrega() {
        return prazoEntrega;
    }

    public String getPrecoItem() {
        return precoItem;
    }

    public String getPrecoMinimo() {
        return precoMinimo;
    }

    public String getPrecoUnidade() {
        return precoUnidade;
    }

    public String getPrecoUnidadeIPI() {
        return precoUnidadeIPI;
    }

    public Double getPrecoVenda() {
        return precoVenda;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Integer getSequencial() {
        return sequencial;
    }

    public String getSiglaMaterial() {
        return siglaMaterial;
    }

    public String getTipoCST() {
        return tipoCST;
    }

    public String getValorPedido() {
        return valorPedido;
    }

    public String getValorPedidoIPI() {
        return valorPedidoIPI;
    }

    public String getValorTotalPedidoSemFrete() {
        return valorTotalPedidoSemFrete;
    }

    public boolean isPeca() {
        return peca;
    }

    public boolean isVendaKilo() {
        return vendaKilo;
    }
}
