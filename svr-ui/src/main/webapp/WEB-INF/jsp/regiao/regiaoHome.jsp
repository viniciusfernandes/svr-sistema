<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html>
<head>
<meta charset="utf-8">

<jsp:include page="/bloco/bloco_css.jsp" />

<script type="text/javascript" src="<c:url value="/js/jquery-min.1.8.3.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/util.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.paginate.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/picklist.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/util.js"/>"></script>

<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.10.4.dialog.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modalConfirmacao.js"/>"></script>

<script type="text/javascript">

$(document).ready(function() {
	scrollTo('${ancora}');
	
	$("#botaoPesquisarRegiao").click(function() {
		toUpperCaseInput();
		inicializarFiltro();
		$('#formPesquisa').submit();
	});

	$('#botaoInserirRegiao').click(function(){
		toUpperCaseInput();
		var parametros = pickListToParameter('listaIdBairroAssociado');
		$('#formRegiao').attr("action",$('#formRegiao').attr("action")+'?'+ parametros);
		$('#formRegiao').submit();
	});
	
	inicializarCampoPesquisaPicklist({
		url: '<c:url value="regiao/bairro/listagem"/>', 
		mensagemEspera: 'CARREGANDO OS BAIRROS ...',
		parametro: 'cep'
	});

	new PickList().initPickList();

	<jsp:include page="/bloco/bloco_paginador.jsp" />
});


function inicializarFiltro() {
	$("#formPesquisa #filtroNome").val($("#nome").val());
}

function inicializarPesquisaBairro() {
	$('#campoPesquisaPicklist').attr('maxlength', 8);
	
	$('#botaoPesquisaPicklist').click(function () {
		var cep = $('#campoPesquisaPicklist').val(); 

		/*
		* Vamos disparar a pesquisa apenas quando o CEP tiver mais de 1 digitos
		* pois com 1 digitos cobrimos um numero de cidades muito grande
		*/
		if (cep == undefined || cep == null || cep.trim().length <= 1) {
			return;
		}
		/*
		* Vamos limpar o select e inserir uma informacao de que a requisicao estaem andamento
		* pois em alguns casos o resultado da consulta eh demorado
		*/
		var selectlist = document.getElementById('SelectList');
		selectlist.innerHTML = '';
		selectlist.add(new Option('CARREGANDO OS BAIRROS...', -1, false, false));
		var request = $.ajax({
							type: "get",
							url: '<c:url value="regiao/bairro/listagem"/>',
							data: 'cep='+cep,
						});
		request.done(function(response) {
			selectlist.innerHTML = '';
			var listaBairro = response.listaBairro;
			var totalBairro = listaBairro.length;
			var bairro = null;
			for(var i =0; i < totalBairro; i++) {
				bairro = listaBairro[i];	
				selectlist.add(new Option(bairro.descricao, bairro.idBairro, false, false));
			}
		});
		
		request.fail(function(request, status) {
			gerarListaMensagemErro(new Array('Falha na busca do bairro para o CEP: ' + $('#cep').val()+' => Status da requisicao: '+status))
		});
	});
}


function inicializarModalCancelamento(botao){
	inicializarModalConfirmacao({
		mensagem: 'Essa a��o n�o poder� ser� desfeita. Voc� tem certeza de que deseja REMOVER essa regi�o?',
		confirmar: function(){
			$(botao).closest('form').submit();	
		}
	});
}

</script>

</head>
<body>
	<jsp:include page="/bloco/bloco_mensagem.jsp" />
	<div id="modal"></div>
	<form id="formPesquisa" action="<c:url value="/regiao/listagem"/>" method="get">
		<input type="hidden" id="filtroNome" name="filtro.nome" />
	</form>

	<form id="formVazio" action="<c:url value="/regiao"/>" method="get"></form>

	<form id="formRegiao" action="<c:url value="/regiao/inclusao"/>"
		method="post">
		<input type="hidden" id="codigo" name="regiao.id" value="${regiao.id}">
		<fieldset>
			<legend>::: Dados da Regi�o :::</legend>
			<div class="label obrigatorio">Regi�o:</div>
			<div class="input" style="width: 40%">
				<input type="text" id="nome" name="regiao.nome"
					value="${regiao.nome}" class="pesquisavel" />
			</div>
		</fieldset>
		<div class="bloco_botoes">
			<a id="botaoPesquisarRegiao" title="Pesquisar Dados da Regi�o"
				class="botaoPesquisar"></a> <a id="botaoLimpar"
				title="Limpar Dados da Regi�o" onclick="limparFormulario();"
				class="botaoLimpar"></a>
		</div>

		<jsp:include page="/bloco/bloco_picklist.jsp" />

		<div class="bloco_botoes">
			<c:if test="${acessoCadastroBasicoPermitido}">
				<input type="button" id="botaoInserirRegiao"
					title="Incluir Dados da Regi�o" class="botaoInserir" />
			</c:if>
		</div>
	</form>

	<a id="rodape"></a>
	<fieldset>
		<legend>::: Resultado da Pesquisa de Regi�es :::</legend>
		<div id="paginador"></div>
		<div>
			<table class="listrada">
				<thead>
					<tr>
						<th style="width: 90%">Regi�o</th>
						<th>A��es</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="regiao" items="${listaRegiao}">
						<tr>
							<td>${regiao.nome}</td>
							<td>
								<div class="coluna_acoes_listagem">
									<form action="<c:url value="/regiao/"/>${regiao.id}"
										method="get">
										<input type="submit" title="Editar Dados da Regi�o" value=""
											class="botaoEditar" />
									</form>
									<c:if test="${acessoCadastroBasicoPermitido}">
										<form action="<c:url value="/regiao/remocao"/>" method="post">
											<input type="hidden" name="idRegiao" value="${regiao.id}">
											<input type="button" id="botaoRemoverRegiao"
												title="Remover Regi�o" value="" class="botaoRemover"
												onclick="inicializarModalCancelamento(this);"/>
										</form>
									</c:if>
								</div>
							</td>
						</tr>

					</c:forEach>
				</tbody>

			</table>
		</div>
	</fieldset>
</body>
</html>
