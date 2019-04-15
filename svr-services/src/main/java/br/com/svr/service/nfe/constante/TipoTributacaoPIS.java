package br.com.svr.service.nfe.constante;

public enum TipoTributacaoPIS {
	PIS_ST("ST - Oper. Tribut�vel por substitui��o", "ST"), 
	PIS_1("01 - Oper. Tribut�vel BC = valor opera��o al�quota normal", "01"), 
	PIS_2("02 - Oper. Tribut�vel BC = valor opera��o al�quota diferenciada", "02"),
	PIS_3("03 - Oper. Tribut�vel BC = qtde vendida X al�quota por unid. produto", "03"),
	PIS_4("04 - Oper. Tribut�vel com tributa��o monof�sica al�quota zero", "04"),
	PIS_5("05 - Oper. Tribut�vel com substitui��o tribut�ria", "05"),
	PIS_6("06 - Oper. Tribut�vel al�quota zero", "06"),
	PIS_7("07 - Oper. isenta contribui��o", "07"),
	PIS_8("08 - Oper. sem incid�ncia contribui��o", "08"),
	PIS_9("09 - Oper. com suspens�o contribui��o", "09"),
	PIS_49("49 - Outras oper. sa�da", "49"),
	PIS_50("50 - Oper. com Direito a Cr�dito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno", "50"),
	PIS_51("51 - Oper. com Direito a Cr�dito - Vinculada Exclusivamente a Receita N�o Tributada no Mercado Interno", "51"),
	PIS_52("52 - Oper. com Direito a Cr�dito � Vinculada Exclusivamente a Receita de Exporta��o", "52"),
	PIS_53("53 - Oper. com Direito a Cr�dito - Vinculada a Receitas Tributadas e N�o-Tributadas no Mercado Interno", "53"),
	PIS_54("54 - Oper. com Direito a Cr�dito - Vinculada a Receitas Tributadas no Mercado Interno e de Exporta��o", "54"),
	PIS_55("55 - Oper. com Direito a Cr�dito - Vinculada a Receitas N�o-Tributadas no Mercado Interno e de Exporta��o", "55"),
	PIS_56("56 - Oper. com Direito a Cr�dito - Vinculada a Receitas Tributadas e N�o-Tributadas no Mercado Interno, e de Exporta��o", "56"),
	PIS_60("60 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada Exclusivamente a Receita Tributada no Mercado Interno", "60"),
	PIS_61("61 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada Exclusivamente a Receita N�o-Tributada no Mercado Interno", "61"),
	PIS_62("62 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada Exclusivamente a Receita de Exporta��o", "62"),
	PIS_63("63 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada a Receitas Tributadas e N�o-Tributadas no Mercado Interno", "63"),
	PIS_64("64 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada a Receitas Tributadas no Mercado Interno e de Exporta��o", "64"),
	PIS_65("65 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada a Receitas N�o-Tributadas no Mercado Interno e de Exporta��o", "65"),
	PIS_66("66 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada a Receitas Tributadas e N�o-Tributadas no Mercado Interno, e de Exporta��o", "66"),
	PIS_67("67 - Cr�dito Presumido - Outras Opera��es", "67"),
	PIS_70("70 - Opera��o de Aquisi��o sem Direito a Cr�dito", "70"),
	PIS_71("71 - Opera��o de Aquisi��o com Isen��o", "71"),
	PIS_72("72 - Opera��o de Aquisi��o com Suspens�o", "72"),
	PIS_73("73 - Opera��o de Aquisi��o a Al�quota Zero", "73"),
	PIS_74("74 - Opera��o de Aquisi��o; sem Incid�ncia da Contribui��o;", "74"),
	PIS_75("75 - Opera��o de Aquisi��o por Substitui��o Tribut�ria", "75"),
	PIS_98("98 - Outras Opera��es de Entrada", "98"),
	PIS_99("99 - Outros", "99");
	
	public static TipoTributacaoPIS getTipoTributacao(String codigo) {
		for (TipoTributacaoPIS t : values()) {
			if (t.codigo.equals(codigo)) {
				return t;
			}
		}
		return null;
	}
	
	private String codigo;
	private String descricao;

	private TipoTributacaoPIS(String descricao, String codigo) {
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
