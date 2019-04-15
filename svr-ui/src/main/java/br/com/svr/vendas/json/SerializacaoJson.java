package br.com.svr.vendas.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SerializacaoJson {
    private final String nome;
    private final Object objeto;
    private final boolean recursivo;
    private List<String> atributoInclusao;
    private List<String> atributoExclusao;

    public SerializacaoJson(Object objeto) {
        this(null, objeto);
    }

    public SerializacaoJson(Object objeto, boolean isRecursivo) {
        this(null, objeto, isRecursivo);
    }

    public SerializacaoJson(String nome, Object objeto) {
        this(nome, objeto, false);
    }

    public SerializacaoJson(String nome, Object objeto, boolean isRecursivo) {
        this.nome = nome;
        this.objeto = objeto;
        this.recursivo = isRecursivo;
    }

    public boolean contemExclusaoAtributo() {
        return atributoExclusao != null && !atributoExclusao.isEmpty();
    }

    public boolean contemInclusaoAtributo() {
        return atributoInclusao != null && !atributoInclusao.isEmpty();
    }

    public boolean contemNome() {
        return this.nome != null;
    }

    public SerializacaoJson excluirAtributo(String... nomeAtributo) {
        if (nomeAtributo == null) {
            return this;
        }

        if (atributoExclusao == null) {
            atributoExclusao = new ArrayList<String>();
        }
        atributoExclusao.addAll(Arrays.asList(nomeAtributo));
        return this;
    }

    public String[] getAtributoExclusao() {
        return atributoExclusao == null ? null : atributoExclusao.toArray(new String[] {});
    }

    public String[] getAtributoInclusao() {
        return atributoInclusao == null ? null : atributoInclusao.toArray(new String[] {});
    }

    public String getNome() {
        return nome;
    }

    public Object getObjeto() {
        return objeto;
    }

    public SerializacaoJson incluirAtributo(String... nomeAtributo) {
        if (nomeAtributo == null) {
            return this;
        }

        if (atributoInclusao == null) {
            atributoInclusao = new ArrayList<String>();
        }
        atributoInclusao.addAll(Arrays.asList(nomeAtributo));
        return this;
    }

    public boolean isRecursivo() {
        return recursivo;
    }

}
