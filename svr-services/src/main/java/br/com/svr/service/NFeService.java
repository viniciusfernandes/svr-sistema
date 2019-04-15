package br.com.svr.service;

import java.util.List;

import javax.ejb.Local;

import br.com.svr.service.entity.ItemPedido;
import br.com.svr.service.entity.Logradouro;
import br.com.svr.service.entity.NFeItemFracionado;
import br.com.svr.service.entity.NFePedido;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.nfe.DuplicataNFe;
import br.com.svr.service.nfe.EnderecoNFe;
import br.com.svr.service.nfe.NFe;
import br.com.svr.service.nfe.constante.TipoNFe;
import br.com.svr.service.wrapper.Periodo;

@Local
public interface NFeService {

	NFe carregarIdentificacaoEmitente(NFe nFe, Integer idPedido);

	String emitirNFe(NFe nFe, TipoNFe tipoNFe, Integer idPedido) throws BusinessException;

	String emitirNFeDevolucao(NFe nFe, Integer idPedido) throws BusinessException;

	String emitirNFeEntrada(NFe nFe, Integer idPedido) throws BusinessException;

	String emitirNFeSaida(NFe nFe, Integer idPedido) throws BusinessException;

	String emitirNFeTriangularizacao(NFe nFe, Integer idPedido) throws BusinessException;

	List<DuplicataNFe> gerarDuplicataDataAmericanaByIdPedido(Integer idPedido);

	List<DuplicataNFe> gerarDuplicataDataLatinaByIdPedido(Integer idPedido);

	EnderecoNFe gerarEnderecoNFe(Logradouro logradouro, String telefone);

	NFe gerarNFeByIdPedido(Integer idPedido) throws BusinessException;

	NFe gerarNFeByNumero(Integer numero) throws BusinessException;

	Integer[] gerarNumeroSerieModeloNFe() throws BusinessException;

	String gerarXMLNfe(NFe nFe, Integer idPedido) throws BusinessException;

	List<Object[]> pesquisarCFOP();

	Integer pesquisarIdPedidoByNumeroNFe(Integer numeroNFe);

	List<NFeItemFracionado> pesquisarItemFracionado();

	List<NFeItemFracionado> pesquisarItemFracionadoByNumeroNFe(Integer numeroNFe);

	List<NFeItemFracionado> pesquisarNFeItemFracionadoQuantidades(Integer numeroNFe);

	List<NFePedido> pesquisarNFePedidoSaidaEmitidaByPeriodo(Periodo periodo);

	List<Integer> pesquisarNumeroNFeByIdPedido(Integer idPedido);

	List<ItemPedido> pesquisarQuantitadeItemRestanteByIdPedido(Integer idPedido);

	List<Integer[]> pesquisarTotalItemFracionado(Integer idPedido);

	Integer pesquisarTotalItemFracionadoByNumeroItemNumeroNFe(Integer numeroItem, Integer numeroNFe);

	List<Integer[]> pesquisarTotalItemFracionadoByNumeroNFe(Integer numeroNFe);

	Integer pesqusisarQuantidadeTotalFracionadoByIdItemPedidoNFeExcluida(Integer idItem, Integer numeroNFe);

	void removerItemFracionadoNFe(Integer idItemFracionado);

	void removerNFe(Integer numeroNFe);

	void validarEmissaoNFePedido(Integer idPedido) throws BusinessException;

	void validarNumeroNFePedido(Integer idPedido, Integer numeroNFe) throws BusinessException;

}
