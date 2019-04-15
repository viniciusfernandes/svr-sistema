package br.com.svr.service.impl.mensagem.email;

import br.com.svr.service.entity.Pedido;
import br.com.svr.service.mensagem.email.AnexoEmail;
import br.com.svr.service.mensagem.email.exception.MensagemEmailException;

public class VendaClienteEmailBuilder extends VendaEmailBuilder {

	public VendaClienteEmailBuilder(Pedido pedido, AnexoEmail arquivoAnexo, AnexoEmail... anexos)
			throws MensagemEmailException {
		super(pedido, arquivoAnexo, anexos);
	}

	@Override
	public String gerarDestinatario() {
		return pedido.getContato().getEmail();
	}
}
