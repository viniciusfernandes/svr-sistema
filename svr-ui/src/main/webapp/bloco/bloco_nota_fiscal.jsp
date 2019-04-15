<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.10.3.datepicker.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.mask.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.maskMoney.js"/>"></script>

<script type="text/javascript">

$(document).ready(function() {
	inserirMascaraData('dataEmissaoNF');
	inserirMascaraData('dataVencimentoNF');
	inserirMascaraMonetaria('valorTotalNF', 8);
	inserirMascaraMonetaria('valorParcelaNF', 8);
	inserirMascaraNumerica('numeroVolumes', '999');
});

</script>

<fieldset id="bloco_dados_nota_fiscal">
	<legend>Dados Nota Fiscal</legend>
	<form id="bloco_dados_nota_fiscal" action="<c:url value="/empacotamento/inclusaodadosnf"/>" method="post">
		<input type="hidden" id="idPedido" name="pedido.id" value="${pedido.id}"/>
	
		<div class="label">N�m. NF</div>
		<div class="input">
			<input type="text" id="numeroNF" name="pedido.numeroNF" value="${pedido.numeroNF}" maxlength="8"/>
		</div>
		<div class="label">Dt.Emiss�o</div>
		<div class="input">
			<input type="text" id="dataEmissaoNF" name="pedido.dataEmissaoNF" value="${pedido.dataEmissaoNFFormatada}" />
		</div>
		<div class="label">Dt. Venc.</div>
		<div class="input">
			<input type="text" id="dataVencimentoNF" name="pedido.dataVencimentoNF" value="${pedido.dataVencimentoNFFormatada}"/>
		</div>
		<div class="label">Vl. Total</div>
		<div class="input">
			<input type="text" id="valorTotalNF" name="pedido.valorTotalNF" value="${pedido.valorTotalNF}"/>
		</div>
		<div class="label">Vl. Parcela</div>
		<div class="input">
			<input type="text" id="valorParcelaNF" name="pedido.valorParcelaNF" value="${pedido.valorParcelaNF}"/>
		</div>
		<div class="label">N�m. Coleta</div>
		<div class="input">
			<input type="text" id="numeroColeta" name="pedido.numeroColeta" value="${pedido.numeroColeta}" maxlength="8"/>
		</div>
		<div class="label">N�m. Volumes</div>
		<div class="input">
			<input type="text" id="numeroVolumes" name="pedido.numeroVolumes" value="${pedido.numeroVolumes}"/>
		</div>
		<c:if test="${empty inclusaoDadosNFdesabilitado}">
			<div class="bloco_botoes">
				<input type="submit" id="botaoInserirDadosNF" value="" class="botaoInserir" title="Inserir Dados Nota Fiscal"/> 
			</div>
		</c:if>
	</form>
</fieldset>