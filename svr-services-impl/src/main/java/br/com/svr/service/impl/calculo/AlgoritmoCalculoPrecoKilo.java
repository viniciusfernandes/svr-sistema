package br.com.svr.service.impl.calculo;

import br.com.svr.service.calculo.exception.AlgoritmoCalculoException;
import br.com.svr.service.entity.ItemPedido;

public class AlgoritmoCalculoPrecoKilo implements AlgoritmoCalculo {

	@Override
	public double calcular(ItemPedido itemPedido) throws AlgoritmoCalculoException {
		if (itemPedido.getMaterial() == null || itemPedido.getMaterial().getPesoEspecifico() == null) {
			throw new AlgoritmoCalculoException("Peso espec�fico do material n�o pode ser nulo");
		}
		// multiplicando a quantidade de itens pelo valor da venda pelo peso de
		// cada item
		return itemPedido.calcularPrecoTotalVenda() * CalculadoraItem.calcularKiloUnidade(itemPedido);
	}

}
