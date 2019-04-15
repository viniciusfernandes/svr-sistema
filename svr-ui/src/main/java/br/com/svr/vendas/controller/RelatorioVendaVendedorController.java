package br.com.svr.vendas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.svr.service.UsuarioService;
import br.com.svr.service.constante.TipoAcesso;
import br.com.svr.service.entity.Usuario;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.relatorio.RelatorioService;
import br.com.svr.service.wrapper.Periodo;
import br.com.svr.vendas.controller.anotacao.Servico;
import br.com.svr.vendas.json.SerializacaoJson;
import br.com.svr.vendas.login.UsuarioInfo;

@Resource
public class RelatorioVendaVendedorController extends AbstractController {

    @Servico
    private UsuarioService usuarioService;

    @Servico
    private RelatorioService relatorioService;

    public RelatorioVendaVendedorController(Result result, UsuarioInfo usuarioInfo) {
        super(result, usuarioInfo);
    }

    @Post("relatorio/venda/vendedor/listagem")
    public void gerarRelatorioPedidoVendedor(boolean orcamento, Date dataInicial, Date dataFinal, Integer idVendedor) {
        try {

            addAtributo("relatorio", relatorioService.gerarRelatorioVendaVendedor(orcamento, new Periodo(
                    dataInicial, dataFinal), idVendedor));
            irRodapePagina();
        } catch (BusinessException e) {
            gerarListaMensagemErro(e);
            irTopoPagina();
        }

        addAtributo("dataInicial", formatarData(dataInicial));
        addAtributo("dataFinal", formatarData(dataFinal));
        addAtributo("orcamento", orcamento);
        addAtributo("vendedor", this.usuarioService.pesquisarUsuarioResumidoById(idVendedor));
    }

    @Get("relatorio/venda/vendedor/nome")
    public void pesquisarVendedorByNome(String nome) {
        List<Autocomplete> lista = new ArrayList<Autocomplete>();
        List<Usuario> listaUsuario = this.usuarioService.pesquisarVendedorByNome(nome);
        for (Usuario usuario : listaUsuario) {
            lista.add(new Autocomplete(usuario.getId(), usuario.getNomeCompleto()));
        }
        serializarJson(new SerializacaoJson("lista", lista));
    }

    @Get("relatorio/venda/vendedor")
    public void relatorioVendaVendedorHome() {
        configurarFiltroPediodoMensal();
        addAtributo("relatorioGerado", contemAtributo("relatorio"));
        addAtributo("titulo", "Relat�rio de Venda/Or�amto do Vendedor");

        boolean acessoPesquisaVendaVendedorPermitido = isAcessoPermitido(TipoAcesso.ADMINISTRACAO,
                TipoAcesso.GERENCIA_VENDAS, TipoAcesso.OPERACAO_CONTABIL);
        addAtributo("acessoPesquisaVendaVendedorPermitido", acessoPesquisaVendaVendedorPermitido);
        /*
         * Caso o vendedor nao tenha permissao de administrador, etc, o campo de
         * vendedor sera desabilitado e para isso vamos preencher o campo com o
         * nome do vendedor que acessou a tela.
         */
        if (!acessoPesquisaVendaVendedorPermitido) {
            addAtributo("vendedor", usuarioService.pesquisarUsuarioResumidoById(getCodigoUsuario()));
        }
    }
}
