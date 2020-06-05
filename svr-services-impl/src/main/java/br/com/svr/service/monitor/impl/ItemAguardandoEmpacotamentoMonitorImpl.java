package br.com.svr.service.monitor.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.svr.service.PedidoService;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.monitor.ItemAguardandoEmpacotamentoMonitor;

@Stateless
public class ItemAguardandoEmpacotamentoMonitorImpl implements ItemAguardandoEmpacotamentoMonitor {

	private final Logger logger = Logger.getLogger(ItemAguardandoEmpacotamentoMonitorImpl.class.getName());

	@EJB
	private PedidoService pedidoService;

	@Override
	public void monitorarItemPedido() {
		monitorarItemPedidoAguardandoMaterial();
		monitorarItemPedidoAguardandoCompra();
	}

	@Override
	public Collection<Integer> monitorarItemPedidoAguardandoCompra() {
		final List<Integer> listaIdPedido = pedidoService.pesquisarIdPedidoAguardandoCompra();
		boolean empacotamentoOk = false;
		// Listas utilizadas para logar o que foi enviado para o empacotamento.
		final Set<Integer> empacotados = new TreeSet<Integer>();
		for (final Integer idPedido : listaIdPedido) {
			try {
				// Encomendado mesmo apos o processamento do agendamento, caso contrario
				// teremos inconsistencia no estado do pedido podendo retornar ao fluxo.
				// Aqui estamos garantindo que mesmo que o pedido permaneca como
				// revenda aguardando encomenda, mas ele ja passou por essa etapa.
				empacotamentoOk = pedidoService.empacotarItemAguardandoCompra(idPedido);
				if (empacotamentoOk) {
					empacotados.add(idPedido);
				}

			}
			catch (final BusinessException e) {
				logger.log(Level.SEVERE,
						"Falha no processamento e reenvio do pedido No. " + idPedido + ". Possivel causa: " + e.getMensagemConcatenada());
			}
		}
		if (!empacotados.isEmpty()) {
			logger.info("Monitor enviou para o empacotamento os pedidos aguardando compra No.: " + Arrays.deepToString(empacotados.toArray()));
		}
		return empacotados;
	}

	@Override
	public Collection<Integer> monitorarItemPedidoAguardandoMaterial() {
		final List<Integer> listaIdPedido = pedidoService.pesquisarIdPedidoAguardandoMaterial();
		boolean empacotamentoOk = false;
		// Listas utilizadas para logar o que foi enviado para o empacotamento.
		final Set<Integer> empacotados = new TreeSet<Integer>();
		for (final Integer idPedido : listaIdPedido) {
			try {
				// Encomendado mesmo apos o processamento do agendamento, caso contrario
				// teremos inconsistencia no estado do pedido podendo retornar ao fluxo.
				// Aqui estamos garantindo que mesmo que o pedido permaneca como
				// revenda aguardando encomenda, mas ele ja passou por essa etapa.
				empacotamentoOk = pedidoService.empacotarItemAguardandoMaterial(idPedido);
				if (empacotamentoOk) {
					empacotados.add(idPedido);
				}

			}
			catch (final BusinessException e) {
				logger.log(Level.SEVERE,
						"Falha no processamento e reenvio do pedido No. " + idPedido + ". Possivel causa: " + e.getMensagemConcatenada());
			}
		}
		if (!empacotados.isEmpty()) {
			logger.info("Monitor enviou para o empacotamento os pedidos aguardando material No.: " + Arrays.deepToString(empacotados.toArray()));
		}
		return empacotados;
	}
}
