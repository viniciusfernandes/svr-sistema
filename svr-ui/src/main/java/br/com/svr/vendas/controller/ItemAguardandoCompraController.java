package br.com.svr.vendas.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.svr.service.PedidoService;
import br.com.svr.service.RepresentadaService;
import br.com.svr.service.constante.TipoPedido;
import br.com.svr.service.entity.Cliente;
import br.com.svr.service.entity.ItemPedido;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.relatorio.RelatorioService;
import br.com.svr.service.wrapper.Periodo;
import br.com.svr.service.wrapper.RelatorioWrapper;
import br.com.svr.vendas.controller.anotacao.Servico;
import br.com.svr.vendas.login.UsuarioInfo;

@Resource
public class ItemAguardandoCompraController extends AbstractController {

    @Servico
    private PedidoService pedidoService;

    @Servico
    private RelatorioService relatorioService;

    @Servico
    private RepresentadaService representadaService;

    public ItemAguardandoCompraController(Result result, UsuarioInfo usuarioInfo) {
        super(result, usuarioInfo);
    }

    @Post("itemAguardandoCompra/item/compra")
    public void comprarItemPedido(Date dataInicial, Date dataFinal, Integer idRepresentadaFornecedora, Cliente cliente,
            List<Integer> listaIdItem) {
        try {
            final Set<Integer> ids = listaIdItem == null ? new HashSet<Integer>() : new HashSet<Integer>(listaIdItem);
            Integer idPedidoCompra = pedidoService
                    .comprarItemPedido(getCodigoUsuario(), idRepresentadaFornecedora, ids);
            addAtributo("dataInicial", formatarData(dataInicial));
            addAtributo("dataFinal", formatarData(dataFinal));
            addAtributo("cliente", cliente);

            redirecTo(PedidoController.class).pesquisarPedidoById(idPedidoCompra, TipoPedido.COMPRA, false);
        } catch (BusinessException e) {
            addAtributo("permanecerTopo", true);
            gerarListaMensagemErro(e);
            pesquisarItemAguardandoCompra(dataInicial, dataFinal, cliente);
        }
    }

    @Post("itemAguardandoCompra/empacotamento")
    public void empacotarPedidoAguardandoCompra(Date dataInicial, Date dataFinal, Integer idPedido, Cliente cliente) {
        boolean empacotamentoOK;
        try {
            empacotamentoOK = pedidoService.empacotarPedidoAguardandoCompra(idPedido);
            if (empacotamentoOK) {
                gerarMensagemSucesso("O pedido No. " + idPedido + " foi enviado para o empacotamento com sucesso");
            } else {
                gerarListaMensagemErro("O pedido No. " + idPedido
                        + " n�o pode ser enviado para o empacotamento pois algum de seus itens no existe no estoque");
            }
        } catch (BusinessException e) {
            gerarListaMensagemErro(e);
            addAtributo("permanecerTopo", true);
        }
        pesquisarItemAguardandoCompra(dataInicial, dataFinal, cliente);
    }

    @Get("itemAguardandoCompra")
    public void itemAguardandoCompraHome() {
        addAtributo("listaFornecedor", this.representadaService.pesquisarFornecedorAtivo());
    }

    @Get("itemAguardandoCompra/item/listagem")
    public void pesquisarItemAguardandoCompra(Date dataInicial, Date dataFinal, Cliente cliente) {
        try {
            Periodo periodo = Periodo.gerarPeriodo(dataInicial, dataFinal);
            RelatorioWrapper<Integer, ItemPedido> relatorio = relatorioService.gerarRelatorioItemAguardandoCompra(
                    cliente.getId(), periodo);

            addAtributo("relatorio", relatorio);
            if (contemAtributo("permanecerTopo")) {
                irTopoPagina();
            } else {
                irRodapePagina();
            }
        } catch (BusinessException e) {
            gerarListaMensagemErro(e);
            irTopoPagina();
        }

        addAtributo("dataInicial", formatarData(dataInicial));
        addAtributo("dataFinal", formatarData(dataFinal));
        addAtributo("cliente", cliente);
    }
}
