package br.com.svr.service.nfe.constante;

public enum TipoIntermediacaoImportacao {
	CONTA_PROPRIA("1 - Importa��o por conta pr�pria", "1"), 
	CONTA_PROPRIA_ORDEM("2 - Importa��o por conta pr�pria e ordem", "2"),
	ENCOMENDA("3 - Importa��o por encomenda", "3");	
	private String codigo;
	private String descricao;

	private TipoIntermediacaoImportacao(String descricao, String codigo) {
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
