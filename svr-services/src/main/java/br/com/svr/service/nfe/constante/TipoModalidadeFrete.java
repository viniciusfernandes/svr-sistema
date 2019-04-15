package br.com.svr.service.nfe.constante;

public enum TipoModalidadeFrete {
	EMITENTE("0 - Por conta do emitente", "0"), 
	DESTINATARIO_REMETENTE("1 - Por conta do destinat�rio/remetente", "1"), 
	TERCEIROS("2 - Por conta de terceiros", "2"), 
	SEM_FRETE("9 - Sem frete", "9");
	
	private String codigo;
	private String descricao;

	private TipoModalidadeFrete(String descricao, String codigo) {
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
