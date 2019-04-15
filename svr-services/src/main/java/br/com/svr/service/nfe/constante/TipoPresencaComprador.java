package br.com.svr.service.nfe.constante;

public enum TipoPresencaComprador {
	NAO_APLICA("0 - N�o se aplica", "0"),
	PRESENCIAL("1 - Presencial", "1"),
	NAO_PRESENCIAL_INTERNET("2 - N�o presencial pela internet", "2"),
	NAO_PRESENCIAL_TELEATENDIMENTO("3 - N�o presencial por teleatendimento", "3"),
	ENTREGA_DOMICILIO("4 - Entrega em domic�lio", "4"), 
	NAO_PRESENCIAL_OUTROS("9 - N�o presencial - outros", "9");
	
	private final String codigo;
	private final String descricao;

	private TipoPresencaComprador(String descricao, String codigo) {
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
