<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/bloco/bloco_css.jsp" />
<jsp:include page="/bloco/bloco_relatorio_css.jsp" />

<script type="text/javascript" src="<c:url value="/js/jquery-min.1.8.3.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/util.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.paginate.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/mascara.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/modalConfirmacao.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/tabela_handler.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/logradouro.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/bloco/contato.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/autocomplete.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.mask.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.10.3.datepicker.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/pedido/pedido.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/pedido/bloco_item_pedido.js?${versaoCache}"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.maskMoney.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.10.4.dialog.min.js"/>"></script>

<style type="text/css">
.listrada td:last-child form input:first-child {
	margin-left: 8%;
}
</style>

<script type="text/javascript">

$(document).ready(function() {
	scrollTo('${ancora}');

	var tabelaItemHandler = gerarTabelaItemPedido('<c:url value="/pedido"/>')
	inicializarAutocompleteMaterial('<c:url value="/pedido/material"/>');
	// inicializarAutocompleteDescricaoPeca('<c:url value="/estoque/descricaopeca"/>');
	
	$("#botaoInserirPedido").click(function() {
		inserirPedido({'urlInclusao':'<c:url value="/pedido/inclusao"/>'});
	});
	 
	$("#botaoInserirItemPedido").click(function() {
		inserirPedido({
			'urlInclusao':'<c:url value="/pedido/inclusao"/>', 
			'inserirItem':inserirItemPedido, 
			'urlInclusaoItem':'<c:url value="/pedido/item/inclusao"/>'
			});
	});

	$("#botaoPesquisaNumeroPedido").click(function() {
		var numeroPedido = $('#numeroPedidoPesquisa').val();
		if (isEmpty(numeroPedido)) {
			gerarListaMensagemAlerta(new Array('O número do pedido é obrigatório para a pesquisa'));
			return;
		} 
		var form = $('#formVazio'); 
		form.attr('action', '<c:url value="/pedido/'+numeroPedido+'?tipoPedido=${tipoPedido}"/>');
		form.submit();
		
	});
	
	$("#botaoCopiarPedido").click(function() {
		var numeroPedido = $('#numeroPedidoPesquisa').val();
		if (isEmpty(numeroPedido)) {
			gerarListaMensagemAlerta(new Array('O número do pedido é obrigatório para copiar o pedido'));
			return;
		} 
		var form = $('#formVazio'); 
		form.attr('method', 'post').attr('action', '<c:url value="/pedido/copia/'+numeroPedido+'?tipoPedido=${tipoPedido}"/>');
		form.submit();
		
	});
	
	$("#botaoLimparNumeroPedido").click(function() {
		 $('#formLimparPedido').submit();
	});
	
	<%--
	$("#representada").change(function() {
		habilitarIPI('<c:url value="/pedido"/>', $(this).val());	
	});
	--%>
	
	$("#botaoImpressaoPedido").click(function() {
		var numeroPedido = $('#numeroPedido').val();
		if (isEmpty(numeroPedido)) {
			gerarListaMensagemAlerta(new Array('O pedido não pode ser impresso pois não existe no sistema'));
			return;
		} 

		$('#idPedidoImpressao').val($('#numeroPedido').val());
		var form = $(this).closest('form'); 
		form.submit();
		
	});
	
	$("#botaoPesquisaPedido").click(function() {
		$('#formPesquisa #tipoPedidoPesquisa').val($('#formPedido #tipoPedido').val());
		$('#formPesquisa #idFornecedorPesquisa').val($('#formPedido #representada').val());
		$('#formPesquisa #idClientePesquisa').val($('#formPedido #idCliente').val());
		$('#formPesquisa #idVendedorPesquisa').val($('#formPedido #idVendedor').val());
		var form = $(this).closest('form'); 
		form.submit();
	});
	
	<c:if test="${acessoCompraPermitido}">
	$("#pedidoAssociado").change(function() {
		if(isEmpty($(this).val())){
			return;
		}
		var f = document.createElement('FORM');
		f.method='GET';
		f.action='<c:url value="/pedidoassociado/pdf"/>';
		f.id = 'formPDFAssociado';
		document.body.appendChild(f);

		adicionarInputHiddenFormulario('formPDFAssociado', 'tipoPedido', '<c:out value="${tipoPedidoAssociado}"/>');
		adicionarInputHiddenFormulario('formPDFAssociado', 'idPedido', $(this).val());
		f.submit();
	});
	</c:if>
	
	$('#botaoEditarCliente').click(function(){
		var id = $('#formPedido #idCliente').val();
		var url = null;
		if(!isEmpty(id)){
			url='<c:url value="/cliente/"/>'+id;
		} else {
			url='<c:url value="/cliente"/>';
		}
		$('#formVazio').attr('action', url).submit();
	});
	
	$('#listaNumeroNFe').change(function(){
		adicionarInputHiddenFormulario('formVazio', 'numeroNFe', $(this).val());
		$('#formVazio').attr('action', '<c:url value="/pedido/nfe"/>').submit();
	});
	
	inserirMascaraData('dataEntrega');
	inserirMascaraCNPJ('cnpj');
	inserirMascaraCPF('cpf');
	inserirMascaraInscricaoEstadual('inscricaoEstadual');
	inserirMascaraNumerica('numeroPedidoPesquisa', '9999999');
	inserirMascaraMonetaria('fretePedido', 7);
	inserirMascaraNumerica('validade', '999');
	inserirMascaraNumerica('prazoEntrega', '999');
	
	<jsp:include page="/bloco/bloco_paginador.jsp" />
	
	inicializarAutocompleteCliente('<c:url value="/pedido/cliente"/>', function(cliente){
		$('#idCliente').val(cliente.id);
		$('#formPesquisa #idClientePesquisa').val(cliente.id);
		$('#site').val(cliente.site);
		$('#email').val(cliente.email);
		$('#cnpj').val(cliente.cnpj);
		$('#cpf').val(cliente.cpf);
		$('#nomeCliente').val(cliente.nomeCompleto);
		$('#idVendedor').val(cliente.vendedor.id);
		$('#suframa').val(cliente.suframa);
		$('#proprietario').val(cliente.vendedor.nome + ' - '+ cliente.vendedor.email);
		$('#logradouroFaturamento').val(cliente.logradouroFormatado);
		limparComboBox('listaTransportadora');
		limparComboBox('listaRedespacho');

		var comboTransportadora = document.getElementById('listaTransportadora');
		var comboRedespacho = document.getElementById('listaRedespacho');

		preencherComboTransportadora(comboTransportadora, cliente.listaTransportadora);
		preencherComboTransportadora(comboRedespacho, cliente.listaRedespacho);
	});
	
	inicializarAutocompleteContatoCliente('<c:url value="/pedido/contatocliente"/>', 'contato_nome', function(contato){
		$('#contato_idContato').val(contato.id);
		$('#contato_nome').val(contato.nome);
		$('#contato_departamento').val(contato.departamento);
		$('#contato_email').val(contato.email);
		$('#contato_ddi').val(contato.ddi);
		$('#contato_ddd').val(contato.ddd);
		$('#contato_dddSecundario').val(contato.dddSecundario);
		$('#contato_telefone').val(contato.telefone);
		$('#contato_telefoneSecundario').val(contato.telefoneSecundario);
		$('#contato_ramal').val(contato.ramal);
		$('#contato_fax').val(contato.fax);
	});
	
	
	<%--Desabilitando toda a tela de pedidos --%>
	<c:if test="${pedidoDesabilitado}">
		$('input[type=text], select:not(.semprehabilitado), textarea').attr('disabled', true).addClass('desabilitado');
	</c:if>

	<c:if test="${empty pedido.id}">
		$('#formEnvioPedido #botaoEnviarPedido').hide();
	</c:if>

	$('#material').focus(function() {
		if($('#representada').val() == '') {
			gerarListaMensagemAlerta(new Array('Escolha uma representada antes de selecionar o material'));
		}
	});

	// A segunda condicao verifica quando o pedido eh do tipo de compra
	habilitar('#nomeCliente', <c:out value="${empty pedido.id and not isCompra}"/>);
	habilitar('#numeroPedidoPesquisa', <c:out value="${empty pedido.id}"/>);
	habilitar('#representada', <c:out value="${empty pedido.id or not contemItem}"/>);
	habilitar('#bloco_item_pedido #ipi', <c:out value="${not ipiDesabilitado}"/>);
	habilitar('#idRepresentada', <c:out value="${not empty pedido.id and contemItem}"/>);
	
	$('#botaoRefazerPedido').click(function (){
		inicializarModalConfirmacao({
			mensagem: 'Essa ação não poderá ser desfeita. Você tem certeza de que deseja REFAZER esse pedido?',
			confirmar: function(){
				$('#botaoRefazerPedido').closest('form').submit();	
			}
		});
	});	
	
	$('#botaoCancelarPedido').click(function (){
		inicializarModalConfirmacao({
			mensagem: 'Essa ação não poderá ser desfeita. Você tem certeza de que deseja CANCELAR esse pedido?',
			confirmar: function(){
				$('#botaoCancelarPedido').closest('form').submit();	
			}
		});
	});
	
	$('#botaoEnviarPedido').click(function (){
		var ok = false;
		inicializarModalConfirmacao({
			mensagem: 'Essa ação não poderá ser desfeita. Você tem certeza de que deseja ENVIAR esse pedido?',
			confirmar: function(){
				var tipoPedido = $('#tipoPedido').val();
				var idPedido = $('#numeroPedido').val();
				
				inserirPedido({
					urlInclusao:'<c:url value="/pedido/inclusao"/>', 
					enviar:	function(){
						var enviarPedido = $.ajax({
						      url: '<c:url value="/pedido/envio"/>',
						      type: 'post',
						      data: {'tipoPedido':tipoPedido, 'idPedido': idPedido},
						    });
						enviarPedido.done(function (response){
							if(ok =(response.sucesso != undefined && response.sucesso != null)){
								limparTela();
								gerarListaMensagemSucesso(response.sucesso);
							}else {
								gerarListaMensagemErro(response.erros);
							}
						});
						enviarPedido.always(function(response){
							if(!ok){
								return;
							}
							document.getElementById('tipoPedido').value = tipoPedido;
							habilitar('#numeroPedidoPesquisa', true);

							$('#divPedAssocLabel, #divPedAssocInput, #divNumNFeLabel, #divNumNFeInput').remove();
							
							var req = $.ajax({
							      url: '<c:url value="/pedido/clienteusuario"/>',
							      type: 'get',
							      data:{tipoPedido:tipoPedido}
							    });
							req.done(function(resp){
								var cli = resp.cliente;
								if(cli.id != undefined){
									document.getElementById('nomeCliente').value = cli.nomeFantasia+' - '+cli.razaoSocial;
									document.getElementById('idCliente').value = cli.id;
								} else {
									habilitar('#nomeCliente', true);
								}
								document.getElementById('idVendedor').value = cli.vendedor.id;
								document.getElementById('proprietario').value = cli.vendedor.nome+' - '+cli.vendedor.email;
							});
						});
						enviarPedido.fail(function(request, status, erro){
							gerarListaMensagemSucesso(["Falha no envio do pedido No. "+idPedido+"."]);
						}); 
					}// fim do callback de envio do pedido
				});
			}
		});
	});
	
	$('#botaoAceitarOrcamento').click(function (){
		if(isEmpty($('#formEnvioPedido #idPedido').val())){
			return;
		}
		inicializarModalConfirmacao({
			mensagem: 'Essa ação não poderá ser desfeita. Você tem certeza de que deseja ACEITAR esse orçamento para os pedidos?',
			confirmar: function(){
				var form = $('#botaoAceitarOrcamento').closest('form');
				$(form).attr('action', '<c:url value="pedido/aceiteorcamento"/>').submit();
			}
		});
	});
});
</script>

</head>
<body>
	<jsp:include page="/bloco/bloco_mensagem.jsp" />
	<div id="modal"></div>

	<form id="formVazio" action="pedido" method="get">
		<input type="hidden" name="orcamento" value="${empty orcamento ? false : orcamento}" />
		<input type="hidden" name="tipoPedido" value="${tipoPedido}" />
	</form>

	<form id="formPedido" action="<c:url value="/pedido/inclusao"/>" method="post">
		<fieldset>
			<legend>::: Dados do ${orcamento ? 'Orçamento': 'Pedido'} de ${isCompra ? 'Compra': 'Venda'} :::</legend>

			<!-- O campo id do pedido eh hidden pois o input text nao eh enviado na edicao do formulario pois esta "disabled" -->
			<input type="hidden" id="numeroPedido" name="pedido.id" value="${pedido.id}" /> 
			<input type="hidden" id="idCliente" name="pedido.cliente.id" value="${cliente.id}" /> 
			<input type="hidden" id="idVendedor" name="pedido.proprietario.id" value="${proprietario.id}" /> 
			<input type="hidden" id="idRepresentada" name="pedido.representada.id" value="${idRepresentadaSelecionada}" />
			<input type="hidden" id="tipoPedido" name="pedido.tipoPedido" value="${tipoPedido}" />
			<input type="hidden" id="orcamento" name="orcamento" value="${empty orcamento ? false : orcamento}" />
			<input type="hidden" id="situacaoPedido" name="pedido.situacaoPedido" value="${pedido.situacaoPedido}"/>
			
			<c:if test="${not empty pedido.id}">
			<div id="divPedAssocLabel" class="label">Pedido(s) de ${isCompra ? 'Venda:': 'Compra:'}</div>
			<div id="divPedAssocInput"  class="input" style="width: 10%">
				<select id="pedidoAssociado" name="idPedidoAssociado" style="width: 100%" class="semprehabilitado">
					<option value=""></option>
					<c:forEach var="idPedidoAssociado" items="${listaIdPedidoAssociado}">
						<option value="${idPedidoAssociado}">${idPedidoAssociado}</option>
					</c:forEach>
				</select>
			</div>
			<div id="divNumNFeLabel" class="label">Núm. NFe(s):</div>
			<div id="divNumNFeInput" class="input" style="width: 50%">
				<select id="listaNumeroNFe" name="numeroNFe" style="width: 25%" class="semprehabilitado">
					<option value="">&lt&lt SELECIONE &gt&gt</option>
					<c:forEach var="numeroNFe" items="${listaNumeroNFe}">
						<option value="${numeroNFe}">${numeroNFe}</option>
					</c:forEach>
				</select>
			</div>
			</c:if>
			
			<div class="label">${isCompra ? 'Comprador:': 'Vendedor:'}</div>
			<div class="input" style="width: 40%">
				<input type="text" id="proprietario" name="proprietario.nome"
					value="${proprietario.nome} - ${proprietario.email}" disabled="disabled"
					class="uppercaseBloqueado desabilitado" style="width: 95%"/>
			</div>
			<div class="label" style="width: 10%">Situação:</div>
			<div class="input" style="width: 20%">
				<input type="text" name="pedido.situacaoPedido" 
					value="${pedido.situacaoPedido}" class="desabilitado" disabled="disabled" width="95%"/>
			</div>
			
			<div class="label">Número:</div>
			<div class="input" style="width: 10%">
				<input type="text" id="numeroPedidoPesquisa" value="${pedido.id}"
					class="pesquisavel" />
			</div>
			<div class="input" style="width: 2%">
				<input type="button" id="botaoPesquisaNumeroPedido"
					title="Pesquisar Pedido" value="" class="botaoPesquisarPequeno" />
			</div>
			<div class="input" style="width: 2%">
				<input type="button" id="botaoLimparNumeroPedido"
					title="Limpar Pedido" value="" class="botaoLimparPequeno" />
			</div>
			<div class="input" style="width: 3%">
				<input type="button" id="botaoCopiarPedido"
					title="Copiar Pedido" value="" class="botaoCopiarPequeno" />
			</div>
			
			<div class="label" style="width: 10%">Nr. Pedido Cliente:</div>
			<div class="input" style="width: 10%">
				<input type="text" id="numeroPedidoCliente"
					name="pedido.numeroPedidoCliente"
					value="${pedido.numeroPedidoCliente}" style="width: 100%" />
			</div>
			<div class="label" style="width: 12%">Pagamento:</div>
			<div class="input" style="width: 20%">
				<input type="text" id="formaPagamento" name="pedido.formaPagamento"
					value="${pedido.formaPagamento}" style="width: 100%" />
			</div>
			<div class="label">
				<label>Data Entrega:</label>
			</div>
			<div class="input" style="width: 10%">
				<input type="text" id="dataEntrega" name="pedido.dataEntrega"
					value="${pedido.dataEntregaFormatada}" />
			</div>
			<div class="label" style="width: 17%">Prazo Entrega:</div>
			<div class="input" style="width: 10%">
				<input type="text" id="prazoEntrega" name="pedido.prazoEntrega" value="${pedido.prazoEntrega}"/>
			</div>
			<div class="label" style="width: 12%">Data Envio:</div>
			<div class="input" style="width: 20%">
				<input type="text" id="dataEnvio" value="${pedido.dataEnvioFormatada}" readonly="readonly"
					class="desabilitado" style="width: 100%" />
			</div>
			<div class="label obrigatorio">Cliente:</div>
			<div class="input" style="width: 38%">
				<input type="text" id="nomeCliente" value="${cliente.nomeCompleto}" class="pesquisavel" style="width: 100%"/>
				<div class="suggestionsBox" id="containerPesquisaCliente" style="display: none; width: 50%"></div>
			</div>
			<div class="input" style="width: 2%">
				<input type="button" id="botaoEditarCliente"
					title="Editar Cliente" value="" class="botaoEditar" />
			</div>
			<div class="label" style="width: 10%">Env. Email Ped.:</div>
			<div class="input" style="width: 30%">
				<input type="checkbox" id="clienteNotificadoVenda"
					name="pedido.clienteNotificadoVenda"
					<c:if test="${pedido.clienteNotificadoVenda}">checked</c:if>
					class="checkbox" style="width: 4%"/>
			</div>
			<div class="label">End. Faturam.</div>
			<div class="input" style="width: 38%">
				<input type="text" id="logradouroFaturamento"
					value="${logradouroFaturamento}" disabled="disabled" class="uppercaseBloqueado desabilitado"/>
			</div>
			<div class="label" style="width: 12%">Email:</div>
			<div class="input" style="width: 20%">
				<input type="text" id="email" name="cliente.email"
					value="${cliente.email}" style="width: 100%"
					class="uppercaseBloqueado desabilitado" disabled="disabled" />
			</div>
			<div class="label">CNPJ:</div>
			<div class="input" style="width: 15%">
				<input type="text" id="cnpj" name="cliente.cnpj" value="${cliente.cnpj}" class="desabilitado" disabled="disabled" />
			</div>
			<div class="label" style="width: 7%">CPF:</div>
			<div class="input" style="width: 15%">
				<input type="text" id="cpf" name="cliente.cpf" value="${cliente.cpf}" class="desabilitado" disabled="disabled" />
			</div>
			<div class="label" style="width: 12%">SUFRAMA:</div>
			<div class="input" style="width: 20%">
				<input type="text" id="suframa" name="cliente.inscricaoSUFRAMA" value="${cliente.inscricaoSUFRAMA}" 
					class="uppercaseBloqueado desabilitado" disabled="disabled" />
			</div>
			<div class="label obrigatorio" >${not empty tipoPedido ? 'Fornecedor:': 'Representada:'}</div>
			<div class="input" style="width: 15%">
				<select id="representada" name="pedido.representada.id" style="width: 100%">
					<option value="">&lt&lt SELECIONE &gt&gt</option>
					<c:forEach var="representada" items="${listaRepresentada}">
						<option value="${representada.id}"
							<c:if test="${representada.id eq idRepresentadaSelecionada}">selected</c:if>>${representada.nomeFantasia}</option>
					</c:forEach>
				</select>
			</div>
			<div class="label obrigatorio" style="width: 7%">Finalidade:</div>
			<div class="input" style="width: 60%">
				<select id="finalidadePedido" name="pedido.finalidadePedido" style="width: 25%" >
					<option value=""></option>
					<c:forEach var="tipo" items="${listaTipoFinalidadePedido}">
						<option value="${tipo}" 
							<c:if test="${tipo eq pedido.finalidadePedido}">selected</c:if>>${tipo.descricao}</option>
					</c:forEach>
				</select>
			</div>
			<div class="label">Transportadora:</div>
			<div class="input" style="width: 15%">
				<select id="listaTransportadora" name="pedido.transportadora.id" style="width: 100%">
					<option value="">&lt&lt SELECIONE &gt&gt</option>
					<c:forEach var="transportadora" items="${listaTransportadora}">
						<option value="${transportadora.id}"
							<c:if test="${transportadora.id eq pedido.transportadora.id}">selected</c:if>>${transportadora.nomeFantasia}</option>
					</c:forEach>
				</select>
			</div>
			<div class="label" style="width: 7%">Redesp.:</div>
			<div class="input" style="width: 60%">
				<select id="listaRedespacho" name="pedido.transportadoraRedespacho.id" style="width: 25%">
					<option value="">&lt&lt SELECIONE &gt&gt</option>
					<c:forEach var="redespacho" items="${listaRedespacho}">
						<option value="${redespacho.id}"
							<c:if test="${redespacho.id eq pedido.transportadoraRedespacho.id}">selected</c:if>>${redespacho.nomeFantasia}</option>
					</c:forEach>
				</select>
			</div>
			<div class="label">Tipo Entrega:</div>
			<div class="input" style="width: 15%">
				<select id="tipoEntrega" name="pedido.tipoEntrega"
					style="width: 100%">
					<option value="">&lt&lt SELECIONE &gt&gt</option>
					<c:forEach var="tipoEntrega" items="${listaTipoEntrega}">
						<option value="${tipoEntrega}"
							<c:if test="${tipoEntrega eq pedido.tipoEntrega}">selected</c:if>>${tipoEntrega.descricao}</option>
					</c:forEach>
				</select>
			</div>
			<div class="label" style="width: 7% ">Frete (R$):</div>
			<div class="input" style="width: 60%">
				<input id="fretePedido" name="pedido.valorFrete" value="${pedido.valorFrete}" style="width: 25%" />
			</div>
			<c:if test="${orcamento}">
				<div class="label">Validade:</div>
				<div class="input" style="width: 80%">
				<input id="validade" name="pedido.validade" value="${pedido.validade}" style="width: 5%" />
			</div>
			</c:if>
			<div class="label">Observação:</div>
			<div class="input areatexto" style="width: 71%">
				<textarea id="obervacao" name="pedido.observacao"
					style="width: 100%">${pedido.observacao}</textarea>
			</div>
			<div class="label">Observação Prod.:</div>
			<div class="input areatexto" style="width: 71%">
				<textarea id="observacaoProducao" name="pedido.observacaoProducao"
					style="width: 100%">${pedido.observacaoProducao}</textarea>
			</div>

		</fieldset>
	</form>
	<div class="bloco_botoes">
		<form id="formPesquisa" action="<c:url value="/pedido/listagem"/>" method="get">
			<input type="hidden" name="tipoPedido"  id="tipoPedidoPesquisa" value="${tipoPedido}"/> 
			<input type="hidden" name="idCliente" id="idClientePesquisa"  value="${cliente.id}"/> 
			<input type="hidden" name="idFornecedor" id="idFornecedorPesquisa" value="${idRepresentadaSelecionada}"/>
			<input type="hidden" name="idVendedor" id="idVendedorPesquisa" value="${proprietario.id}"/>  
			<input type="hidden" name="orcamento" id="orcamento" value="${empty orcamento ? false : orcamento}"/>
			<input type="button" id="botaoPesquisaPedido" value="" title="Pesquisar Dados do Pedido" class="botaoPesquisar" />
		</form>
		<form id="formLimparPedido" action="pedido/limpar" method="get">
			<input type="hidden" name="tipoPedido" value="${tipoPedido}" />
			<input type="hidden" name="orcamento" id="orcamento" value="${empty orcamento ? false : orcamento}"/>
			<input type="submit" value="" title="Limpar Dados do Pedido" class="botaoLimpar" />
		</form>
		<form action="pedido/pdf" method="get">
			<input type="hidden" name="tipoPedido" value="${tipoPedido}" /> 
			<input type="hidden" name="idPedido" id="idPedidoImpressao" value="${pedido.id}" />
			<input type="button" id="botaoImpressaoPedido" value="" title="Imprimir Pedido" class="botaoPDF" />
		</form>

		<c:if test="${acessoRefazerPedidoPermitido}">
			<form action="pedido/refazer" method="post">
				<input type="hidden" name="tipoPedido" value="${tipoPedido}" /> 
				<input type="hidden" name="idPedido" id="idPedido" value="${pedido.id}" />
				<input type="hidden" name="orcamento" id="orcamento" value="${empty orcamento ? false : orcamento}"/>
				<input id="botaoRefazerPedido" type="button" value="" title="Refazer Pedido" class="botaoRefazer" />
			</form>
		</c:if>
		<c:if test="${acessoCancelamentoPedidoPermitido}">
			<form action="pedido/cancelamento" method="post">
				<input type="hidden" name="tipoPedido" value="${tipoPedido}" /> 
				<input type="hidden" name="idPedido" id="idPedidoCancelamento" value="${pedido.id}" />
				<input id="botaoCancelarPedido" type="button" value="" title="Cancelar Pedido" class="botaoCancelar" />
			</form>
		</c:if>
	</div>

	<jsp:include page="/bloco/bloco_contato.jsp" />

	<div class="bloco_botoes">
		<c:if test="${not pedidoDesabilitado and acessoCadastroPedidoPermitido}">
			<a id="botaoInserirPedido" title="Incluir Dados do Pedido" class="botaoInserir"></a>
		</c:if>
	</div>
	<jsp:include page="/bloco/bloco_item_pedido.jsp" />

	<form id="formEnvioPedido" action="<c:url value="/pedido/envio"/>" method="post">
		<input type="hidden" name="tipoPedido" value="${tipoPedido}"/>
		<input type="hidden" name="orcamento" value="${orcamento}"/>
		<div class="bloco_botoes">
			<input type="button" id="botaoEnviarPedido" title="Enviar Email do ${orcamento ? 'Orçamento' : 'Pedido'}" value="" class="botaoEnviarEmail"
				<c:if test="${not acessoEnvioPedidoPermitido and not acessoReenvioPedidoPermitido}"> style='display:none'</c:if> 
			/>
			<c:if test="${orcamento}">
				<input type="button" id="botaoAceitarOrcamento" title="Aceitar do Or�amento" value="" class="botaoAceitar"/>
			</c:if>
			<input type="hidden" id="idPedido" name="idPedido" value="${pedido.id}" />
		</div>
	</form>

	<jsp:include page="/bloco/bloco_listagem_item_pedido.jsp"/>
	
</body>
</html>
