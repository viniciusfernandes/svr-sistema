package br.com.svr.vendas.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.svr.service.NFeService;
import br.com.svr.service.entity.NFePedido;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.wrapper.Periodo;
import br.com.svr.util.NumeroUtils;
import br.com.svr.util.StringUtils;
import br.com.svr.vendas.controller.anotacao.Servico;
import br.com.svr.vendas.relatorio.conversor.GeradorRelatorioPDF;

@Resource
public class RelatorioFaturamentoController extends AbstractController {

    @Servico
    private NFeService nFeService;

    public RelatorioFaturamentoController(Result result, GeradorRelatorioPDF geradorRelatorioPDF,
            HttpServletRequest request) {
        super(result, null, geradorRelatorioPDF, request);
    }

    @Get("relatorio/faturamento/pdf")
    public Download downloadRelatorioFaturamento(Date dataInicial, Date dataFinal) {
        try {
            List<NFePedido> lNfe = nFeService.pesquisarNFePedidoSaidaEmitidaByPeriodo(new Periodo(dataInicial,
                    dataFinal));
            double vlTotal = 0;
            double vlTotalICMS = 0;
            for (NFePedido n : lNfe) {
                n.setDataEmissaoFormatada(StringUtils.formatarData(n.getDataEmissao()));
                vlTotal += n.getValor() == null ? 0 : n.getValor();
                vlTotalICMS += n.getValorICMS() == null ? 0 : n.getValorICMS();
            }

            Collections.sort(lNfe, new Comparator<NFePedido>() {
                @Override
                public int compare(NFePedido o1, NFePedido o2) {
                    return o1.getNumero().compareTo(o2.getNumero());
                }
            });

            String dtInicial = StringUtils.formatarData(dataInicial);
            String dtFinal = StringUtils.formatarData(dataFinal);

            addAtributoPDF("dataInicial", dtInicial);
            addAtributoPDF("dataFinal", dtFinal);
            addAtributoPDF("vlTotal", NumeroUtils.arredondarValor2Decimais(vlTotal));
            addAtributoPDF("vlTotalICMS", NumeroUtils.arredondarValor2Decimais(vlTotalICMS));
            addAtributoPDF("listaNFe", lNfe);

            processarPDF("relatorioFaturamento.html");
            return gerarDownloadPDF(gerarPDF(), "Faturamento de " + dtInicial + " a " + dtFinal);
        } catch (BusinessException e) {
            gerarLogErro("gera��o do relat�rio de faturamento", e);
            return null;
        }
    }

    @Get("relatorio/faturamento")
    public void relatorioFaturamentoHome() {
        configurarFiltroPediodoMensal();
    }

}
