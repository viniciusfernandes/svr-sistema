package br.com.svr.vendas.controller;

import java.util.Date;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.svr.service.entity.Pedido;
import br.com.svr.service.relatorio.RelatorioService;
import br.com.svr.service.validacao.InformacaoInvalidaException;
import br.com.svr.service.wrapper.Periodo;
import br.com.svr.vendas.controller.anotacao.Servico;

@Resource
public class RelatorioPedidoPeriodoController extends AbstractController {

    @Servico
    private RelatorioService relatorioService;

    public RelatorioPedidoPeriodoController(Result result) {
        super(result);
    }

    @Get("relatorio/pedido/listagem")
    public void pesquisarPedidoByPeriodo(Date dataInicial, Date dataFinal, boolean isCompra, boolean isEntrega) {
        try {
            List<Pedido> listaPedido = null;
            if (isEntrega) {
                listaPedido = relatorioService.gerarRelatorioEntrega(new Periodo(dataInicial, dataFinal));
                addAtributo(
                        "tituloRelatorio",
                        "Rel�torio de Acompanhamento de Entregas de " + this.formatarData(dataInicial) + " � "
                                + this.formatarData(dataFinal));
            } else if (isCompra) {
                listaPedido = relatorioService.gerarRelatorioCompra(new Periodo(dataInicial, dataFinal));
                addAtributo("tituloRelatorio", "Rel�torio de Pedido de Compras de " + this.formatarData(dataInicial)
                        + " � " + this.formatarData(dataFinal));

            } else {
                listaPedido = relatorioService.gerarRelatorioVenda(new Periodo(dataInicial, dataFinal));
                addAtributo("tituloRelatorio", "Rel�torio de Pedido de Vendas de " + this.formatarData(dataInicial)
                        + " � " + this.formatarData(dataFinal));

            }

            for (Pedido pedido : listaPedido) {
                formatarPedido(pedido);
            }
            addAtributo("listaPedido", listaPedido);
            addAtributo("relatorioGerado", true);
        } catch (InformacaoInvalidaException e) {
            this.gerarListaMensagemErro(e);
        }

        addAtributo("dataInicial", formatarData(dataInicial));
        addAtributo("dataFinal", formatarData(dataFinal));
        redirecTo(this.getClass()).relatorioPedidoPeriodoHome(dataInicial, dataFinal, isCompra, isEntrega);
    }

    @Get("relatorio/pedido")
    public void relatorioPedidoPeriodoHome(Date dataInicial, Date dataFinal, boolean isCompra, boolean isEntrega) {
        if (isEntrega) {
            addAtributo("titulo", "Relat�rio de Entregas por Per�odo");
            addAtributo(
                    "tituloRelatorio",
                    "Rel�torio de Acompanhamento de Entregas de " + this.formatarData(dataInicial) + " � "
                            + this.formatarData(dataFinal));
        } else if (isCompra) {
            addAtributo("titulo", "Relat�rio de Compras por Per�odo");
            addAtributo("tituloRelatorio", "Rel�torio de Pedido de Compras de " + this.formatarData(dataInicial)
                    + " � " + this.formatarData(dataFinal));

        } else {
            addAtributo("titulo", "Relat�rio de Vendas por Per�odo");
            addAtributo("tituloRelatorio", "Rel�torio de Pedido de Vendas de " + this.formatarData(dataInicial) + " � "
                    + this.formatarData(dataFinal));

        }
        addAtributo("isCompra", isCompra);
        addAtributo("isEntrega", isEntrega);

        configurarFiltroPediodoMensal();
    }
}
