package br.com.svr.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.svr.service.constante.FormaMaterial;
import br.com.svr.service.entity.ItemEstoque;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.impl.anotation.WARNING;
import br.com.svr.service.impl.util.QueryUtil;
import br.com.svr.util.StringUtils;

public class ItemEstoqueDAO extends GenericDAO<ItemEstoque> {
	public ItemEstoqueDAO(EntityManager entityManager) {
		super(entityManager);
	}

	public void alterarPrecoMedioFatorICMS(List<ItemEstoque> listaItem) {
		for (ItemEstoque item : listaItem) {
			entityManager
					.createQuery(
							"update ItemEstoque i set i.precoMedio = :precoMedio, i.precoMedioFatorICMS =:precoMedioFatorICMS where i.id = :idItemEstoque ")

					.setParameter("precoMedio", item.getPrecoMedio())
					.setParameter("precoMedioFatorICMS", item.getPrecoMedioFatorICMS())
					.setParameter("idItemEstoque", item.getId()).executeUpdate();
		}
	}

	public void alterarQuantidade(Integer idItemEstoque, Integer quantidade) {
		entityManager.createQuery("update ItemEstoque i set i.quantidade = :quantidade where i.id = :idItemEstoque ")
				.setParameter("quantidade", quantidade).setParameter("idItemEstoque", idItemEstoque).executeUpdate();
	}

	public Double calcularValorEstoque(Integer idMaterial, FormaMaterial formaMaterial) {
		StringBuilder select = new StringBuilder();
		select.append("select SUM(i.precoMedio * i.quantidade * (1 + i.aliquotaIPI)) from ItemEstoque i ");
		if (idMaterial != null && formaMaterial != null) {
			select.append("where i.material.id = :idMaterial and i.formaMaterial = :formaMaterial ");
		}

		if (idMaterial != null && formaMaterial == null) {
			select.append("where i.material.id = :idMaterial ");
		}

		if (idMaterial == null && formaMaterial != null) {
			select.append("where i.formaMaterial = :formaMaterial ");
		}

		Query query = entityManager.createQuery(select.toString());
		if (idMaterial != null) {
			query.setParameter("idMaterial", idMaterial);
		}

		if (formaMaterial != null) {
			query.setParameter("formaMaterial", formaMaterial);
		}
		return QueryUtil.gerarRegistroUnico(query, Double.class, 0d);
	}

	private StringBuilder gerarConstrutorItemEstoque() {
		return new StringBuilder(
				"select new ItemEstoque(i.id, i.formaMaterial, i.descricaoPeca, i.material.sigla, i.medidaExterna, i.medidaInterna, i.comprimento, i.precoMedio, i.precoMedioFatorICMS, i.margemMinimaLucro, i.quantidade, i.quantidadeMinima, i.aliquotaIPI) from ItemEstoque i ");
	}

	public void inserirConfiguracaoEstoque(ItemEstoque configuracao) throws BusinessException {

		StringBuilder update = new StringBuilder(
				"update ItemEstoque i set i.margemMinimaLucro = :margemMinimaLucro, i.quantidadeMinima = :quantidadeMinima where i.material = :material and i.formaMaterial = :formaMaterial ");

		if (configuracao.contemMedida()) {

			if (configuracao.getMedidaExterna() != null) {
				update.append("and i.medidaExterna = :medidaExterna ");
			} else {
				update.append("and i.medidaExterna is null ");
			}

			if (configuracao.getMedidaInterna() != null) {
				update.append("and i.medidaInterna = :medidaInterna ");
			} else {
				update.append("and i.medidaInterna is null ");
			}

			if (configuracao.getComprimento() != null) {
				update.append("and i.comprimento = :comprimento ");
			} else {
				update.append("and i.comprimento is null ");
			}
		}

		Query query = entityManager.createQuery(update.toString())

		.setParameter("material", configuracao.getMaterial())
				.setParameter("formaMaterial", configuracao.getFormaMaterial())
				.setParameter("margemMinimaLucro", configuracao.getMargemMinimaLucro())
				.setParameter("quantidadeMinima", configuracao.getQuantidadeMinima());

		if (configuracao.contemMedida()) {

			if (configuracao.getMedidaExterna() != null) {
				query.setParameter("medidaExterna", configuracao.getMedidaExterna());
			}

			if (configuracao.getMedidaInterna() != null) {
				query.setParameter("medidaInterna", configuracao.getMedidaInterna());
			}

			if (configuracao.getComprimento() != null) {
				query.setParameter("comprimento", configuracao.getComprimento());
			}
		}
		query.executeUpdate();
	}

	public void inserirConfiguracaoNcmEstoque(Integer idMaterial, FormaMaterial formaMaterial, String ncm) {
		entityManager
				.createQuery(
						"update ItemEstoque i set i.ncm = :ncm where i.material.id = :idMaterial and i.formaMaterial = :formaMaterial ")
				.setParameter("idMaterial", idMaterial).setParameter("formaMaterial", formaMaterial)
				.setParameter("ncm", ncm).executeUpdate();
	}

	public ItemEstoque pesquisarById(Integer idItemEstoque) {
		return pesquisarById(ItemEstoque.class, idItemEstoque);
	}

	public FormaMaterial pesquisarFormaMaterialItemEstoque(Integer idItemEstoque) {
		return QueryUtil.gerarRegistroUnico(
				entityManager.createQuery("select i.formaMaterial from ItemEstoque i where i.id = :idItemEstoque")
						.setParameter("idItemEstoque", idItemEstoque), FormaMaterial.class, null);
	}

	public List<ItemEstoque> pesquisarItemEstoque(Integer idMaterial, FormaMaterial formaMaterial, String descricaoPeca) {
		return pesquisarItemEstoque(idMaterial, formaMaterial, descricaoPeca, true);
	}

	@SuppressWarnings("unchecked")
	@WARNING(data = "02/07/2015", descricao = "Aqui temos um like na descricao do material o que pode acarretar lentidao nas pesquisas caso existe um grande numero de pecas no estoque")
	public List<ItemEstoque> pesquisarItemEstoque(Integer idMaterial, FormaMaterial formaMaterial,
			String descricaoPeca, boolean zeradosExcluidos) {
		StringBuilder select = gerarConstrutorItemEstoque();

		if (idMaterial != null || formaMaterial != null) {
			select.append("where ");
		}

		if (idMaterial != null) {
			select.append("i.material.id = :idMaterial ");
		}

		if (formaMaterial != null && idMaterial != null) {
			select.append("and i.formaMaterial = :formaMaterial ");
		} else if (formaMaterial != null) {
			select.append("i.formaMaterial = :formaMaterial ");
		}

		if (StringUtils.isNotEmpty(descricaoPeca)) {
			select.append("and i.descricaoPeca like :descricaoPeca ");
		}

		if (zeradosExcluidos) {
			select.append("and i.quantidade > 0 ");
		}

		if (FormaMaterial.CH.equals(formaMaterial) || FormaMaterial.TB.equals(formaMaterial)) {
			select.append("order by i.formaMaterial, i.material.sigla, i.medidaExterna asc, i.medidaInterna asc, i.comprimento asc ");

		} else {
			select.append("order by i.formaMaterial, i.material.sigla, i.medidaInterna asc, i.medidaExterna asc, i.comprimento asc ");
		}

		Query query = entityManager.createQuery(select.toString());
		if (idMaterial != null) {
			query.setParameter("idMaterial", idMaterial);
		}

		if (formaMaterial != null) {
			query.setParameter("formaMaterial", formaMaterial);
		}

		if (StringUtils.isNotEmpty(descricaoPeca)) {
			query.setParameter("descricaoPeca", "%" + descricaoPeca + "%");
		}
		return query.getResultList();
	}

	public ItemEstoque pesquisarItemEstoqueByMedida(Integer idMaterial, FormaMaterial formaMaterial,
			Double medidaExterna, Double medidaInterna, Double comprimento, boolean apenasID) {

		boolean conteMedida = medidaExterna != null || medidaInterna != null || comprimento != null;
		boolean conteMaterial = idMaterial != null || formaMaterial != null;

		// Pois sao parametros que todo item deve conter
		if (!conteMaterial || !conteMedida) {
			return null;
		}

		StringBuilder select = new StringBuilder();
		if (apenasID) {
			select.append("select new ItemEstoque(i.id) ");
		} else {
			select.append("select i ");
		}

		select.append("from ItemEstoque i where i.material.id = :idMaterial and i.formaMaterial = :formaMaterial ");

		if (medidaExterna != null) {
			select.append("and i.medidaExterna = :medidaExterna ");
		} else {
			select.append("and i.medidaExterna is null ");
		}

		if (medidaInterna != null) {
			select.append("and i.medidaInterna = :medidaInterna ");
		} else {
			select.append("and i.medidaInterna is null ");
		}

		if (comprimento != null) {
			select.append("and i.comprimento = :comprimento ");
		} else {
			select.append("and i.comprimento is null ");
		}

		// A ordenacao desses tipos deve ser diferentes mesmo
		if (FormaMaterial.CH.equals(formaMaterial) || FormaMaterial.TB.equals(formaMaterial)) {
			select.append("order by i.formaMaterial, i.material.sigla, i.medidaExterna asc, i.medidaInterna asc, i.comprimento asc ");
		} else {
			select.append("order by i.formaMaterial, i.material.sigla, i.medidaInterna asc, i.medidaExterna asc, i.comprimento asc ");
		}

		TypedQuery<ItemEstoque> query = entityManager.createQuery(select.toString(), ItemEstoque.class)
				.setParameter("idMaterial", idMaterial).setParameter("formaMaterial", formaMaterial);

		if (medidaExterna != null) {
			query.setParameter("medidaExterna", medidaExterna);
		}
		if (medidaInterna != null) {
			query.setParameter("medidaInterna", medidaInterna);
		}
		if (comprimento != null) {
			query.setParameter("comprimento", comprimento);
		}

		List<ItemEstoque> l = query.getResultList();
		return recuperarItemNaoZerado(l);
	}

	public List<ItemEstoque> pesquisarItemEstoqueEscasso() {
		StringBuilder select = gerarConstrutorItemEstoque();

		select.append(" where i.quantidade < i.quantidadeMinima order by i.formaMaterial, i.material.sigla, i.medidaExterna asc, i.medidaInterna asc, i.comprimento asc ");
		return entityManager.createQuery(select.toString(), ItemEstoque.class).getResultList();
	}

	public Object[] pesquisarMargemMininaEPrecoMedio(Integer idItemEstoque) {
		return QueryUtil
				.gerarRegistroUnico(
						entityManager
								.createQuery(
										"select i.margemMinimaLucro, i.precoMedioFatorICMS, i.precoMedio, i.aliquotaIPI from ItemEstoque i where i.id= :idItemEstoque")
								.setParameter("idItemEstoque", idItemEstoque), Object[].class, new Object[] { null,
								null, null });
	}

	public String pesquisarNcmItemEstoque(Integer idMaterial, FormaMaterial formaMaterial) {
		Query query = entityManager
				.createQuery(
						"select i.ncm from ItemEstoque i where i.material.id = :idMaterial and i.formaMaterial = :formaMaterial and i.ncm != null")
				.setParameter("idMaterial", idMaterial).setParameter("formaMaterial", formaMaterial).setFirstResult(0)
				.setMaxResults(1);

		return QueryUtil.gerarRegistroUnico(query, String.class, null);
	}

	public String pesquisarNcmItemEstoque(ItemEstoque item) {
		if (item == null || item.getMaterial() == null) {
			return null;
		}
		return pesquisarNcmItemEstoque(item.getMaterial().getId(), item.getFormaMaterial());
	}

	public ItemEstoque pesquisarPecaByDescricao(Integer idMaterial, String descricaoPeca, boolean apenasID) {
		if (StringUtils.isEmpty(descricaoPeca) || idMaterial == null) {
			return null;
		}
		StringBuilder select = new StringBuilder();
		if (apenasID) {
			select.append("select new ItemEstoque(i.id, i.ncm) ");
		} else {
			select.append("select i ");
		}
		select.append("from ItemEstoque i where i.material.id = :idMaterial and i.formaMaterial = :formaMaterial and i.descricaoPeca = :descricaoPeca");
		TypedQuery<ItemEstoque> query = entityManager.createQuery(select.toString(), ItemEstoque.class);
		List<ItemEstoque> l = query.setParameter("formaMaterial", FormaMaterial.PC)
				.setParameter("idMaterial", idMaterial).setParameter("descricaoPeca", descricaoPeca).getResultList();

		return recuperarItemNaoZerado(l);
	}

	public List<ItemEstoque> pesquisarPrecoMedioAliquotaICMSItemEstoque(Integer idItemEstoque, Integer idMaterial,
			FormaMaterial formaMaterial) {
		StringBuilder select = new StringBuilder(
				"select new ItemEstoque(i.id, i.precoMedio, i.aliquotaICMS) from ItemEstoque i ");

		if (idItemEstoque != null) {
			select.append("where i.id = :idItemEstoque ");
		} else {
			select.append("where i.formaMaterial = :formaMaterial and i.material.id = :idMaterial ");
		}

		TypedQuery<ItemEstoque> query = entityManager.createQuery(select.toString(), ItemEstoque.class);
		if (idItemEstoque != null) {
			query.setParameter("idItemEstoque", idItemEstoque);
		} else {
			query.setParameter("formaMaterial", formaMaterial).setParameter("idMaterial", idMaterial);
		}

		return query.getResultList();
	}

	public Integer pesquisarQuantidadeByIdItemEstoque(Integer idItemEstoque) {
		return QueryUtil.gerarRegistroUnico(
				entityManager.createQuery("select i.quantidade from ItemEstoque i where i.id = :idItemEstoque")
						.setParameter("idItemEstoque", idItemEstoque), Integer.class, 0);
	}

	@WARNING(data = "08/07/2015", descricao = "Esse metodo foi criado pois no banco de dados foram incluido elementos com duplicidade, alguns zerado ou nao, entao vamos retornar o primeiro com quantidade")
	private ItemEstoque recuperarItemNaoZerado(List<ItemEstoque> listaItem) {
		if (listaItem == null || listaItem.size() == 0) {
			return null;
		}
		for (ItemEstoque itemEstoque : listaItem) {
			if (itemEstoque.getQuantidade() > 0) {
				return itemEstoque;
			}
		}
		return listaItem.get(0);
	}

}
