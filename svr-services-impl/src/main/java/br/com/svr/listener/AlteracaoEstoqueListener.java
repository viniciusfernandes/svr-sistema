package br.com.svr.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import br.com.svr.service.monitor.ItemAguardandoEmpacotamentoMonitor;

/*
@MessageDriven(name = "AlteracaoEstoqueListener", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/vendas/AlteracaoEstoque"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
*/
public class AlteracaoEstoqueListener implements MessageListener {

	// @EJB
	private ItemAguardandoEmpacotamentoMonitor pedidoAguardandoMaterialMonitor;

	/*
	 * Esse listener deveria ser acionado sempre que haver uma alteracao de estoque, e o o publisher
	 * estava no controller de estoque antes de ser desabilitado no sistema.
	 */
	@Override
	public void onMessage(Message mensagem) {
		pedidoAguardandoMaterialMonitor.empacotarItemPedidoAguardandoMaterial();
		pedidoAguardandoMaterialMonitor.empacotarItemPedidoAguardandoCompra();
	}

}
