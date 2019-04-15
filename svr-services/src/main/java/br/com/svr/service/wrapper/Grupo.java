package br.com.svr.service.wrapper;

import java.util.ArrayList;
import java.util.List;

import br.com.svr.util.NumeroUtils;

class Grupo {

	protected final String nome;
	protected final Double valor;
	protected final String valorFormatado;
	protected List<Grupo> listaSubgrupo;

	Grupo(String nome) {
		this(nome, 0d);
	}

	Grupo(String nome, Double valor) {
		this.nome = nome;
		this.valor = valor;
		this.valorFormatado = NumeroUtils.formatarValor2Decimais(valor);
	}

	void addSubgrupo(Grupo grupo) {
		if (this.listaSubgrupo == null) {
			this.listaSubgrupo = new ArrayList<Grupo>();
		}
		this.listaSubgrupo.add(grupo);
	}

	List<Grupo> getListaSubgrupo() {
		return listaSubgrupo;
	}

	public String getNome() {
		return nome;
	}

	int getNumeroSubgrupo() {
		return listaSubgrupo != null ? listaSubgrupo.size() : 0;
	}

	public Double getValor() {
		return valor;
	}

	String getValorFormatado() {
		return valorFormatado;
	}

	public Double getValorTotal() {
		if (this.listaSubgrupo == null || this.listaSubgrupo.isEmpty()) {
			return 0d;
		}
		
		Double t = 0d;
		for (Grupo g : this.listaSubgrupo) {
			t += g.valor;
		}

		return t;
	}

	String getValorTotalFormatado() {
		return NumeroUtils.formatarValor2Decimais(this.getValorTotal());
	}
	
	void limparListaSubgrupo() {
		if (listaSubgrupo != null) {
			listaSubgrupo.clear();
		}
		;
	}
}
