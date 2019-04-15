package br.com.svr.service.nfe.constante;

public enum TipoTransporteImportacao {
	MARITIMA("01 - Mar�tima", "01"),
	FLUVIAL("02 - Fluvial", "02"),
	LACUSTRE("03 - Lacustre", "03"),
	AEREA("04 - A�rea", "04"),
	POSTAL("05 - PostaL", "05"),
	FERROVIARIA("06 - Ferrovi�ria", "06"),
	RODOVIARIA("07 - Rodovi�ria", "07"),
	CONDUTO_REDE_TRANSMICAO("08 - Conduto/Rede Transmiss�o", "08"),
	MEIO_PROPRIOS("09 - Meios pr�prios", "09"),
	ENTRADA_SAIDA_FICTA("02 - Entrada/Sa�da Ficta", "10"),
	COURIER("11 - Courier", "11"),
	HANDCARRY("12 - Handcarry", "12");
	
	private final String codigo;
	private final String descricao;

	private TipoTransporteImportacao(String descricao, String codigo) {
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
