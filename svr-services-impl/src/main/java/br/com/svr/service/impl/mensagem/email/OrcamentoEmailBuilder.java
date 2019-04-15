package br.com.svr.service.impl.mensagem.email;

import br.com.svr.service.entity.Pedido;
import br.com.svr.service.mensagem.email.AnexoEmail;
import br.com.svr.service.mensagem.email.exception.MensagemEmailException;
import br.com.svr.util.NumeroUtils;

public class OrcamentoEmailBuilder extends PedidoEmailBuilder {

	public OrcamentoEmailBuilder(Pedido pedido, AnexoEmail pdfPedido, AnexoEmail... anexos)
			throws MensagemEmailException {
		super(pedido, pdfPedido, anexos);
		pdfPedido.setNome("Or�amento No. " + pedido.getId() + " " + pedido.getCliente().getNomeFantasia());
		pdfPedido.setDescricao("Or�amento realizado com sucesso.");
	}

	@Override
	public String gerarConteudo() {
		return "Prezado " + pedido.getContato().getNome() + ", segue o or�amento para analise. Valor total de R$ "
				+ NumeroUtils.formatarValor2Decimais(pedido.getValorPedido());
	}

	@Override
	public String gerarDestinatario() {
		return pedido.getContato().getEmail();
	}

	@Override
	public String gerarDestinatarioCc() {
		return pedido.getVendedor().getEmailCopia();
	}

	@Override
	public String gerarRemetente() {
		return pedido.getVendedor().getEmail();
	}

	@Override
	public String gerarTitulo() {
		return "Or�amento de Venda No. " + pedido.getId() + " - " + pedido.getCliente().getNomeFantasia();
	}

}
