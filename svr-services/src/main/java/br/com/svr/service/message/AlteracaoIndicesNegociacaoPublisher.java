package br.com.svr.service.message;

//@Local
public interface AlteracaoIndicesNegociacaoPublisher {
	public void publicar(Integer idCliente, Double indiceQuantidade, Double indiceValor);
}
