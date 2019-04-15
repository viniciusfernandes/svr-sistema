package br.com.svr.service.constante;

public enum TipoFinalidadePedido {
    INDUSTRIALIZACAO("INDUSTRIALIZA��O"), 
    CONSUMO("CONSUMO"), 
    REVENDA("REVENDA"),
    NOTA_ENTRADA("NOTA DE ENTRADA"),
    RETORNO("RETORNO"),
    DEVOLUCAO("DEVOLU��O"),
    AMOSTRA_GRATIS("AMOSTRA GR�TIS"),
    REMESSA_INDUSTRIALIZACAO("REMESSA P/ INDUSTRIALIZA��O"),
    REMESSA_CONSERTO("REMESSA P/ CONSERTO"),
    REMESSA_ANALISE("REMESSA P/ AN�LISE"),
    SIMPLES_REMESSA("SIMPLES REMESSA"),
    OUTRA_ENTRADA("OUTRA ENTRADA"),
    PRIMEIRA_NOTA_TRIANGULAR("PRIMEIRA NOTA TRIANGULAR");

    
    private String descricao;

    private TipoFinalidadePedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
