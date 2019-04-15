package br.com.svr.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import br.com.svr.service.calculo.exception.AlgoritmoCalculoException;
import br.com.svr.service.constante.SituacaoPedido;
import br.com.svr.service.constante.TipoFinalidadePedido;
import br.com.svr.service.constante.TipoLogradouro;
import br.com.svr.service.constante.TipoPedido;
import br.com.svr.service.entity.Cliente;
import br.com.svr.service.entity.ItemPedido;
import br.com.svr.service.entity.LogradouroPedido;
import br.com.svr.service.entity.Pedido;
import br.com.svr.service.entity.Representada;
import br.com.svr.service.entity.Transportadora;
import br.com.svr.service.entity.Usuario;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.mensagem.email.AnexoEmail;
import br.com.svr.service.wrapper.PaginacaoWrapper;
import br.com.svr.service.wrapper.Periodo;
import br.com.svr.service.wrapper.TotalizacaoPedidoWrapper;

@Local
public interface PedidoService {
	Integer aceitarListaItemOrcamento(Integer idCliente, Integer idRepresentada, Integer idVendedor,
			TipoPedido tipoPedido, Integer[] listaIdItemSelecionado) throws BusinessException;

	Integer aceitarOrcamento(Integer idOrcamento) throws BusinessException;

	Integer aceitarOrcamentoENegociacaoByIdOrcamento(Integer idOrcamento) throws BusinessException;

	void alterarItemAguardandoCompraByIdPedido(Integer idPedido);

	void alterarItemAguardandoMaterialByIdPedido(Integer idPedido);

	void alterarQuantidadeRecepcionada(Integer idItemPedido, Integer quantidadeRecepcionada) throws BusinessException;

	void alterarQuantidadeReservadaByIdItemPedido(Integer idItemPedido);

	void alterarRevendaAguardandoMaterialByIdItem(Integer idItemPedido);

	void alterarSituacaoPedidoByIdItemPedido(Integer idItemPedido, SituacaoPedido situacaoPedido);

	void alterarSituacaoPedidoByIdItemPedido(List<Integer> listaIdItem, SituacaoPedido situacaoPedido);

	void alterarSituacaoPedidoByIdPedido(Integer idPedido, SituacaoPedido situacaoPedido);

	void calcularComissaoItemPedido(Integer idItem) throws BusinessException;

	List<Date> calcularDataPagamento(Integer idPedido);

	List<Date> calcularDataPagamento(Integer idPedido, Date dataInicial);

	Double calcularPesoItemPedido(ItemPedido itemPedido) throws AlgoritmoCalculoException;

	Double calcularValorFretePorItemByIdItem(Integer idItem);

	Double calcularValorFretePorItemByIdPedido(Integer idPedido);

	Double[] calcularValorFreteUnidadeByIdPedido(Integer idPedido);

	void cancelarOrcamento(Integer idOrcamento) throws BusinessException;

	void cancelarOrcamentoRemoverNegociacao(Integer idOrcamento) throws BusinessException;

	void cancelarPedido(Integer idPedido) throws BusinessException;

	Integer comprarItemPedido(Integer idComprador, Integer idFornecedor, Set<Integer> listaIdItemPedido)
			throws BusinessException;

	void configurarDataEnvio(Integer idPedido);

	boolean contemFornecedorDistintoByIdItem(List<Integer> listaIdItem);

	boolean contemItemPedido(Integer idPedido);

	boolean contemQuantidadeNaoRecepcionadaItemPedido(Integer idItemPedido);

	Integer copiarPedido(Integer idPedido, boolean isOrcamento) throws BusinessException;

	boolean empacotarItemAguardandoCompra(Integer idPedido) throws BusinessException;

	boolean empacotarItemAguardandoMaterial(Integer idPedido) throws BusinessException;

	boolean empacotarPedidoAguardandoCompra(Integer idPedido) throws BusinessException;

	void enviarPedido(Integer idPedido, AnexoEmail pdfPedido) throws BusinessException;

	void enviarPedido(Integer idPedido, AnexoEmail pdfPedido, AnexoEmail... anexos) throws BusinessException;

	Pedido gerarPedidoItemSelecionado(Integer idVendedor, boolean isCompra, boolean isOrcamento,
			List<Integer> listaIdItemSelecionado) throws BusinessException;

	void inserirDadosNotaFiscal(Pedido pedido);

	Integer inserirItemPedido(Integer idPedido, ItemPedido itemPedido) throws BusinessException;

	void inserirNcmItemAguardandoMaterialAssociadoByIdItemCompra(Integer idItemPedidoCompra, String ncm)
			throws BusinessException;

	Pedido inserirOrcamento(Pedido pedido) throws BusinessException;

	Pedido inserirPedido(Pedido pedido) throws BusinessException;

	boolean isCalculoIPIHabilitado(Integer idPedido);

	boolean isComissaoSimplesVendedor(Integer idPedido);

	boolean isOrcamentoAberto(Integer idPedido);

	boolean isPedidoCancelado(Integer idPedido);

	boolean isPedidoEnviado(Integer idPedido);

	boolean isPedidoExistente(Integer idPedido);

	PaginacaoWrapper<Pedido> paginarPedido(Integer idCliente, Integer idVendedor, Integer idFornecedor,
			boolean isCompra, Integer indiceRegistroInicial, Integer numeroMaximoRegistros, boolean isOrcamento);

	double pesquisarAliquotaIPIByIdItemPedido(Integer idItemPedido);

	List<Pedido> pesquisarBy(Pedido filtro, Integer indiceRegistroInicial, Integer numeroMaximoRegistros);

	List<Pedido> pesquisarByIdCliente(Integer idCliente);

	List<Pedido> pesquisarByIdCliente(Integer idCliente, Integer indiceRegistroInicial, Integer numeroMaximoRegistros);

	List<Pedido> pesquisarByIdClienteIdFornecedor(Integer idCliente, Integer idFornecedor, boolean isCompra,
			Integer indiceRegistroInicial, Integer numeroMaximoRegistros);

	List<ItemPedido> pesquisarCaracteristicaItemPedidoByNumeroItem(List<Integer> listaNumeroItem, Integer idPedido);

	Cliente pesquisarClienteByIdPedido(Integer idPedido);

	Cliente pesquisarClienteResumidoByIdPedido(Integer idPedido);

	double pesquisarComissaoRepresentadaByIdPedido(Integer idPedido);

	Pedido pesquisarCompraById(Integer id);

	List<Pedido> pesquisarCompraByPeriodoEComprador(Periodo periodo, Integer idComprador) throws BusinessException;

	Pedido pesquisarDadosNotaFiscalByIdItemPedido(Integer idItemPedido);

	Date pesquisarDataEnvio(Integer idPedido);

	List<Pedido> pesquisarEntregaVendaByPeriodo(Periodo periodo);

	List<Pedido> pesquisarEnviadosByPeriodoERepresentada(Periodo periodo, Integer idRepresentada);

	List<Pedido> pesquisarEnviadosByPeriodoEVendedor(Periodo periodo, Integer idVendedor) throws BusinessException;

	Integer pesquisarIdClienteByIdPedido(Integer idPedido);

	List<Integer> pesquisarIdItemPedidoByIdPedido(Integer idPedido);

	Integer pesquisarIdItemPedidoByIdPedidoSequencial(Integer idPedido, Integer sequencial);

	Object[] pesquisarIdMaterialFormaMaterialItemPedido(Integer idItemPedido);

	Object[] pesquisarIdNomeClienteNomeContatoValor(Integer idPedido);

	List<Integer> pesquisarIdPedidoAguardandoCompra();

	List<Integer> pesquisarIdPedidoAguardandoEmpacotamento();

	List<Integer> pesquisarIdPedidoAguardandoMaterial();

	List<Integer> pesquisarIdPedidoAssociadoByIdPedidoOrigem(Integer idPedidoOrigem, boolean isCompra);

	Integer pesquisarIdPedidoByIdItemPedido(Integer idItemPedido);

	List<Integer> pesquisarIdPedidoByIdItemPedido(List<Integer> listaIdItemPedido);

	List<Integer> pesquisarIdPedidoItemAguardandoCompra();

	Integer[] pesquisarIdPedidoQuantidadeSequencialByIdPedido(Integer idItem);

	Integer pesquisarIdRepresentadaByIdPedido(Integer idPedido);

	Integer pesquisarIdVendedorByIdPedido(Integer idPedido);

	List<ItemPedido> pesquisarItemAguardandoCompra(Integer idCliente, Periodo periodo);

	List<ItemPedido> pesquisarItemAguardandoMaterial(Integer idRepresentada, Periodo periodo);

	List<ItemPedido> pesquisarItemPedidoAguardandoEmpacotamento();

	List<ItemPedido> pesquisarItemPedidoAguardandoEmpacotamento(Integer idCliente);

	ItemPedido pesquisarItemPedidoById(Integer idItemPedido);

	List<ItemPedido> pesquisarItemPedidoById(List<Integer> listaIdItem);

	List<ItemPedido> pesquisarItemPedidoByIdClienteIdVendedorIdFornecedor(Integer idCliente, Integer idVendedor,
			Integer idFornecedor, boolean isOrcamento, boolean isCompra, Integer indiceRegistroInicial,
			Integer numeroMaximoRegistros, ItemPedido itemVendido);

	List<ItemPedido> pesquisarItemPedidoByIdPedido(Integer idPedido);

	List<ItemPedido> pesquisarItemPedidoCompraAguardandoRecepcao(Integer idRepresentada,
			List<Integer> listaNumeroPedido, Periodo periodo);

	List<ItemPedido> pesquisarItemPedidoCompraAguardandoRecepcao(Integer idRepresentada, Periodo periodo);

	List<ItemPedido> pesquisarItemPedidoCompradoResumidoByPeriodo(Periodo periodo);

	List<ItemPedido> pesquisarItemPedidoCompraEfetivada(Integer idRepresentada, List<Integer> listaNumeroPedido,
			Periodo periodo);

	List<ItemPedido> pesquisarItemPedidoCompraEfetivada(Integer idRepresentada, Periodo periodo);

	List<ItemPedido> pesquisarItemPedidoEncomendado();

	List<ItemPedido> pesquisarItemPedidoEncomendado(Integer idCliente, Date dataInicial, Date dataFinal);

	ItemPedido pesquisarItemPedidoPagamento(Integer idItemPedido);

	ItemPedido pesquisarItemPedidoQuantidadeESequencial(Integer idItem);

	List<ItemPedido> pesquisarItemPedidoRepresentacaoByPeriodo(Periodo periodo);

	ItemPedido pesquisarItemPedidoResumidoMaterialEMedidas(Integer idItem);

	List<ItemPedido> pesquisarItemPedidoResumidoMaterialEMedidasByIdPedido(Integer idPedido);

	List<ItemPedido> pesquisarItemPedidoRevendaByPeriodo(Periodo periodo);

	List<ItemPedido> pesquisarItemPedidoVendaByPeriodo(Periodo periodo, Integer idVendedor);

	List<ItemPedido> pesquisarItemPedidoVendaResumidaByPeriodo(Periodo periodo);

	List<LogradouroPedido> pesquisarLogradouro(Integer idPedido);

	List<LogradouroPedido> pesquisarLogradouro(Integer idPedido, TipoLogradouro tipo);

	String pesquisarNomeVendedorByIdPedido(Integer idPedido);

	String pesquisarNumeroPedidoClienteByIdPedido(Integer idPedido);

	Pedido pesquisarPedidoById(Integer id);

	Pedido pesquisarPedidoById(Integer idPedido, boolean isCompra);

	List<Pedido> pesquisarPedidoByIdCliente(Integer idCliente, Integer indiceRegistroInicial,
			Integer numeroMaximoRegistros);

	List<Pedido> pesquisarPedidoByIdClienteIdVendedorIdFornecedor(Integer idCliente, Integer idVendedor,
			Integer idFornecedor, boolean isCompra, Integer indiceRegistroInicial, Integer numeroMaximoRegistros);

	Pedido pesquisarPedidoByIdItemPedido(Integer idItemPedido);

	List<Pedido> pesquisarPedidoCompraByPeriodo(Periodo periodo);

	List<Pedido> pesquisarPedidoVendaByPeriodo(Periodo periodo);

	Usuario pesquisarProprietario(Integer idPedido);

	int pesquisarQuantidadeItemPedido(Integer idItemPedido);

	Integer[] pesquisarQuantidadeItemPedidoByIdItemPedido(Integer idItemPedido);

	List<Integer[]> pesquisarQuantidadeItemPedidoByIdPedido(Integer idPedido);

	int pesquisarQuantidadeNaoRecepcionadaItemPedido(Integer idItemPedido);

	int pesquisarQuantidadeRecepcionadaItemPedido(Integer idItemPedido);

	Integer pesquisarQuantidadeReservadaByIdItemPedido(Integer idItemPedido);

	Representada pesquisarRepresentadaByIdPedido(Integer idPedido);

	Representada pesquisarRepresentadaNomeFantasiaByIdPedido(Integer idPedido);

	Representada pesquisarRepresentadaResumidaByIdPedido(Integer idPedido);

	Integer pesquisarSequencialItemByIdItemPedido(Integer idItem);

	List<SituacaoPedido> pesquisarSituacaoCompraEfetivada();

	SituacaoPedido pesquisarSituacaoPedidoById(Integer idPedido);

	SituacaoPedido pesquisarSituacaoPedidoByIdItemPedido(Integer idItemPedido);

	List<SituacaoPedido> pesquisarSituacaoRevendaEfetivada();

	List<SituacaoPedido> pesquisarSituacaoVendaEfetivada();

	Object[] pesquisarTelefoneContatoByIdPedido(Integer idPedido);

	List<TipoFinalidadePedido> pesquisarTipoFinaldadePedidoFaturavel();

	TipoPedido pesquisarTipoPedidoByIdPedido(Integer idPedido);

	List<TotalizacaoPedidoWrapper> pesquisarTotalCompraResumidaByPeriodo(Periodo periodo);

	long pesquisarTotalItemCompradoNaoRecebido(Integer idPedido);

	Long pesquisarTotalItemPedido(Integer idPedido);

	long pesquisarTotalItemPedidoByIdItem(Integer idItem);

	Long pesquisarTotalPedidoByIdClienteIdFornecedor(Integer idCliente, Integer idFornecedor, boolean isOrcamento,
			boolean isCompra);

	Long pesquisarTotalPedidoByIdClienteIdVendedorIdFornecedor(Integer idCliente, Integer idVendedor,
			Integer idFornecedor, boolean isOrcamento, boolean isCompra, ItemPedido itemVendido);

	Long pesquisarTotalPedidoVendaByIdClienteIdVendedorIdFornecedor(Integer idCliente);

	List<TotalizacaoPedidoWrapper> pesquisarTotalPedidoVendaResumidaByPeriodo(Periodo periodo);

	Transportadora pesquisarTransportadoraByIdPedido(Integer idPedido);

	Transportadora pesquisarTransportadoraResumidaByIdPedido(Integer idPedido);

	Double pesquisarValorFreteByIdItem(Integer idItem);

	Double pesquisarValorFreteByIdPedido(Integer idPedido);

	Double pesquisarValorPedido(Integer idPedido);

	Double[] pesquisarValorPedidoByItemPedido(Integer idItemPedido);

	Double pesquisarValorPedidoIPI(Integer idPedido);

	List<TotalizacaoPedidoWrapper> pesquisarValorVendaClienteByPeriodo(Periodo periodo, Integer idCliente,
			boolean isOrcamento);

	Pedido pesquisarVendaById(Integer id);

	List<Pedido> pesquisarVendaByPeriodoEVendedor(boolean orcamento, Periodo periodo, Integer idVendedor)
			throws BusinessException;

	Usuario pesquisarVendedorByIdItemPedido(Integer idItemPedido);

	void reencomendarItemPedido(Integer idItemPedido) throws BusinessException;

	Integer refazerPedido(Integer idPedido) throws BusinessException;

	Pedido removerItemPedido(Integer idItemPedido) throws BusinessException;

	void removerLogradouroPedido(Integer idPedido);

}
