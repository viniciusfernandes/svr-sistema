<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">

$(document).ready(function(){
	var listaIdSelecionado=<c:out value="${not empty listaIdItemSelecionado ? listaIdItemSelecionado : \'new Array()\'}"/>;
	tabelaLinhaSelecionavelExt({
		idForm:'formPesquisa',
		idTabela:'tabelaListagemItemPedido',
		idBotaoLimpar:'botaoRemoverSelecaoItem',
		listaIdSelecionado: listaIdSelecionado
	});

	$('#botaoCopiarItem').click(function(){
		$('#formPesquisa').attr('action', '<c:url value="${orcamento ? \'orcamento\' : \'pedido\' }/copiaitem"/>').attr('method', 'post').submit();
	});
	
});

</script>
<a id="rodape"></a>
	<fieldset>
		<legend>::: Resultado da Pesquisa de Pedidos de ${isCompra ? 'Compra': 'Venda'} :::</legend>
		<div id="paginador"></div>
		<div class="bloco_botoes">
			<c:if test="${orcamento}">
				<input type="button" id="botaoAceitarListaItemOrcamento" title="Aceitar Itens Or�amento" value="" class="botaoAceitar"/>
			</c:if>
			<input type="button" id="botaoCopiarItem" title="Gerar Novo ${orcamento ? 'Or�amento': 'Pedido'}" value="" class="botaoCesta"/>
			<input type="button" id="botaoRemoverSelecaoItem" value="" title="Remover Sele��o Item" class="botaoLimpar" />
		</div>
		<div>
			<table id="tabelaListagemItemPedido" class="listrada">
			<thead>
				<tr>
					<th style="width: 10%">Situa�.</th>
					<th style="width: 8%">Ped./N. Cli.</th>
					<th style="width: 5%">Item</th>
					<th style="width: 5%">Qtde.</th>
					<th style="width: 35%">Descri��o</th>
					<th style="width: 5%">Venda</th>
					<th style="width: 5%">Pre�o (R$)</th>
					<th style="width: 5%">Unid. (R$)</th>
					<th style="width: 10%">Total (R$)</th>
					<th style="width: 5%">IPI (%)</th>
					<th style="width: 5%">ICMS (%)</th>
					<th>A��es</th>
				</tr>
			</thead>

			<tbody>
			
			<c:forEach items="${relatorioItemPedido.listaGrupo}" var="grupo" varStatus="iGrupo">
					<c:forEach items="${grupo.listaElemento}" var="item" varStatus="iElemento">
						<tr>
							<c:if test="${iElemento.count le 1}">
								<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}">${grupo.id.situacaoPedido.descricao}</td>
								<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}">${grupo.id.id}/<br></br>${grupo.id.numeroPedidoCliente}</td>
							</c:if>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${item.sequencial}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${item.quantidade}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${item.descricao}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" style="text-align: center;">${item.tipoVenda}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${item.precoVendaFormatado}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${item.precoUnidadeFormatado}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${item.precoItemFormatado}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${item.aliquotaIPIFormatado}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${item.aliquotaICMSFormatado}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">
								<div class="coluna_acoes_listagem">
									<form action="${orcamento? 'orcamento/pdf': 'pedido/pdf' }">
										<input type="hidden" name="tipoPedido" value="${grupo.id.tipoPedido}" /> 
										<input type="hidden" name="idPedido" value="${grupo.id.id}" />
										<div class="input" style="width: 35%">
											<input type="submit" value="" title="Visualizar Pedido PDF" class="botaoPdf_16 botaoPdf_16_centro" />
										</div>
									</form>
									<form action="${orcamento? 'orcamento/': 'pedido/' }${grupo.id.id}" method="get">
										<input type="hidden" name="tipoPedido" value="${grupo.id.tipoPedido}" /> 
										<input type="hidden" name="id" value="${grupo.id.id}" />
										<div class="input" style="width: 35%">
											<input type="submit" id="botaoEditarPedido" title="Editar Dados do Pedido" value="" class="botaoEditar" />
										</div>
									</form>
									<div class="input" style="width: 30%">
										<input type="checkbox" value="${item.id}" ${not empty idSelec[item.id]?'checked':''}/>
									</div>
								</div>
							</td>
						</tr>
					</c:forEach>
				</c:forEach>

			</tbody>
			
		</table>
			
		</div>
	</fieldset>