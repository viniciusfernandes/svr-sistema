package br.com.svr.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import br.com.svr.service.entity.Contato;

@Local
public interface ContatoService {

    Integer inserir(Contato contato);

    List<? extends Contato> pesquisar(Integer id, Class<? extends Contato> classe);

    <T extends Contato> List<T> pesquisarAusentes(Integer id, Collection<T> listaContato, Class<T> classe);

    Contato pesquisarById(Integer idContato);

    <T extends Contato> T pesquisarById(Integer idContato, Class<T> classe);

    void remover(Integer idContato);

	<T extends Contato> void remover(Integer idContato, Class<T> classe);

}
