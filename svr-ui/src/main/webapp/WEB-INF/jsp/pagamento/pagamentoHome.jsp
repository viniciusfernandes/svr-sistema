<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE >
<html>
<head>

<jsp:include page="/bloco/bloco_css.jsp" />
<jsp:include page="/bloco/bloco_relatorio_css.jsp" />

<script type="text/javascript" src="<c:url value="/js/jquery-min.1.8.3.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/util.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/mascara.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modalConfirmacao.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/autocomplete.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.mask.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.10.3.datepicker.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.maskMoney.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.10.4.dialog.min.js"/>"></script>

<style type="text/css">
#tabelaPagamentos tr th, #tabelaPagamentos tr td{
	font-size: 12px;
} 
</style>
<script type="text/javascript">

$(document).ready(function() {
	$('html, body').animate({
	    scrollTop: ($('#${ancora}').first().offset().top)
	},0);
	
	$('#botaoInserirPagamento').click(function(){
		
		adicionarInputHiddenFormulario('formPagamento', 'dataInicial', $('#dataInicial').val());
		adicionarInputHiddenFormulario('formPagamento', 'dataFinal', $('#dataFinal').val());
		$('#formPagamento').attr('action', '<c:out value="pagamento/inclusao"/>').attr('method', 'post').submit();	
	});
	
	$('#botaoLimparPeriodo').click(function(){
		$(this).closest('form').attr('action', '<c:out value="pagamento"/>').attr('method', 'get').submit();	
	});
	
	$('#botaoPesquisarFornecedor').click(function(){
		var idForn = $('#idFornecedor').val();
		if(isEmpty(idForn)){
			return;
		}
		adicionarInputHiddenFormulario('formVazio', 'idFornecedor', idForn);
		adicionarInputHiddenFormulario('formVazio', 'dataInicial', $('#dataInicial').val());
		adicionarInputHiddenFormulario('formVazio', 'dataFinal', $('#dataFinal').val());
		$('#formVazio').attr('action', '<c:out value="pagamento/fornecedor/"/>'+idForn).attr('method', 'get').submit();	
	});
	
	$('#botaoPesquisarCompras').click(function(){
		var idForn = $('#idFornecedor').val();
		if(isEmpty(idForn)){
			return;
		}
		adicionarInputHiddenFormulario('formVazio', 'dataInicial', $('#dataInicial').val());
		adicionarInputHiddenFormulario('formVazio', 'dataFinal', $('#dataFinal').val());
		$('#formVazio').attr('action', '<c:out value="pagamento/compraefetivada/listagem/"/>'+idForn).attr('method', 'get').submit();	
	});
	
	$('#botaoPesquisarPedido').click(function(){
		var idPedido = $('#pedido').val();
		if(isEmpty(idPedido)){
			return;
		}
		adicionarInputHiddenFormulario('formVazio', 'idPedido', idPedido);
		adicionarInputHiddenFormulario('formVazio', 'dataInicial', $('#dataInicial').val());
		adicionarInputHiddenFormulario('formVazio', 'dataFinal', $('#dataFinal').val());
		$('#formVazio').attr('action', '<c:out value="pagamento/pedido/"/>'+idPedido).attr('method', 'get').submit();	
	});
	
	$('#botaoPesquisarNF').click(function(){
		var numeroNF = $('#numeroNF').val();
		if(isEmpty(numeroNF)){
			return;
		}
		adicionarInputHiddenFormulario('formVazio', 'numeroNF', numeroNF);
		adicionarInputHiddenFormulario('formVazio', 'dataInicial', $('#dataInicial').val());
		adicionarInputHiddenFormulario('formVazio', 'dataFinal', $('#dataFinal').val());
		$('#formVazio').attr('action', '<c:out value="pagamento/nf/"/>'+numeroNF).attr('method', 'get').submit();	
	});
	
});

function removerPagamento(botao){
	inicializarModalConfirmacao({
		mensagem: 'Voc� tem certeza de que deseja REMOVER esse item?',
		confirmar: function(){
			$(botao).closest('form').submit();
		}
	});
};
</script>

</head>
<body>

	<div id="modal"></div>
	
	<fieldset id="bloco_pesquisa">
			<legend>::: Pagamentos do Per�odo :::</legend>
			<form action="<c:url value="/pagamento/periodo/listagem"/>" method="get">
				<div class="label" style="width: 30%">Data In�cial:</div>
				<div class="input" style="width: 10%">
					<input type="text" id="dataInicial" name="dataInicial"
						value="${dataInicial}" maxlength="10" class="pesquisavel" />
				</div>
				<div class="label" style="width: 10%">Data Final:</div>
				<div class="input" style="width: 10%">
					<input type="text" id="dataFinal" name="dataFinal"
						value="${dataFinal}" maxlength="100" class="pesquisavel"
						style="width: 100%" />
				</div>
				<div class="input" style="width: 2%">
					<input type="submit" id="botaoPesquisarPagamentoPeriodo" title="Pesquisar Pagamentos por Per�odo" value="" class="botaoPesquisarPequeno" style="width: 100%"/>
				</div>
				<div class="input" style="width: 10%">
					<input type="button" id="botaoLimparPeriodo" value="" title="Limpar Pagamentos do Per�odo" class="botaoLimparPequeno" />
				</div>
			</form>
		</fieldset>
		
	<form id="formVazio"></form>
	
	<a id="rodape"></a>
	<jsp:include page="/bloco/bloco_edicao_pagamento.jsp"/>
<c:choose>
	<c:when test="${isPesquisaPagamento}">
		<jsp:include page="/bloco/bloco_listagem_pedido_compra.jsp"></jsp:include>
	</c:when>
	<c:when test="${not isPesquisaPagamento and not empty relatorio}">
		<jsp:include page="/bloco/bloco_mensagem.jsp" />
		<table id="tabelaPagamentos" class="listrada">
			<caption>${relatorio.titulo}</caption>
			<thead>
				<tr>
					<th style="width: 3%">Sit.</th>
					<th style="width: 7%">Venc.</th>
					<th style="width: 5%">NF</th>
					<th style="width: 5%">Ped.</th>
					<th style="width: 2%">Item</th>
					<th style="width: 31%">Desc.</th>
					<th style="width: 3%">Parc.</th>
					<th style="width: 8%">Forn.</th>
					<th style="width: 5%">ICMS(R$)</th>
					<th style="width: 7%">Val.(R$)</th>
					<th style="width: 9%">Item (R$)</th>
					<th style="width: 9%">NF (R$)</th>
					<th colspan="2" style="width: 7%">A��o</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${relatorio.listaGrupo}" var="grupo" varStatus="iGrupo">
					<c:forEach items="${grupo.listaElemento}" var="elemento" varStatus="iElemento">
						<tr id="${grupo.id}">
							<c:if test="${iElemento.count le 1}">
								<c:choose>
									<c:when test="${grupo.propriedades['liquidado']}">
										<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}" style="text-align: center"><div class="botaoVerificacaoEfetuadaGrande" title="Liquidado"></div></td>
									</c:when>
									<c:when test="${grupo.propriedades['vencido']}">
										<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}" style="text-align: center"><div class="botaoVerificacaoFalhaGrande" title="Vencido"></div></td>
									</c:when>
									<c:otherwise>
										<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}" style="text-align: center"><div class="botaoVerificacaoAguardadaGrande" title="Aguardando"></div></td>
									</c:otherwise>
								</c:choose>
								<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}" style="text-align: center">${grupo.propriedades['dataVencimento']}</td>
								<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}" style="text-align: center">${grupo.propriedades['numeroNF']}</td>
							</c:if>
							<td id="${elemento.id }" class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${elemento.idPedido}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${elemento.sequencialItem}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${elemento.descricao}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${elemento.parcelaFormatada}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${elemento.nomeFornecedor}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${elemento.valorCreditoICMS}</td>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}">${elemento.valor}</td>
							<c:if test="${iElemento.count le 1}">
								<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}" style="text-align: center">${grupo.propriedades['valorParcela']}</td>
								<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}" style="text-align: center">${grupo.propriedades['valorParcelaNF']}</td>
							</c:if>
							<%--BLOCO DE BOTOES PARA EDICAO E REMOCAO DOS PAGAMENTOS--%>
							<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" style="width: 5%">
								<div class="coluna_acoes_listagem">
								<div class="input" style="width: 50%">
									<form action="<c:url value="/pagamento/"/>${elemento.id}" >
										<input type="hidden" name="dataInicial" value="${dataInicial}"/>
										<input type="hidden" name="dataFinal" value="${dataFinal}"/>
										<input type="submit" value="" title="Editar Pagamento" class="botaoEditar"/>
									</form>
								</div>
								<div class="input" style="width: 50%">
									<form action="<c:url value="/pagamento/remocao/nfparcelada/"/>${elemento.id}" method="post">
										<input type="hidden" name="dataInicial" value="${dataInicial}"/>
										<input type="hidden" name="dataFinal" value="${dataFinal}"/>
										<input type="hidden" name="numeroNF" value="${elemento.numeroNF}"/>
										<input type="button" value="" title="Remover Pagamento" class="botaoRemover" onclick="removerPagamento(this);"/>
									</form>
								</div>
								</div>
							</td>
							<%--BLOCO DE BOTOES PARA LIQUIDACAO DOS PAGAMENTOS--%>
							<c:if test="${iElemento.count le 1}">
								<td class="fundo${iGrupo.index % 2 == 0 ? 1 : 2}" rowspan="${grupo.totalElemento}" style="width: 2%">
									<div class="coluna_acoes_listagem">
										<c:choose>
											<c:when test="${grupo.propriedades['insumo']}">
												<form action="<c:url value="/pagamento/liquidacao/nfparcelada"/>" method="post" >
													<input type="hidden" name="idGrupo" value="${grupo.id}"/>
													<input type="hidden" name="dataInicial" value="${dataInicial}"/>
													<input type="hidden" name="dataFinal" value="${dataFinal}"/>
													<input type="hidden" name="numeroNF" value="${elemento.numeroNF}"/>
													<input type="hidden" name="idFornecedor" value="${elemento.idFornecedor}"/>
													<input type="hidden" name="nomeFornecedor" value="${elemento.nomeFornecedor}"/>
													<input type="hidden" name="parcela" value="${elemento.parcela}"/>
													<c:choose>
														<c:when test="${not grupo.propriedades['liquidado']}">
															<input type="hidden" name="liquidado" value="true"/>
															<input type="submit" value="" title="Liquidar Pagamento" class="botaoVerificacaoEfetuadaPequeno" />
														</c:when>
														<c:otherwise>
															<input type="hidden" name="liquidado" value="false"/>
															<input type="submit" value="" title="Retornar Liquida��o Pagamento" class="botaoVerificacaoFalhaPequeno" />
														</c:otherwise>	
													</c:choose>
												</form>
											</c:when>
											<c:otherwise>
												<form action="<c:url value="/pagamento/liquidacao/${elemento.id}"/>" method="post" >
													<input type="hidden" name="idGrupo" value="${grupo.id}"/>
													<input type="hidden" name="dataInicial" value="${dataInicial}"/>
													<input type="hidden" name="dataFinal" value="${dataFinal}"/>
													<c:choose>
														<c:when test="${not grupo.propriedades['liquidado']}">
															<input type="hidden" name="liquidado" value="true"/>
															<input type="submit" value="" title="Liquidar Pagamento" class="botaoVerificacaoEfetuadaPequeno" />
														</c:when>
															<c:otherwise>
															<input type="hidden" name="liquidado" value="false"/>
															<input type="submit" value="" title="Retornar Liquida��o Pagamento" class="botaoVerificacaoFalhaPequeno" />
														</c:otherwise>	
													</c:choose>
												</form>
											</c:otherwise>
										</c:choose>
									</div>
								</td>
							</c:if>
						</tr>
					</c:forEach>
				</c:forEach>
				
			</tbody>
			<tfoot>
				<tr>
					<td colspan="6" style="text-align: right;">QTDE.:</td>
					<td colspan="8"><div style="text-align: left;">${relatorio.propriedades['qtde']}</div></td>
				</tr>
				<tr>
					<td colspan="6" style="text-align: right;">TOTAL:</td>
					<td colspan="8"><div style="text-align: left;">R$ ${relatorio.propriedades['tot']}</div></td>
				</tr>
				<tr>
					<td colspan="6" style="text-align: right;">CRED. ICMS:</td>
					<td colspan="8"><div style="text-align: left;">R$ ${relatorio.propriedades['totCredICMS']}</div></td>
				</tr>
			</tfoot>
		</table>
	</c:when>
</c:choose>
	
		
</body>
</html>