package br.com.svr.service.constante;

import java.util.ArrayList;
import java.util.List;


public enum SituacaoPedido {
	DIGITACAO ("DIGITA��O") ,
	ORCAMENTO ("OR�AMENTO"),
	ENVIADO ("ENVIADO"),
	CANCELADO ("CANCELADO"),
	COMPRA_AGUARDANDO_RECEBIMENTO("COMPRA AGUARDANDO RECEBIMENTO"),
	COMPRA_RECEBIDA("COMPRA RECEBIDA"),
	REVENDA_AGUARDANDO_EMPACOTAMENTO("REVENDA AGUARDANDO EMPACOTAMENTO"),
	ITEM_AGUARDANDO_COMPRA("ITEM AGUARDANDO COMPRA"),
	EMPACOTADO("EMPACOTADO"),
	COMPRA_ANDAMENTO("COMPRA EM ANDAMENTO"),
	ITEM_AGUARDANDO_MATERIAL("ITEM AGUARDANDO MATERIAL"),
	REVENDA_PARCIALMENTE_RESERVADA("REVENDA PARCIALMENTE RESERVADA"),
	ORCAMENTO_DIGITACAO ("OR�AMENTO DIGITA��O"),
	ORCAMENTO_ACEITO ("OR�AMENTO ACEITO"),
	ORCAMENTO_CANCELADO("OR�AMENTO CANCELADO");
	
	private String descricao;
	
	private SituacaoPedido(String descricao){
		this.descricao = descricao;
		
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	private static List<SituacaoPedido> listaCancelado= new ArrayList<SituacaoPedido>();
	private static List<SituacaoPedido> listaOrcamento = new ArrayList<SituacaoPedido>();
	private static List<SituacaoPedido> listaOrcamentoAberto = new ArrayList<SituacaoPedido>();
	private static List<SituacaoPedido> listaOrcamentoEfetivado = new ArrayList<SituacaoPedido>();
	private static List<SituacaoPedido> listaPedidoNaoEfetivado = new ArrayList<SituacaoPedido>();
	private static List<SituacaoPedido> listaCompraEfetivada = new ArrayList<SituacaoPedido>();
	private static List<SituacaoPedido> listaVendaEfetivada = new ArrayList<SituacaoPedido>();

	static{
		listaOrcamento.add(ORCAMENTO);
		listaOrcamento.add(ORCAMENTO_ACEITO);
		listaOrcamento.add(ORCAMENTO_CANCELADO);
		listaOrcamento.add(ORCAMENTO_DIGITACAO);
		
		listaOrcamentoAberto.add(ORCAMENTO);
		listaOrcamentoAberto.add(ORCAMENTO_DIGITACAO);
		
		listaOrcamentoEfetivado.add(ORCAMENTO);
		listaOrcamentoEfetivado.add(ORCAMENTO_DIGITACAO);
		listaOrcamentoEfetivado.add(ORCAMENTO_ACEITO);
		
		listaCancelado.add(CANCELADO);
		listaCancelado.add(ORCAMENTO_CANCELADO);
		
		// Devemos considerar que um orcamento nao eh um pedido.
		listaPedidoNaoEfetivado.add(DIGITACAO);
		listaPedidoNaoEfetivado.add(CANCELADO);
		listaPedidoNaoEfetivado.addAll(listaOrcamento);
		
		listaCompraEfetivada.add(COMPRA_AGUARDANDO_RECEBIMENTO);
		listaCompraEfetivada.add(COMPRA_ANDAMENTO);
		listaCompraEfetivada.add(COMPRA_RECEBIDA);
		
		listaVendaEfetivada.add(ENVIADO);
		listaVendaEfetivada.add(REVENDA_AGUARDANDO_EMPACOTAMENTO);
		listaVendaEfetivada.add(ITEM_AGUARDANDO_COMPRA);
		listaVendaEfetivada.add(EMPACOTADO);
		listaVendaEfetivada.add(ITEM_AGUARDANDO_MATERIAL);
		listaVendaEfetivada.add(REVENDA_PARCIALMENTE_RESERVADA);
	}
	public static List<SituacaoPedido> getListaOrcamento(){
		return listaOrcamento;
	}
	
	public static List<SituacaoPedido> getListaOrcamentoAberto(){
		return listaOrcamentoAberto;
	}
	
	public static List<SituacaoPedido> getListaOrcamentoEfetivado(){
		return listaOrcamentoEfetivado;
	}
	
	public static List<SituacaoPedido> getListaPedidoNaoEfetivado (){
		return listaPedidoNaoEfetivado ;
	}
	
	public static List<SituacaoPedido> getListaCompraEfetivada(){
		return listaCompraEfetivada;
	}
	
	public static boolean isCancelado(SituacaoPedido situacaoPedido){
		return situacaoPedido != null && listaCancelado.contains(situacaoPedido);
	}
	
	public static boolean isCompraEfetivada(SituacaoPedido stPedido) {
		return stPedido != null && listaCompraEfetivada.contains(stPedido);
	}

	public static boolean isOrcamento(SituacaoPedido situacaoPedido){
		return situacaoPedido != null && listaOrcamento.contains(situacaoPedido);
	}
	
	public static boolean isOrcamentoAberto(SituacaoPedido situacaoPedido){
		return situacaoPedido != null && listaOrcamentoAberto.contains(situacaoPedido);
	}
	
	public boolean isVendaEfetivada() {
		return listaVendaEfetivada.contains(this);
	}
	
	public static boolean isVendaEfetivada(SituacaoPedido stPedido) {
		return stPedido != null && listaVendaEfetivada.contains(stPedido);
	}
	
	public static List<SituacaoPedido> getListaVendaEfetivada() {
		return listaVendaEfetivada;
	}
}
