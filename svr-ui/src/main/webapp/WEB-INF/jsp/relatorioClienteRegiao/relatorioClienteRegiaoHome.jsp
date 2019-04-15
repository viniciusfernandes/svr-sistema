<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html>
<head>
<meta charset="utf-8">

<jsp:include page="/bloco/bloco_css.jsp" />
<jsp:include page="/bloco/bloco_relatorio_css.jsp" />


<script type="text/javascript" src="<c:url value="/js/jquery-min.1.8.3.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.paginate.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/autocomplete.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/util.js"/>"></script>
<style type="text/css">
.listrada {
	margin-left: 10%;
	margin-right: 10%;
	width: 80%;
}
</style>
<script type="text/javascript">

$(document).ready(function() {
	scrollTo('${ancora}');

	inicializarAutomcompleteRegiao();
});

function inicializarAutomcompleteRegiao () {
	autocompletar (
			{
				url: '<c:url value="/relatorio/cliente/regiao/listagem"/>',
				campoPesquisavel: 'nome', 
				parametro: 'nome', 
				containerResultados: 'containerPesquisaRegiao',
				selecionarItem: function (itemLista){
					$('#formPesquisa #idRegiao').val(itemLista.id);	
					$('#formPesquisa #nomeRegiao').val(itemLista.innerHTML);					
				}
			}
	);
};

</script>

</head>
<body>
	<jsp:include page="/bloco/bloco_mensagem.jsp" />

	<fieldset>
		<legend>::: Dados do Relat�rio dos Clientes da Regi�o :::</legend>
		<div class="label obrigatorio">Regi�o:</div>
		<div class="input" style="width: 40%">
			<input type="text" id="nome" name="regiao.nome" value="${regiao.nome}" class="pesquisavel" />
			<div class="suggestionsBox" id="containerPesquisaRegiao" style="display: none; width: 50%"></div>
		</div>
	</fieldset>
	<div class="bloco_botoes">
		<form id="formPesquisa" action="<c:url value="/relatorio/cliente/regiao/listagem/cliente"/>" method="get">
			<input type="hidden" id="idRegiao" name="regiao.id" value="${regiao.id}"/>
			<input type="hidden" id="nomeRegiao" name="regiao.nome" value="${regiao.nome}"/> 
			<input type="submit" value="" title="Pesquisar Clientes da Regi�o" class="botaoPesquisar" />
		</form>
		<form action="<c:url value="/relatorio/cliente/regiao"/>" method="get">
			<input type="submit" value=""
				title="Limpar Dados do Relat�rio de Clientes da Regi�o"
				class="botaoLimpar" />
		</form>

	</div>

	<a id="rodape"></a>
	<c:if test="${relatorioGerado}">
		<table class="listrada">
			<caption>${tituloRelatorio}</caption>
			<thead>
				<tr>
					<th style="width: 15%">Cliente</th>
					<th style="width: 30%%">Contato</th>
					<th style="width: 50%">Endere�o</th>
					<th style="width: 5%">A��es</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach var="cliente" items="${listaCliente}">
					<tr>
						<td>${cliente.nomeFantasia}</td>
						<c:choose>
							<c:when test="${cliente.listaContatoPreenchida}">
								<td>${cliente.contatoPrincipal.nome}-
									${cliente.contatoPrincipal.telefoneFormatado}</td>
							</c:when>
							<c:otherwise>
								<td></td>
							</c:otherwise>
						</c:choose>
						<td>${cliente.logradouroFaturamento.descricao}</td>
						<td>
							<div class="coluna_acoes_listagem">
								<form action="<c:url value="/cliente/"/>${cliente.id}"
									method="get">
									<input type="submit" title="Visualizar Dados do Cliente"
										value="" class="botaoEditar" />
								</form>
							</div>
						</td>
					</tr>

				</c:forEach>
			</tbody>

		</table>
	</c:if>
</body>
</html>
