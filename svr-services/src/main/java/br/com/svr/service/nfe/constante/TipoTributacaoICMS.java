package br.com.svr.service.nfe.constante;

public enum TipoTributacaoICMS {
	ICMS_00("00 - Tributada integralmente", "00"), 
	ICMS_10("10 - Tributada  com cobran�a por substitui��o tribut�ria", "10"), 
	ICMS_20("20 - Tributada com redu��o da base de c�lculo", "20"), 
	ICMS_30("30 - Isenta ou n�o tributada com cobran�a por substitui��o tribut�ria", "30"), 
	ICMS_40("40 - Isenta", "40"), 
	ICMS_41("41 - N�o tributada", "41"), 
	ICMS_50("50 - Suspens�o", "50"), 
	ICMS_51("51 - Deferimento", "51"), 
	ICMS_60("60 - Cobrado anteriormente por substitui��o tribut�ria", "60"), 
	ICMS_70("70 - Com Redu��o da base de c�lculo e cobran�a por substitui��o tribut�ria", "70"), 
	ICMS_90("90 - Outros", "90"),
	ICMS_PART("PART- Opera��o interestadual - Partilha entre UF origem e UF destino", "PART");
	
	public static TipoTributacaoICMS getTipoTributacao(String codigo) {
		for (TipoTributacaoICMS t : values()) {
			if (t.codigo.equals(codigo)) {
				return t;
			}
		}
		return null;
	}
	
	private String codigo;
	private String descricao;

	private TipoTributacaoICMS(String descricao, String codigo) {
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
