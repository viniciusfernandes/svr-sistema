package br.com.svr.service.impl.mensagem.email;

import br.com.svr.service.entity.Pedido;
import br.com.svr.service.mensagem.email.AnexoEmail;
import br.com.svr.service.mensagem.email.exception.MensagemEmailException;
import br.com.svr.util.StringUtils;

public class CompraEmailBuilder extends PedidoEmailBuilder {

	public CompraEmailBuilder(Pedido pedido, AnexoEmail pdfPedido, AnexoEmail... anexos) throws MensagemEmailException {
		super(pedido, pdfPedido, anexos);
		pdfPedido.setNome("Pedido No. " + pedido.getId() + " " + pedido.getCliente().getNomeFantasia());
		pdfPedido.setDescricao("Pedido de comprado por " + pedido.getComprador().getNome());
	}

	@Override
	public String gerarConteudo() {
		return "Segue o pedido de compra efetuado por "
				+ pedido.getComprador().getNome()
				+ (StringUtils.isNotEmpty(pedido.getObservacao()) ? "\n\nFavor considerar as seguintes observa��es:\n"
						+ pedido.getObservacao() : "");
	}

	@Override
	public String gerarDestinatario() {
		// Aqui estamos enviando o email para a representada (fornecedor no caso
		// de compras).
		return pedido.getRepresentada().getEmail();
	}

	@Override
	public String gerarDestinatarioCc() {
		// Aqui estamos enviando o email para
		// os emails cadastrados no cliente comprador (no caso a revendedora)
		// para acompanhamento das compras.
		return pedido.getCliente().getEmail();
	}

	@Override
	public String gerarRemetente() {
		return pedido.getComprador().getEmail();
	}

	@Override
	public String gerarTitulo() {
		return "Pedido de Compra No: " + pedido.getId() + " - " + pedido.getComprador().getNome();
	}
}
