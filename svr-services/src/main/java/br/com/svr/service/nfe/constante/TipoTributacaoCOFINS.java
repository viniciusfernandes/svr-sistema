package br.com.svr.service.nfe.constante;

public enum TipoTributacaoCOFINS {
	COFINS_ST("ST - Oper. Tribut�vel por substitui��o tribut�ria em percentual", "ST"), 
	COFINS_1("01 - Oper. Tribut�vel BC = valor opera��o al�quota normal", "01"), 
	COFINS_2("02 - Oper. Tribut�vel BC = valor opera��o al�quota diferenciada", "02"), 
	COFINS_3("03 - Oper. Tribut�vel BC = qtde vendida X al�quota por unid. produto", "03"), 
	COFINS_4("04 - Oper. Tribut�vel com tributa��o monof�sica al�quota zero", "04"),
	COFINS_5("05 - Oper. Tribut�vel por substitui��o tribut�ria", "05"), 
	COFINS_6("06 - Oper. Tribut�vel al�quota zero", "06"), 
	COFINS_7("07 - Oper. isenta contribui��o", "07"), 
	COFINS_8("08 - Oper. sem incid�ncia contribui��o", "08"), 
	COFINS_9("09 - Oper. com suspens�o contribui��o", "09"), 
	COFINS_49("49 - Outras oper. sa�da", "49"),
	COFINS_50("50 - Oper. com Direito a Cr�dito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno", "50"),
	COFINS_51("51 - Oper. com Direito a Cr�dito - Vinculada Exclusivamente a Receita N�o Tributada no Mercado Interno", "51"),
	COFINS_52("52 - Oper. com Direito a Cr�dito � Vinculada Exclusivamente a Receita de Exporta��o", "52"),
	COFINS_53("53 - Oper. com Direito a Cr�dito - Vinculada a Receitas Tributadas e N�o-Tributadas no Mercado Interno", "53"),
	COFINS_54("54 - Oper. com Direito a Cr�dito - Vinculada a Receitas Tributadas no Mercado Interno e de Exporta��o", "54"),
	COFINS_55("55 - Oper. com Direito a Cr�dito - Vinculada a Receitas N�o-Tributadas no Mercado Interno e de Exporta��o", "55"),
	COFINS_56("56 - Oper. com Direito a Cr�dito - Vinculada a Receitas Tributadas e N�o-Tributadas no Mercado Interno, e de Exporta��o", "56"),
	COFINS_60("60 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada Exclusivamente a Receita Tributada no Mercado Interno", "60"),
	COFINS_61("61 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada Exclusivamente a Receita N�o-Tributada no Mercado Interno", "61"),
	COFINS_62("62 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada Exclusivamente a Receita de Exporta��o", "62"),
	COFINS_63("63 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada a Receitas Tributadas e N�o-Tributadas no Mercado Interno", "63"),
	COFINS_64("64 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada a Receitas Tributadas no Mercado Interno e de Exporta��o", "64"),
	COFINS_65("65 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada a Receitas N�o-Tributadas no Mercado Interno e de Exporta��o", "65"),
	COFINS_66("66 - Cr�dito Presumido - Opera��o de Aquisi��o Vinculada a Receitas Tributadas e N�o-Tributadas no Mercado Interno, e de Exporta��o", "66"),
	COFINS_67("67 - Cr�dito Presumido - Outras Opera��es", "67"),
	COFINS_70("70 - Opera��o de Aquisi��o sem Direito a Cr�dito", "70"),
	COFINS_71("71 - Opera��o de Aquisi��o com Isen��o", "71"),
	COFINS_72("72 - Opera��o de Aquisi��o com Suspens�o", "72"),
	COFINS_73("73 - Opera��o de Aquisi��o a Al�quota Zero", "73"),
	COFINS_74("74 - Opera��o de Aquisi��o; sem Incid�ncia da Contribui��o;", "74"),
	COFINS_75("75 - Opera��o de Aquisi��o por Substitui��o Tribut�ria", "75"),
	COFINS_98("98 - Outras Opera��es de Entrada", "98"),
	COFINS_99("99 - Outros", "99");

	public static TipoTributacaoCOFINS getTipoTributacao(String codigo) {
		for (TipoTributacaoCOFINS t : values()) {
			if (t.codigo.equals(codigo)) {
				return t;
			}
		}
		return null;
	}

	private String codigo;

	private String descricao;

	private TipoTributacaoCOFINS(String descricao, String codigo) {
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
