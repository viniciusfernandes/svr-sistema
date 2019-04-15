package br.com.svr.service.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.svr.service.entity.ItemReservado;
import br.com.svr.service.impl.util.QueryUtil;

public class ItemReservadoDAO extends GenericDAO<ItemReservado> {

	public ItemReservadoDAO(EntityManager entityManager) {
		super(entityManager);
	}

	@SuppressWarnings("unchecked")
	public List<ItemReservado> pesquisarItemReservadoByIdItemEstoque(Integer idItemEstoque) {
		return entityManager
				.createQuery(
						"select new ItemReservado(i.id, i.itemPedido.id, i.itemEstoque.id) from ItemReservado i where i.itemEstoque.id =:idItemEstoque")
				.setParameter("idItemEstoque", idItemEstoque).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ItemReservado> pesquisarItemReservadoByIdItemPedido(Integer idItemPedido) {
		return entityManager
				.createQuery(
						"select new ItemReservado(i.id, i.itemPedido.id, i.itemEstoque.id) from ItemReservado i where i.itemPedido.id =:idItemPedido")
				.setParameter("idItemPedido", idItemPedido).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ItemReservado> pesquisarItemReservadoByIdPedido(Integer idPedido) {
		return entityManager
				.createQuery(
						"select new ItemReservado(i.id, i.itemPedido.id, i.itemPedido.quantidade, i.itemEstoque.id, i.itemEstoque.quantidade) from ItemReservado i where i.itemPedido.pedido.id = :idPedido")
				.setParameter("idPedido", idPedido).getResultList();
	}

	public Integer[] pesquisarQuantidadeEstoqueByIdItemPedido(Integer idItemPedido) {
		Object[] o = QueryUtil
				.gerarRegistroUnico(
						entityManager
								.createQuery(
										"select i.itemEstoque.id, i.itemEstoque.quantidade from ItemReservado i where i.itemPedido.id = :idItemPedido")
								.setParameter("idItemPedido", idItemPedido), Object[].class, null);

		return o == null || o.length <= 0 ? new Integer[] {} : new Integer[] {(Integer) o[0], (Integer) o[1]};
	}

	public Long pesquisarTotalItemPedidoReservado(Integer idPedido) {
		return (Long) entityManager
				.createQuery("select count(i.id) from ItemReservado i where i.itemPedido.pedido.id = :idPedido")
				.setParameter("idPedido", idPedido).getSingleResult();
	}

	public void removerByIdItemPedido(Integer idItemPedido) {
		entityManager.createQuery("delete from ItemReservado i where i.itemPedido.id =:idItemPedido")
				.setParameter("idItemPedido", idItemPedido).executeUpdate();
	}
	
}
