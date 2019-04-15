package br.com.svr.service.nfe.constante;

public enum TipoFormaPagamento {
	VISTA("0 - � vista", "0"),
	PRAZO("1 - � prazo", "1"),
	OUTROS("2 - Outros", "2") ;
	private final String codigo;
	private final String descricao;

	private TipoFormaPagamento(String descricao, String codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

}
