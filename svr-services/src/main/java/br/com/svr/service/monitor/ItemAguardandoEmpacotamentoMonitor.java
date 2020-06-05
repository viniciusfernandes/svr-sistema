package br.com.svr.service.monitor;

import java.util.Collection;

import javax.ejb.Local;

@Local
public interface ItemAguardandoEmpacotamentoMonitor {
	void monitorarItemPedido();

	Collection<Integer> monitorarItemPedidoAguardandoCompra();

	Collection<Integer> monitorarItemPedidoAguardandoMaterial();
}
