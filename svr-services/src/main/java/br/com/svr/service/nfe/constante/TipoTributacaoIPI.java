package br.com.svr.service.nfe.constante;

public enum TipoTributacaoIPI {
	IPI_00("00 - Entrada com recupera��o de cr�dito", "00"), 
	IPI_01("01 - Entrada tributada com al�quota zero", "01"), 
	IPI_02("02 - Entrada isenta", "02"), 
	IPI_03("03 - Entrada n�o-tributada", "03"), 
	IPI_04("04 - Entrada imune", "04"), 
	IPI_05("05 - Entrada com suspens�o", "05"), 
	IPI_49("49 - Outras entradas", "49"), 
	IPI_50("50 - Sa�da tributada", "50"), 
	IPI_51("51 - Sa�da tributada com al�quota zero", "51"), 
	IPI_52("52 - Sa�da isenta", "52"), 
	IPI_53("53 - Sa�da n�o-tributada", "53"), 
	IPI_54("54 - Sa�da imune", "54"), 
	IPI_55("55 - Sa�da com suspens�o", "55"), 
	IPI_99("99 - Outras sa�das", "99");
	
	public static TipoTributacaoIPI getTipoTributacao(String codigo) {
		for (TipoTributacaoIPI t : values()) {
			if (t.codigo.equals(codigo)) {
				return t;
			}
		}
		return null;
	}
	
	private String codigo;

	private String descricao;
	
	private TipoTributacaoIPI(String descricao, String codigo) {
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
