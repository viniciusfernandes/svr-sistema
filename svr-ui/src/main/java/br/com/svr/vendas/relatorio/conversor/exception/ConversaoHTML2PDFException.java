package br.com.svr.vendas.relatorio.conversor.exception;

import br.com.svr.service.exception.BusinessException;

public class ConversaoHTML2PDFException extends BusinessException {
    private static final long serialVersionUID = 7021878033141035308L;

    public ConversaoHTML2PDFException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
