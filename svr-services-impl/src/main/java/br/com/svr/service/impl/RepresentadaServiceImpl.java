package br.com.svr.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.svr.service.ContatoService;
import br.com.svr.service.LogradouroService;
import br.com.svr.service.RepresentadaService;
import br.com.svr.service.constante.TipoApresentacaoIPI;
import br.com.svr.service.constante.TipoPedido;
import br.com.svr.service.constante.TipoRelacionamento;
import br.com.svr.service.dao.RepresentadaDAO;
import br.com.svr.service.entity.ComentarioRepresentada;
import br.com.svr.service.entity.ContatoRepresentada;
import br.com.svr.service.entity.LogradouroRepresentada;
import br.com.svr.service.entity.Representada;
import br.com.svr.service.entity.Usuario;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.impl.util.QueryUtil;
import br.com.svr.service.validacao.ValidadorInformacao;
import br.com.svr.util.StringUtils;

@Stateless
public class RepresentadaServiceImpl implements RepresentadaService {

	@EJB
	private ContatoService contatoService;

	@PersistenceContext(unitName = "svr")
	private EntityManager entityManager;

	@EJB
	private LogradouroService logradouroService;

	private RepresentadaDAO representadaDAO;

	@Override
	public Integer desativar(Integer id) {
		Query query = this.entityManager.createQuery("update Representada r set r.ativo = false where r.id = :id");
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	private Query gerarQueryPesquisa(Representada filtro, StringBuilder select) {
		Query query = this.entityManager.createQuery(select.toString());

		if (StringUtils.isNotEmpty(filtro.getNomeFantasia())) {
			query.setParameter("nomeFantasia", "%" + filtro.getNomeFantasia() + "%");
		}

		if (StringUtils.isNotEmpty(filtro.getRazaoSocial())) {
			query.setParameter("razaoSocial", "%" + filtro.getRazaoSocial() + "%");
		}

		if (StringUtils.isNotEmpty(filtro.getCnpj())) {
			query.setParameter("cnpj", "%" + filtro.getCnpj() + "%");
		}
		return query;
	}

	private void gerarRestricaoPesquisa(Representada filtro, Boolean apenasAtivos, StringBuilder select) {
		StringBuilder restricao = new StringBuilder();

		if (StringUtils.isNotEmpty(filtro.getNomeFantasia())) {
			restricao.append("t.nomeFantasia LIKE :nomeFantasia AND ");
		}

		if (StringUtils.isNotEmpty(filtro.getRazaoSocial())) {
			restricao.append("t.razaoSocial LIKE :razaoSocial AND ");
		}

		if (StringUtils.isNotEmpty(filtro.getCnpj())) {
			restricao.append("t.cnpj LIKE :cnpj AND ");
		}

		if (apenasAtivos != null && Boolean.TRUE.equals(apenasAtivos)) {
			restricao.append("t.ativo = true AND ");
		} else if (apenasAtivos != null && Boolean.FALSE.equals(apenasAtivos)) {
			restricao.append("t.ativo = false AND ");
		}

		if (restricao.length() > 0) {
			select.append(" WHERE ");
			select.append(restricao);
			select.delete(select.lastIndexOf("AND"), select.length() - 1);
		}
	}

	@PostConstruct
	public void init() {
		this.representadaDAO = new RepresentadaDAO(this.entityManager);
	}

	@Override
	public Integer inserir(final Representada representada) throws BusinessException {
		ValidadorInformacao.validar(representada);

		if (isNomeFantasiaExistente(representada.getId(), representada.getNomeFantasia())) {
			throw new BusinessException("O nome fantasia enviado ja foi cadastrado para outra representada");
		}

		if (isCNPJExistente(representada.getId(), representada.getCnpj())) {
			throw new BusinessException("CNPJ enviado ja foi cadastrado para outra representada");
		}

		if (representada.isRevendedor() && isRevendedorExistente(representada.getId())) {
			Representada revendedor = pesquisarRevendedor();
			throw new BusinessException("J� existe um revendedor cadastrado no sistema. Remova o revendedor \""
					+ revendedor.getNomeFantasia() + "\" para em seguida cadastrar um outro.");
		}

		if (representada.isRepresentda() && representada.getComissao() == 0) {
			throw new BusinessException("A comiss�o � obrigatorio no cadastro da representada");
		}

		representada.setLogradouro(logradouroService.inserir(representada.getLogradouro()));

		if (representada.getId() == null) {
			return representadaDAO.inserir(representada).getId();
		} else {
			return representadaDAO.alterar(representada).getId();
		}
	}

	@Override
	public void inserirComentario(Integer idProprietario, Integer idRepresentada, String comentario)
			throws BusinessException {
		Representada representada = new Representada();
		representada.setId(idRepresentada);
		Usuario usuario = new Usuario(idProprietario);

		ComentarioRepresentada comentarioRepresentada = new ComentarioRepresentada();
		comentarioRepresentada.setConteudo(comentario);
		comentarioRepresentada.setDataInclusao(new Date());
		comentarioRepresentada.setRepresentada(representada);
		comentarioRepresentada.setUsuario(usuario);

		ValidadorInformacao.validar(comentarioRepresentada);
		entityManager.persist(comentarioRepresentada);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Boolean isCalculoIPIHabilitado(Integer idRepresentada) {
		return !TipoApresentacaoIPI.NUNCA.equals(representadaDAO.pesquisarTipoApresentacaoIPI(idRepresentada));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean isCNPJExistente(Integer id, String cnpj) {
		return this.representadaDAO.isEntidadeExistente(Representada.class, id, "cnpj", cnpj);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean isNomeFantasiaExistente(Integer id, String nomeFantasia) {
		return this.representadaDAO.isEntidadeExistente(Representada.class, id, "nomeFantasia", nomeFantasia);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean isRevendedor(Integer idRepresentada) {
		return TipoRelacionamento.REVENDA.equals(representadaDAO.pesquisarTipoRelacionamento(idRepresentada));
	}

	private boolean isRevendedorExistente(Integer id) {
		return this.representadaDAO.isEntidadeExistente(Representada.class, id, "tipoRelacionamento",
				TipoRelacionamento.REVENDA);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public double pesquisarAliquotaICMSRevendedor() {
		return representadaDAO.pesquisarAliquotaICMSRevendedor();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@SuppressWarnings("unchecked")
	public List<Representada> pesquisarBy(Representada filtro, Boolean apenasAtivos, Integer indiceRegistroInicial,
			Integer numeroMaximoRegistros) {
		if (filtro == null) {
			return Collections.emptyList();
		}

		StringBuilder select = new StringBuilder("SELECT t FROM Representada t ");
		this.gerarRestricaoPesquisa(filtro, apenasAtivos, select);
		select.append(" order by t.nomeFantasia ");

		Query query = this.gerarQueryPesquisa(filtro, select);

		if (indiceRegistroInicial != null && indiceRegistroInicial >= 0 && numeroMaximoRegistros != null
				&& numeroMaximoRegistros >= 0) {
			query.setFirstResult(indiceRegistroInicial).setMaxResults(numeroMaximoRegistros);
		}
		return query.getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Representada pesquisarById(Integer id) {
		return representadaDAO.pesquisarById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Representada> pesquisarById(List<Integer> listaIdRepresentada) {
		return this.entityManager.createQuery("select m from Representada m where m.id in (:listaIdRepresentada) ")
				.setParameter("listaIdRepresentada", listaIdRepresentada).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ComentarioRepresentada> pesquisarComentarioByIdRepresentada(Integer idRepresentada) {
		return (List<ComentarioRepresentada>) entityManager
				.createQuery(
						"select new ComentarioRepresentada (c.dataInclusao, c.conteudo, v.nome, v.sobrenome) from ComentarioRepresentada c "
								+ " inner join c.usuario v where c.representada.id = :idRepresentada order by c.dataInclusao desc")
				.setParameter("idRepresentada", idRepresentada).getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public double pesquisarComissaoRepresentada(Integer idRepresentada) {
		return representadaDAO.pesquisarComissao(idRepresentada);
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ContatoRepresentada> pesquisarContato(Integer id) {
		return (List<ContatoRepresentada>) this.contatoService.pesquisar(id, ContatoRepresentada.class);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Representada> pesquisarFornecedor(Boolean ativo) {
		return representadaDAO.pesquisarRepresentadaByTipoRelacionamento(ativo, TipoRelacionamento.FORNECIMENTO,
				TipoRelacionamento.REPRESENTACAO_FORNECIMENTO);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Representada> pesquisarFornecedorAtivo() {
		return pesquisarFornecedor(true);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Representada> pesquisarFornecedorAtivoByNomeFantasia(String nomeFantasia) {
		return representadaDAO.pesquisarRepresentadaByTipoRelacionamento(nomeFantasia, true,
				TipoRelacionamento.FORNECIMENTO, TipoRelacionamento.REPRESENTACAO_FORNECIMENTO);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Integer pesquisarIdRevendedor() {
		Representada r = representadaDAO.pesquisarRevendedor();
		return r == null ? null : r.getId();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public LogradouroRepresentada pesquisarLogradorouro(Integer id) {
		return representadaDAO.pesquisarLogradorouro(id);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String pesquisarNomeFantasiaById(Integer idRepresentada) {
		return representadaDAO.pesquisarNomeFantasiaById(idRepresentada);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Representada> pesquisarRepresentada(Boolean ativo) {
		return representadaDAO.pesquisarRepresentadaExcluindoRelacionamento(ativo, TipoRelacionamento.FORNECIMENTO);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Representada> pesquisarRepresentadaAtiva() {
		return this.pesquisarRepresentada(true);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Representada> pesquisarRepresentadaAtivoByTipoPedido(TipoPedido tipoPedido) {
		if (TipoPedido.COMPRA.equals(tipoPedido)) {
			return pesquisarFornecedorAtivo();
		}
		return pesquisarRepresentadaAtiva();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Representada> pesquisarRepresentadaEFornecedor() {
		return representadaDAO.pesquisarRepresentadaEFornecedor();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Representada pesquisarRevendedor() {
		return representadaDAO.pesquisarRevendedor();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public TipoApresentacaoIPI pesquisarTipoApresentacaoIPI(Integer idRepresentada) {
		Representada representada = pesquisarById(idRepresentada);
		return representada != null ? representada.getTipoApresentacaoIPI() : null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public TipoRelacionamento pesquisarTipoRelacionamento(Integer idRepresentada) {
		return representadaDAO.pesquisarTipoRelacionamento(idRepresentada);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Long pesquisarTotalRegistros(Representada filtro, Boolean apenasAtivos) {
		if (filtro == null) {
			return 0L;
		}

		final StringBuilder select = new StringBuilder("SELECT count(t.id) FROM Representada t ");
		this.gerarRestricaoPesquisa(filtro, apenasAtivos, select);
		Query query = this.gerarQueryPesquisa(filtro, select);
		return QueryUtil.gerarRegistroUnico(query, Long.class, null);
	}

}
