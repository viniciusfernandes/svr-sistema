package br.com.svr.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.svr.service.ContatoService;
import br.com.svr.service.dao.ContatoDAO;
import br.com.svr.service.entity.Contato;
import br.com.svr.service.impl.util.QueryUtil;

@Stateless
public class ContatoServiceImpl implements ContatoService {
	private ContatoDAO contatoDAO;

	@PersistenceContext(unitName = "svr")
	private EntityManager entityManager;

	@PostConstruct
	public void init() {
		contatoDAO = new ContatoDAO(entityManager);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer inserir(Contato contato) {
		return contatoDAO.alterar(contato).getId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<? extends Contato> pesquisar(Integer id, Class<? extends Contato> classe) {
		String nome = classe.getSimpleName();
		return this.entityManager
				.createQuery(
						"select c from " + nome + " c inner join c." + nome.replace("Contato", "").toLowerCase()
								+ " x where x.id = :id ").setParameter("id", id).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends Contato> List<T> pesquisarAusentes(Integer id, Collection<T> listaContato, Class<T> classe) {
		String nomeTipoContato = classe.getSimpleName();
		StringBuilder select = new StringBuilder();
		select.append("select c from ").append(nomeTipoContato).append(" c inner join c.")
				.append(nomeTipoContato.replace("Contato", "").toLowerCase()).append(" l where l.id =:id ");

		if (listaContato != null && !listaContato.isEmpty()) {
			select.append(" and c not in (:listaContato) ");
		}

		final Query query = this.entityManager.createQuery(select.toString()).setParameter("id", id);

		if (listaContato != null && !listaContato.isEmpty()) {
			query.setParameter("listaContato", listaContato);
		}
		return query.getResultList();
	}

	@Override
	public Contato pesquisarById(Integer idContato) {
		return QueryUtil.gerarRegistroUnico(
				this.entityManager.createQuery("select c from Contato c where c.id = :idContato ").setParameter(
						"idContato", idContato), Contato.class, null);
	}

	@Override
	public <T extends Contato> T pesquisarById(Integer idContato, Class<T> classe) {
		String nomeTipoLogradouro = classe.getSimpleName();
		StringBuilder select = new StringBuilder();
		select.append("select c from ").append(nomeTipoLogradouro).append(" c where c.id = :idContato ");
		return QueryUtil.gerarRegistroUnico(
				this.entityManager.createQuery(select.toString()).setParameter("idContato", idContato), classe, null);
	}

	@Override
	public void remover(Integer idContato) {
		final Contato contato = this.pesquisarById(idContato);
		if (contato != null) {
			this.entityManager.remove(contato);
		}
	}

	@Override
	public <T extends Contato> void remover(Integer idContato, Class<T> classe) {
		this.entityManager.remove(this.pesquisarById(idContato, classe));
	}
}
