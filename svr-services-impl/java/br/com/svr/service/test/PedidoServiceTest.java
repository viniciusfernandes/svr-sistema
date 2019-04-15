package br.com.svr.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import br.com.svr.service.ClienteService;
import br.com.svr.service.ComissaoService;
import br.com.svr.service.EstoqueService;
import br.com.svr.service.MaterialService;
import br.com.svr.service.PedidoService;
import br.com.svr.service.RepresentadaService;
import br.com.svr.service.constante.FormaMaterial;
import br.com.svr.service.constante.SituacaoPedido;
import br.com.svr.service.constante.TipoApresentacaoIPI;
import br.com.svr.service.constante.TipoCliente;
import br.com.svr.service.constante.TipoEntrega;
import br.com.svr.service.constante.TipoLogradouro;
import br.com.svr.service.constante.TipoPedido;
import br.com.svr.service.constante.TipoRelacionamento;
import br.com.svr.service.constante.TipoVenda;
import br.com.svr.service.entity.Cliente;
import br.com.svr.service.entity.Contato;
import br.com.svr.service.entity.ContatoRepresentada;
import br.com.svr.service.entity.ItemPedido;
import br.com.svr.service.entity.Logradouro;
import br.com.svr.service.entity.Material;
import br.com.svr.service.entity.Pedido;
import br.com.svr.service.entity.Representada;
import br.com.svr.service.entity.Usuario;
import br.com.svr.service.exception.BusinessException;
import br.com.svr.service.mensagem.email.AnexoEmail;
import br.com.svr.service.test.builder.ServiceBuilder;
import br.com.svr.util.DateUtils;
import br.com.svr.util.StringUtils;

public class PedidoServiceTest extends AbstractTest {

	private ClienteService clienteService;

	private ComissaoService comissaoService;

	private EstoqueService estoqueService;

	private MaterialService materialService;

	private PedidoService pedidoService;

	private RepresentadaService representadaService;

	public PedidoServiceTest() {
		pedidoService = ServiceBuilder.buildService(PedidoService.class);
		clienteService = ServiceBuilder.buildService(ClienteService.class);
		materialService = ServiceBuilder.buildService(MaterialService.class);
		estoqueService = ServiceBuilder.buildService(EstoqueService.class);
		comissaoService = ServiceBuilder.buildService(ComissaoService.class);
		representadaService = ServiceBuilder.buildService(RepresentadaService.class);

	}

	private void associarVendedor(Cliente cliente) {
		cliente.setVendedor(eBuilder.buildUsuario());
		cliente.setEmail(cliente.getEmail() + Math.random());
		try {
			clienteService.inserir(cliente);
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	private Material gerarMaterial(Representada representada) {
		Material mat = eBuilder.buildMaterial();
		mat.setImportado(true);
		mat.addRepresentada(representada);
		try {
			mat.setId(materialService.inserir(mat));
			;
		} catch (BusinessException e1) {
			printMensagens(e1);
		}
		return mat;
	}

	private Pedido[] gerarRevendaEncomendada() {
		Pedido pRevenda = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(pRevenda.getRepresentada());
		item1.setPedido(pRevenda);

		ItemPedido item2 = eBuilder.buildItemPedidoPeca();
		item2.setPedido(pRevenda);
		item2.setMaterial(item1.getMaterial());

		Integer idPedidoRevenda = pRevenda.getId();
		try {
			pedidoService.inserirItemPedido(idPedidoRevenda, item1);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.inserirItemPedido(idPedidoRevenda, item2);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedidoRevenda, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedidoRevenda);
		assertEquals(
				"O pedido nao contem itens no estoque e deve aguardar o setor de comprar encomendar os itens de um fornecedor",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Representada fornecedor = gRepresentada.gerarFornecedor();
		gPedido.gerarRevendedor();

		Set<Integer> listaId = new HashSet<Integer>();
		listaId.add(item1.getId());
		listaId.add(item2.getId());

		gPedido.gerarAssociacaoMaterial(item1.getMaterial(), fornecedor.getId());
		gPedido.gerarAssociacaoMaterial(item2.getMaterial(), fornecedor.getId());

		Integer idPedidoCompra = null;
		try {
			idPedidoCompra = pedidoService.comprarItemPedido(pRevenda.getComprador().getId(), fornecedor.getId(),
					listaId);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedidoRevenda);
		assertEquals("Os itens do pedido nao contem itens mas ja foram encomendados pelo setor de compras",
				SituacaoPedido.ITEM_AGUARDANDO_MATERIAL, situacaoPedido);

		Pedido pCompra = recarregarEntidade(Pedido.class, idPedidoCompra);
		pCompra.setFormaPagamento("A VISTA");
		pCompra.setDataEntrega(TestUtils.gerarDataAmanha());
		try {
			pedidoService.enviarPedido(idPedidoCompra, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		return new Pedido[] { pCompra, pRevenda };
	}

	@Test
	public void testAceiteOrcamento() {
		Pedido orcamento = gPedido.gerarPedidoOrcamento();
		ItemPedido itemOrcamento = gPedido.gerarItemPedido(orcamento.getRepresentada());
		Integer idOrcamento = orcamento.getId();

		try {
			pedidoService.inserirItemPedido(idOrcamento, itemOrcamento);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idOrcamento, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		orcamento = pedidoService.pesquisarPedidoById(idOrcamento);
		assertEquals("A situacao do orcamento deve ser ORCAMENTO apos o aceite do orcamento", SituacaoPedido.ORCAMENTO,
				orcamento.getSituacaoPedido());

		Integer idPedido = null;
		try {
			idPedido = pedidoService.aceitarOrcamento(idOrcamento);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Pedido pedido = pedidoService.pesquisarPedidoById(idPedido);
		assertEquals("A situacao do pedido deve ser DIGITACAO apos o aceite do orcamento", SituacaoPedido.DIGITACAO,
				pedido.getSituacaoPedido());

		orcamento = recarregarEntidade(Pedido.class, idOrcamento);
		assertEquals("A situacao do orcamento deve ser ORCAMENTO ACEITO apos o aceite do orcamento",
				SituacaoPedido.ORCAMENTO_ACEITO, orcamento.getSituacaoPedido());

		assertNotEquals("OS ids do pedido e do orcamento devem ser diferentes apos o aceito do orcamento",
				pedido.getId(), orcamento.getId());

	}

	@Test
	public void testAceiteOrcamentoSemModificarPedidoNaoOrcamento() {
		Pedido pedido = gPedido.gerarPedidoRevenda();

		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		Integer idPedido = pedido.getId();

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedidoAntes = pedido.getSituacaoPedido();
		boolean throwed = false;
		try {
			pedidoService.aceitarOrcamento(idPedido);
		} catch (BusinessException e) {
			throwed = true;
		}

		assertTrue("Um id de pedido foi usado no aceite de orcamento e isso nao pode ocorrer.", throwed);
		pedido = pedidoService.pesquisarPedidoById(idPedido);
		assertEquals(
				"A situacao do pedido que nao seja um orcamento nao deve ser modificada apos o aceite do orcamento",
				situacaoPedidoAntes, pedido.getSituacaoPedido());
	}

	@Test
	public void testAlteracaoQuantidadeRecepcionada() {
		ItemPedido itemPedido = gPedido.gerarItemPedidoCompra();

		try {
			pedidoService.alterarQuantidadeRecepcionada(itemPedido.getId(), itemPedido.getQuantidade());
		} catch (BusinessException e) {
			printMensagens(e);
		}

		assertEquals(SituacaoPedido.COMPRA_AGUARDANDO_RECEBIMENTO,
				pedidoService.pesquisarSituacaoPedidoById(itemPedido.getPedido().getId()));
	}

	@Test
	public void testAlteracaoQuantidadeRecepcionadaInferiorQuantidadeComprada() {
		ItemPedido itemPedido = gPedido.gerarItemPedidoCompra();

		Integer quantidadeRecepcionada = itemPedido.getQuantidade() - 1;
		try {
			pedidoService.alterarQuantidadeRecepcionada(itemPedido.getId(), quantidadeRecepcionada);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		assertEquals(SituacaoPedido.COMPRA_AGUARDANDO_RECEBIMENTO,
				pedidoService.pesquisarSituacaoPedidoById(itemPedido.getPedido().getId()));
	}

	@Test
	public void testAlteracaoQuantidadeRecepcionadaSuperiorQuantidadeComprada() {
		ItemPedido itemPedido = gPedido.gerarItemPedidoCompra();
		Integer quantidadeRecepcionada = itemPedido.getQuantidade() + 1;
		boolean throwed = false;
		try {
			pedidoService.alterarQuantidadeRecepcionada(itemPedido.getId(), quantidadeRecepcionada);
		} catch (BusinessException e) {
			throwed = true;
		}

		assertTrue("A quantidade recepcionada e superior a quantidade comprada e deve ser validada", throwed);
	}

	@Test
	public void testCalculoParcelasVencimento() {
		Pedido p = gPedido.gerarPedidoRevenda();
		p.setFormaPagamento("10/20/30 dias");
		try {
			p = pedidoService.inserirPedido(p);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		// Gerando as datas de parcelamento a partir da data atual.
		Date dtAtual = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dtAtual);
		c.add(Calendar.DAY_OF_MONTH, 10);

		Date dt1 = c.getTime();

		c = Calendar.getInstance();
		c.setTime(dtAtual);
		c.add(Calendar.DAY_OF_MONTH, 20);

		Date dt2 = c.getTime();

		c = Calendar.getInstance();
		c.setTime(dtAtual);
		c.add(Calendar.DAY_OF_MONTH, 30);

		Date dt3 = c.getTime();

		List<Date> listaData = new ArrayList<Date>();
		listaData.add(dt1);
		listaData.add(dt2);
		listaData.add(dt3);

		List<Date> listaParg = pedidoService.calcularDataPagamento(p.getId(), dtAtual);
		assertTrue("O pedido contem 3 parcelas de pagamento e deve ter 3 datas de vencimento.", listaParg != null
				&& listaParg.size() == 3);

		int totalDt = 0;
		for (Date pag : listaParg) {
			for (int i = 0; i < listaData.size(); i++) {
				if (StringUtils.formatarData(listaData.get(i)).equals(StringUtils.formatarData(pag))) {
					totalDt++;
				}
			}
		}
		assertTrue("O total de parcelas de vencimento esta diferente do previso e configurado no pedido.", totalDt == 3);
	}

	@Test
	public void testCancelamentoPedidoVendaComQuantidadeInferiorAoEstoque() {
		ItemPedido iCompra = gPedido.gerarItemPedidoNoEstoque();
		Pedido pVenda = gPedido.gerarPedidoRevenda();
		Integer qtdeAntes = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		ItemPedido iVenda = null;
		Integer diferenca = 1;
		try {
			iVenda = iCompra.clone();
			// Adicionando uma quantidade superior ao do estoque para realizar
			// reserva parcial.
			iVenda.setQuantidade(iVenda.getQuantidade() - diferenca);

			pedidoService.inserirItemPedido(pVenda.getId(), iVenda);
			pedidoService.enviarPedido(pVenda.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Integer qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos a venda de quantidade igual a do estoque o item do estoque deve estar zerado",
				(Integer) diferenca, qtdeDepois);
		try {
			pedidoService.cancelarPedido(pVenda.getId());
		} catch (BusinessException e) {
			printMensagens(e);
		}
		qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos o cancelamento da venda de quantidade do estoque deve ser igual a inicial", qtdeAntes,
				qtdeDepois);

		iVenda = pedidoService.pesquisarItemPedidoById(iVenda.getId());
		assertEquals(
				"Apos o cancelamento da venda a quantidade reservada deve ser a mesma da quantidade total vendida",
				iVenda.getQuantidade(), iVenda.getQuantidadeReservada());

	}

	@Test
	public void testCancelamentoPedidoVendaComReservaParcial() {
		ItemPedido iCompra = gPedido.gerarItemPedidoNoEstoque();
		Pedido pVenda = gPedido.gerarPedidoRevenda();
		Integer qtdeAntes = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		ItemPedido iVenda = null;
		try {
			iVenda = iCompra.clone();
			// Adicionando uma quantidade superior ao do estoque para realizar
			// reserva parcial.
			iVenda.setQuantidade(iVenda.getQuantidade() + 50);

			pedidoService.inserirItemPedido(pVenda.getId(), iVenda);
			pedidoService.enviarPedido(pVenda.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Integer qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos a venda de quantidade igual a do estoque o item do estoque deve estar zerado", (Integer) 0,
				qtdeDepois);
		try {
			pedidoService.cancelarPedido(pVenda.getId());
		} catch (BusinessException e) {
			printMensagens(e);
		}
		qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos o cancelamento da venda de quantidade do estoque deve ser igual a inicial", qtdeAntes,
				qtdeDepois);

		iVenda = pedidoService.pesquisarItemPedidoById(iVenda.getId());
		assertEquals("Apos o cancelamento da venda a quantidade reservada deve ser a mesma do que foi comprado",
				qtdeAntes, iVenda.getQuantidadeReservada());

	}

	@Test
	public void testCancelamentoPedidoVendaQuantidadeIgualAoEstoque() {
		ItemPedido iCompra = gPedido.gerarItemPedidoNoEstoque();
		Pedido pVenda = gPedido.gerarPedidoRevenda();
		Integer qtdeAntes = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();

		try {
			ItemPedido iVenda = iCompra.clone();
			pedidoService.inserirItemPedido(pVenda.getId(), iVenda);
			pedidoService.enviarPedido(pVenda.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Integer qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos a venda de quantidade igual a do estoque o item do estoque deve estar zerado", (Integer) 0,
				qtdeDepois);
		try {
			pedidoService.cancelarPedido(pVenda.getId());
		} catch (BusinessException e) {
			printMensagens(e);
		}
		qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos o cancelamento da venda de quantidade do estoque deve ser igual a inicial", qtdeAntes,
				qtdeDepois);

		SituacaoPedido st = pedidoService.pesquisarSituacaoPedidoById(pVenda.getId());
		assertEquals("Apos o cancelamento a situacao do pedido deve ser CANCELADO", SituacaoPedido.CANCELADO, st);
	}

	@Test
	public void testCopiaPedido() {
		Pedido p = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(p.getRepresentada());
		ItemPedido item2 = gPedido.gerarItemPedido(p.getRepresentada());

		// Alterando as medidas do item para simular itens distintos.
		item2.setMedidaExterna(200d);
		item2.setMedidaInterna(20d);
		item2.setComprimento(200d);
		try {
			pedidoService.inserirItemPedido(p.getId(), item1);
			pedidoService.inserirItemPedido(p.getId(), item2);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(p.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		SituacaoPedido s = p.getSituacaoPedido();
		Integer idCopia = null;
		try {
			idCopia = pedidoService.copiarPedido(p.getId(), false);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Pedido pCopia = pedidoService.pesquisarPedidoById(idCopia);
		assertNotEquals("O pedido copia nao pode conter o mesmo ID do pedido copiado", p.getId(), pCopia.getId());

		assertEquals("O pedido copia deve estar em DIGITACAO apos a copia", SituacaoPedido.DIGITACAO,
				pCopia.getSituacaoPedido());
		assertEquals("O pedido copiado deve ter a situacao igual a situacao anterior a copia", s, p.getSituacaoPedido());
		assertNull("O pedido copiado nao pode ter data de envio", pCopia.getDataEnvio());
		assertNull("O pedido copiado nao pode ter data emissao NFe", pCopia.getDataEmissaoNF());
		assertNull("O pedido copiado nao pode ter data vencimento", pCopia.getDataVencimentoNF());
		assertNull("O pedido copiado nao pode ter valor de parcela NFe", pCopia.getValorParcelaNF());
		assertNull("O pedido copiado nao pode ter total NFe", pCopia.getValorTotalNF());
		assertEquals("O pedido copia deve conter a mesma data de entrega",
				StringUtils.formatarData(pCopia.getDataEntrega()), StringUtils.formatarData(p.getDataEntrega()));

		List<ItemPedido> lItemCopia = pedidoService.pesquisarItemPedidoByIdPedido(pCopia.getId());
		List<ItemPedido> lItem = pedidoService.pesquisarItemPedidoByIdPedido(p.getId());

		// Nao me lembro o porque da validacao no logradouro Talvez tenha que
		// remover.
		if (pCopia.getListaLogradouro() != null) {
			for (Logradouro lCopia : pCopia.getListaLogradouro()) {
				for (Logradouro l : p.getListaLogradouro()) {
					if (lCopia.getId() == null) {
						Assert.fail("O logradouro do pedido que foi copiado nao contem id. Item: "
								+ lCopia.getEnderecoNumeroBairro());
						break;
					}
					assertFalse("O logradouro do pedido que foi copiado nao pode conter o mesmo id do original", lCopia
							.getId().equals(l.getId()));
				}
			}
		}

		for (ItemPedido iCopia : lItemCopia) {
			for (ItemPedido iPed : lItem) {
				if (iCopia.getId() == null) {
					Assert.fail("O item do pedido que foi copiado nao contem id. Item: "
							+ iCopia.getDescricaoSemFormatacao());
					break;
				}
				assertNull("O item copiado nao pode ter pedido de compra. Item: " + iCopia.getDescricaoSemFormatacao(),
						iCopia.getIdPedidoCompra());
				assertTrue(
						"O item copiado nao pode ter quantidade recepcionada. Item: "
								+ iCopia.getDescricaoSemFormatacao(), iCopia.getQuantidadeRecepcionada() == null
								|| iCopia.getQuantidadeRecepcionada() == 0);
				assertNull("O item copiado nao pode ter pedido de venda. Item: " + iCopia.getDescricaoSemFormatacao(),
						iCopia.getIdPedidoVenda());
				assertTrue(
						"O item copiado nao pode ter quantidade reservada. Item: " + iCopia.getDescricaoSemFormatacao(),
						iCopia.getQuantidadeReservada() == null || iCopia.getQuantidadeReservada() == 0);
				assertFalse("O item copiado nao pode ter encomendas. Item: " + iCopia.getDescricaoSemFormatacao(),
						iCopia.isEncomendado());
				assertFalse("O item do pedido que foi copiado nao pode conter o mesmo id do item original. Item: "
						+ iCopia.getDescricaoSemFormatacao(), iCopia.getId().equals(iPed.getId()));
			}
		}
	}

	@Test
	public void testDataEnvioCopiaPedidoEnviado() {
		Pedido p = gPedido.gerarPedidoRepresentacao();
		Integer idPed = null;
		try {
			idPed = pedidoService.inserirPedido(p).getId();
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		ItemPedido i = gPedido.gerarItemPedido(p.getRepresentada());

		try {
			pedidoService.inserirItemPedido(idPed, i);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPed, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		// Retrocedendo a data de envio para simular uma copia em data posterior
		p.setDataEnvio(DateUtils.retrocederData(p.getDataEnvio()));
		try {
			pedidoService.inserirPedido(p);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		p = pedidoService.pesquisarPedidoById(idPed);
		String dtEnvFormt = StringUtils.formatarData(p.getDataEnvio());

		Integer idCopia = null;
		try {
			idCopia = pedidoService.copiarPedido(idPed, false);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Pedido pCopia = pedidoService.pesquisarPedidoById(idCopia);
		Date dtEnvCopia = pCopia.getDataEnvio();

		assertNull("A data de envio de uma copia do pedido deve ser nula", dtEnvCopia);

		try {
			pedidoService.enviarPedido(idCopia, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
		pCopia = pedidoService.pesquisarPedidoById(idCopia);
		String dtCopiaFormt = StringUtils.formatarData(pCopia.getDataEnvio());
		assertNotEquals(
				"As datas de envio do pedido original e copiado nao podem ser iguais pois o pedido copiado eh antigo",
				dtCopiaFormt, dtEnvFormt);

		List<ItemPedido> lItem = pedidoService.pesquisarItemPedidoByIdPedido(idCopia);
		for (ItemPedido item : lItem) {
			assertNull("Apos a copia os itens nao podem ter pedido de compra", item.getIdPedidoCompra());
			assertNull("Apos a copia os itens nao podem ter pedido de venda", item.getIdPedidoVenda());
			assertFalse("Apos a copia os itens nao podem ter sido encomendados", item.getQuantidadeEncomendada() == 0);
			assertTrue("Apos a copia os itens nao podem ter sido reservados", item.getQuantidadeReservada() == null
					|| item.getQuantidadeReservada() == 0);
			assertTrue("Apos a copia os itens nao podem ter sido recepcionado",
					item.getQuantidadeRecepcionada() == null || item.getQuantidadeRecepcionada() == 0);
		}
	}

	@Test
	public void testDuploCancelamentoPedidoVenda() {
		ItemPedido iCompra = gPedido.gerarItemPedidoNoEstoque();
		Pedido pVenda = gPedido.gerarPedidoRevenda();
		Integer qtdeAntes = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		ItemPedido iVenda = null;
		try {
			iVenda = iCompra.clone();
			pedidoService.inserirItemPedido(pVenda.getId(), iVenda);
			pedidoService.enviarPedido(pVenda.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Integer qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos a venda de quantidade igual a do estoque o item do estoque deve estar zerado", (Integer) 0,
				qtdeDepois);
		try {
			pedidoService.cancelarPedido(pVenda.getId());
		} catch (BusinessException e) {
			printMensagens(e);
		}
		qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos o cancelamento da venda de quantidade do estoque deve ser igual a inicial", qtdeAntes,
				qtdeDepois);
		try {
			pedidoService.cancelarPedido(pVenda.getId());
		} catch (BusinessException e) {
			printMensagens(e);
		}
		qtdeDepois = estoqueService.pesquisarItemEstoque(iCompra).getQuantidade();
		assertEquals("Apos um segundo cancelamento da venda de quantidade do estoque deve ser igual a inicial",
				qtdeAntes, qtdeDepois);
	}

	@Test
	public void testEfetuarEncomendaItemPedido() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	@Test
	public void testEnvioEmailPedido() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		Integer idPedido = null;
		try {
			idPedido = pedidoService.inserirPedido(pedido).getId();
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		ItemPedido i = gPedido.gerarItemPedido(pedido.getRepresentada());

		try {
			pedidoService.inserirItemPedido(idPedido, i);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(pedido.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	@Test
	public void testEnvioEmailPedidoCancelado() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		pedido.setSituacaoPedido(SituacaoPedido.CANCELADO);

		Integer idPedido = pedido.getId();
		boolean throwed = false;
		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("Pedido cancelado nao pode ser enviado por email", throwed);
	}

	@Test
	public void testEnvioEmailPedidoClienteNaoPropspectado() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		boolean throwed = false;
		try {
			pedidoService.enviarPedido(pedido.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("Pedido com cliente nao propspectado nao pode ser enviado por email", throwed);
	}

	@Test
	public void testEnvioEmailPedidoJaEnviado() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		pedido.setSituacaoPedido(SituacaoPedido.ENVIADO);
		boolean throwed = false;
		try {
			pedidoService.enviarPedido(pedido.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("Pedido ja enviado nao pode ser enviado por email", throwed);
	}

	@Test
	public void testEnvioEmailPedidoOrcamento() {
		Pedido pedido = gPedido.gerarPedidoOrcamento();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		Integer idPedido = pedido.getId();

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		pedido = pedidoService.pesquisarPedidoById(idPedido);
		assertEquals("A situacao do pedido deve ser ORCAMENTO apos o envio de email de orcamento",
				SituacaoPedido.ORCAMENTO, pedido.getSituacaoPedido());

	}

	@Test
	public void testEnvioEmailPedidoOrcamentoCliente() {
		Pedido pedido = gPedido.gerarPedidoOrcamento();
		pedido.setClienteNotificadoVenda(true);

		try {
			pedidoService.inserirPedido(pedido);
		} catch (BusinessException e2) {
			printMensagens(e2);
		}

		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		Integer idPedido = pedido.getId();

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		pedido = pedidoService.pesquisarPedidoById(idPedido);
		assertEquals("A situacao do pedido deve ser ORCAMENTO apos o envio de email de orcamento",
				SituacaoPedido.ORCAMENTO, pedido.getSituacaoPedido());

	}

	@Test
	public void testEnvioEmailPedidoOrcamentoSemDDDContato() {
		Pedido pedido = gPedido.gerarPedidoOrcamento();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		Integer idPedido = pedido.getId();

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		Contato contato = pedido.getContato();
		contato.setDdd(null);
		boolean throwed = false;
		try {
			// Inserindo a alteracao do contato no sistema
			pedidoService.inserirPedido(pedido);
		} catch (BusinessException e1) {
			throwed = true;
		}
		assertTrue("Nao eh possivel inserir pedido sem DDD do contato", throwed);
		throwed = false;

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}

		assertTrue("O DDD do contato do orcamento eh obrigatorio", throwed);
		pedido = pedidoService.pesquisarPedidoById(idPedido);
		assertEquals("O pedido nao foi enviado e deve permanecer na situacao de orcamento", SituacaoPedido.ORCAMENTO,
				pedido.getSituacaoPedido());

	}

	@Test
	public void testEnvioEmailPedidoOrcamentoSemEmailContato() {
		Pedido pedido = gPedido.gerarPedidoOrcamento();
		pedido.getContato().setEmail(null);

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		boolean throwed = false;
		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O email do contato eh obrigatorio para o envio do orcamento", throwed);
	}

	@Test
	public void testEnvioEmailPedidoOrcamentoSemTelefoneContato() {
		Pedido pedido = gPedido.gerarPedidoOrcamento();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		Integer idPedido = pedido.getId();

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		Contato contato = pedido.getContato();
		contato.setTelefone(null);
		boolean throwed = false;
		try {
			// Inserindo a alteracao do contato no sistema
			pedidoService.inserirPedido(pedido);
		} catch (BusinessException e1) {
			throwed = true;
		}
		assertTrue("O telefone  do contato do orcamento eh obrigatorio", throwed);

		throwed = false;
		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}

		assertTrue("O telefone  do contato do orcamento eh obrigatorio", throwed);
		pedido = pedidoService.pesquisarPedidoById(idPedido);
		assertEquals("O pedido nao foi enviado e deve permanecer na situacao de orcamento", SituacaoPedido.ORCAMENTO,
				pedido.getSituacaoPedido());

	}

	@Test
	public void testEnvioEmailPedidoReservaInvalida() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.setDataEntrega(TestUtils.gerarDataAmanha());
		pedido.setFormaPagamento("30 dias");
		pedido.setTipoEntrega(TipoEntrega.FOB);
		Integer idPedido = null;
		try {
			pedido = pedidoService.inserirPedido(pedido);
			idPedido = pedido.getId();
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	@Test
	public void testEnvioEmailPedidoSemEnderecoCobranca() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		Cliente cliente = pedido.getCliente();
		// removendo os logrs para ter certeza que nao ha informacao no sistema.
		clienteService.removerLogradouroByIdCliente(cliente.getId());

		cliente.setListaLogradouro(null);
		cliente.addLogradouro(eBuilder.buildLogradouroCliente(TipoLogradouro.ENTREGA));
		cliente.addLogradouro(eBuilder.buildLogradouroCliente(TipoLogradouro.FATURAMENTO));

		try {
			cliente = clienteService.inserir(cliente);
		} catch (BusinessException e2) {
			printMensagens(e2);
		}

		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		Integer idPedido = pedido.getId();

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		boolean throwed = false;
		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O endereco de cobranca eh obrigatorio para o envio do pedido por email", throwed);
	}

	@Test
	public void testEnvioEmailPedidoSemEnderecoEntrega() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		Cliente cliente = pedido.getCliente();
		// removendo os logrs para ter certeza que nao ha informacao no sistema.
		clienteService.removerLogradouroByIdCliente(cliente.getId());

		cliente.setListaLogradouro(null);
		cliente.addLogradouro(eBuilder.buildLogradouroCliente(TipoLogradouro.COBRANCA));
		cliente.addLogradouro(eBuilder.buildLogradouroCliente(TipoLogradouro.FATURAMENTO));

		try {
			cliente = clienteService.inserir(cliente);
		} catch (BusinessException e2) {
			printMensagens(e2);
		}

		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		Integer idPedido = pedido.getId();

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		boolean throwed = false;
		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O endereco de entrega eh obrigatorio para o envio do pedido por email", throwed);
	}

	@Test
	public void testEnvioEmailPedidoSemEnderecoFaturamento() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		Cliente cliente = pedido.getCliente();
		// removendo os logrs para ter certeza que nao ha informacao no sistema.
		clienteService.removerLogradouroByIdCliente(cliente.getId());

		cliente.setListaLogradouro(null);
		cliente.addLogradouro(eBuilder.buildLogradouroCliente(TipoLogradouro.COBRANCA));
		cliente.addLogradouro(eBuilder.buildLogradouroCliente(TipoLogradouro.ENTREGA));

		try {
			cliente = clienteService.inserir(cliente);
		} catch (BusinessException e2) {
			printMensagens(e2);
		}

		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		Integer idPedido = pedido.getId();

		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		boolean throwed = false;
		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O endereco de faturamento eh obrigatorio para o envio do pedido por email", throwed);
	}

	@Test
	public void testEnvioEmailPedidoSemItens() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		boolean throwed = false;
		try {
			pedidoService.enviarPedido(pedido.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("Pedido sem itens nao pode ser enviado por email", throwed);
	}

	@Test
	public void testEnvioPedidoCompra() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.setDataEntrega(TestUtils.gerarDataAmanha());
		pedido.setFormaPagamento("30 dias a vista");
		pedido.setTipoEntrega(TipoEntrega.CIF);
		pedido.setTipoPedido(TipoPedido.COMPRA);

		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		assertEquals("Antes do envio do pedido de compra ele deve estar como em digitacao", SituacaoPedido.DIGITACAO,
				pedido.getSituacaoPedido());

		try {
			pedidoService.enviarPedido(pedido.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertEquals("Apos o envio do pedido de compra, seu estado deve ser como aguardando recebimento",
				SituacaoPedido.COMPRA_AGUARDANDO_RECEBIMENTO, pedido.getSituacaoPedido());

	}

	@Test
	public void testEnvioPedidoRevendaComissaoItemPedido() {
		Pedido pedido = gPedido.gerarPedidoRevendaComItem();

		Integer idPedido = pedido.getId();

		List<ItemPedido> listaItem = pedidoService.pesquisarItemPedidoByIdPedido(idPedido);
		ItemPedido itemPedido = listaItem.get(0);
		itemPedido.setAliquotaComissao(0.5d);

		final Double valorComissionado = itemPedido.calcularPrecoItem() * itemPedido.getAliquotaComissao();
		final Integer idItemPedido = itemPedido.getId();

		try {
			// Estamos inserindo a inclusao de uma aliquota de comissao do item
			// para
			// testar o algoritmo de calculo de comissoes disparado no envio do
			// pedido. Essa informacao pode ser inputada pelo usuario e deve ter
			// prioridade no calculo.
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		Integer idVendedor = pedido.getVendedor().getId();
		try {
			comissaoService.inserirComissaoVendedor(idVendedor, 0.1, null);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		itemPedido = pedidoService.pesquisarItemPedidoById(idItemPedido);

		assertEquals(valorComissionado, itemPedido.getValorComissionado());
	}

	@Test
	public void testEnvioPedidoVendaEmailContato() {
		Pedido p = gPedido.gerarPedidoRevenda();
		p.setClienteNotificadoVenda(true);

		try {
			pedidoService.inserirPedido(p);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		ItemPedido i = gPedido.gerarItemPedido(p.getRepresentada());
		try {
			pedidoService.inserirItemPedido(p.getId(), i);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		try {
			pedidoService.enviarPedido(p.getId(), new AnexoEmail(new byte[] {}), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	@Test
	public void testEnvioRevendaAguardandoMaterialEmpacotamentoInvalido() {
		Pedido[] pedidos = gerarRevendaEncomendada();
		Integer idPedidoCompra = pedidos[0].getId();
		Integer idPedidoRevenda = pedidos[1].getId();

		List<ItemPedido> listaItemComprado = pedidoService.pesquisarItemPedidoByIdPedido(idPedidoCompra);
		// Vamos inserir apenas 1 item do pedido para manter a revenda como
		// encomendada.
		ItemPedido itemComprado = listaItemComprado.get(0);
		try {
			// Recepcionando os itens comprados para preencher o estoque.
			estoqueService
					.adicionarQuantidadeRecepcionadaItemCompra(itemComprado.getId(), itemComprado.getQuantidade());
		} catch (BusinessException e) {
			printMensagens(e);
		}

		try {
			pedidoService.empacotarItemAguardandoMaterial(idPedidoRevenda);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedidoRevenda);
		assertEquals(SituacaoPedido.ITEM_AGUARDANDO_MATERIAL, situacaoPedido);
	}

	@Test
	public void testEnvioRevendaEncomendadaEmpacotamento() {
		Pedido[] pedidos = gerarRevendaEncomendada();
		Integer idPedidoCompra = pedidos[0].getId();
		Integer idPedidoRevenda = pedidos[1].getId();

		List<ItemPedido> listaItemComprado = pedidoService.pesquisarItemPedidoByIdPedido(idPedidoCompra);
		for (ItemPedido itemComprado : listaItemComprado) {
			// Recepcionando os itens comprados para preencher o estoque.
			try {
				estoqueService.adicionarQuantidadeRecepcionadaItemCompra(itemComprado.getId(),
						itemComprado.getQuantidade());
			} catch (BusinessException e) {
				printMensagens(e);
			}
		}

		try {
			pedidoService.empacotarItemAguardandoMaterial(idPedidoRevenda);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedidoRevenda);
		assertEquals(SituacaoPedido.REVENDA_AGUARDANDO_EMPACOTAMENTO, situacaoPedido);
	}

	public void testInclusaoItemPedido() {
		Pedido pedido = eBuilder.buildPedido();
		associarVendedor(pedido.getCliente());

		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	@Test
	public void testInclusaoItemPedidoComIPIRepresentadaSemIPI() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.NUNCA);
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		ItemPedido itemPedido = eBuilder.buildItemPedido();
		itemPedido.setAliquotaIPI(0.02);
		boolean throwed = false;
		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("A representada definida no pedido nao permite valores de IPI", throwed);
	}

	@Test
	public void testInclusaoItemPedidoFormaQuadradaMedidaInternaIgualExterna() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();

		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		itemPedido.setFormaMaterial(FormaMaterial.BQ);
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertEquals(
				"Para a forma de material qaudrada as medidas interna e externa devem ser identicas apos a inclusao do item",
				itemPedido.getMedidaExterna(), itemPedido.getMedidaInterna());
	}

	@Test
	public void testInclusaoItemPedidoIPINuloMaterialImportadoRepresentadaIPIOcasional() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.OCASIONAL);
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Material material = gerarMaterial(pedido.getRepresentada());
		ItemPedido itemPedido = eBuilder.buildItemPedido();
		itemPedido.setMaterial(material);
		itemPedido.setAliquotaIPI(null);

		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Double ipi = itemPedido.getFormaMaterial().getIpi();
		assertEquals("O IPI nao foi enviado e deve ser o valor default apos a inclusao", ipi,
				itemPedido.getAliquotaIPI());
	}

	@Test
	public void testInclusaoItemPedidoIPINuloRepresentadaComIPIObrigatorio() {
		Pedido p = gPedido.gerarPedidoComItem(TipoPedido.REPRESENTACAO);

		Representada representada = p.getRepresentada();
		representada.setTipoApresentacaoIPI(TipoApresentacaoIPI.SEMPRE);
		try {
			representadaService.inserir(representada);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		List<ItemPedido> l = pedidoService.pesquisarItemPedidoByIdPedido(p.getId());
		for (ItemPedido i : l) {

			assertTrue("O IPI do item do item " + i.getSequencial()
					+ " nao confere com o IPI da forma de material escolhida", FormaMaterial.TB.getIpi() == i
					.getAliquotaIPI().doubleValue());

		}
	}

	@Test
	public void testInclusaoItemPedidoIPIZeradoMaterialImportadoRepresentadaIPIOcasional() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.OCASIONAL);
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Material material = gerarMaterial(pedido.getRepresentada());
		ItemPedido itemPedido = eBuilder.buildItemPedido();
		itemPedido.setMaterial(material);
		itemPedido.setAliquotaIPI(0d);

		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Double ipi = 0d;
		assertEquals("O IPI foi enviado zerado e deve ser o mesmo apos a inclusao", ipi, itemPedido.getAliquotaIPI());
	}

	@Test
	public void testInclusaoItemPedidoIPIZeradoRepresentadaComIPIObrigatorio() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.SEMPRE);

		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		itemPedido.setAliquotaIPI(0d);

		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertTrue("O IPI do item nao confere com o IPI da forma de material escolhida",
				FormaMaterial.TB.getIpi() == itemPedido.getAliquotaIPI().doubleValue());
	}

	@Test
	public void testInclusaoItemPedidoMaterialImportadoRepresentadaIPIOcasional() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.OCASIONAL);
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Material material = gerarMaterial(pedido.getRepresentada());
		final Double ipi = 0.02d;
		ItemPedido itemPedido = eBuilder.buildItemPedido();
		itemPedido.setMaterial(material);
		itemPedido.setAliquotaIPI(ipi);

		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertEquals("O IPI foi enviado e deve ser o mesmo apos a inclusao do item do pedido", ipi,
				itemPedido.getAliquotaIPI());
	}

	@Test
	public void testInclusaoItemPedidoMaterialImportadoRepresentadaSemIPI() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.NUNCA);
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Material material = gerarMaterial(pedido.getRepresentada());

		ItemPedido itemPedido = eBuilder.buildItemPedido();
		itemPedido.setMaterial(material);
		itemPedido.setAliquotaIPI(0.02);
		boolean throwed = false;
		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("A representada definida no pedido nao permite valores de IPI", throwed);
	}

	@Test
	public void testInclusaoItemPedidoMaterialNacionalRepresentadaIPIOcasional() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.OCASIONAL);
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Material material = gerarMaterial(pedido.getRepresentada());
		material.setImportado(false);

		final Double ipi = 0.02d;
		ItemPedido itemPedido = eBuilder.buildItemPedido();
		itemPedido.setMaterial(material);
		itemPedido.setAliquotaIPI(ipi);

		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertEquals("O IPI foi enviado e deve ser o mesmo apos a inclusao do item do pedido", ipi,
				itemPedido.getAliquotaIPI());
	}

	@Test
	public void testInclusaoItemPedidoPecaDescricaoNula() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		itemPedido.setTipoVenda(TipoVenda.PECA);
		itemPedido.setFormaMaterial(FormaMaterial.PC);
		itemPedido.setDescricaoPeca(null);
		boolean throwed = false;
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("A venda de peca deve conter uma descricao", throwed);
	}

	@Test
	public void testInclusaoItemPedidoPecaPorKilo() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		itemPedido.setTipoVenda(TipoVenda.KILO);
		itemPedido.setFormaMaterial(FormaMaterial.PC);
		itemPedido.setDescricaoPeca("engrenagem de plastico");
		boolean throwed = false;
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("Nao eh possivel vender uma peca por kilo. A venda deve ser feita por peca", throwed);
	}

	@Test
	public void testInclusaoItemPedidoPecaSemDescricao() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		itemPedido.setTipoVenda(TipoVenda.PECA);
		itemPedido.setFormaMaterial(FormaMaterial.PC);
		itemPedido.setDescricaoPeca("");
		boolean throwed = false;
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("A venda de peca deve conter uma descricao", throwed);
	}

	@Test
	public void testInclusaoItemPedidoPecaVendidoPorKilo() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();

		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		itemPedido.setTipoVenda(TipoVenda.KILO);
		itemPedido.setFormaMaterial(FormaMaterial.PC);
		itemPedido.setDescricaoPeca("engrenagem de plastico");
		boolean throwed = false;
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("Uma peca nunca pode ser vendida a kilo", throwed);
	}

	@Test
	public void testInclusaoItemPedidoPecaVendidoPorPeca() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();

		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		itemPedido.setTipoVenda(TipoVenda.PECA);
		itemPedido.setFormaMaterial(FormaMaterial.PC);
		itemPedido.setDescricaoPeca("engrenagem de plastico");
		itemPedido.setMedidaExterna(null);
		itemPedido.setMedidaInterna(null);
		itemPedido.setComprimento(null);
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	@Test
	public void testInclusaoItemPedidoRepresentacaoSemComissaoRepresentacao() {
		Pedido pedido = gPedido.gerarPedidoRepresentacaoComItem();
		Integer idPedido = pedido.getId();
		Integer idVendedor = pedido.getVendedor().getId();
		boolean throwed = false;

		try {
			comissaoService.inserirComissaoVendedor(idVendedor, null, null);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		ItemPedido i = gPedido.gerarItemPedido(pedido.getRepresentada());
		try {
			pedidoService.inserirItemPedido(idPedido, i);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O vendedor ainda nao possui comissao de representacao e deve ser validado no sistema", throwed);

		try {
			comissaoService.inserirComissaoVendedor(idVendedor, null, 0.1);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		i = gPedido.gerarItemPedido(pedido.getRepresentada());
		try {
			pedidoService.inserirItemPedido(idPedido, i);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	@Test
	public void testInclusaoItemPedidoRepresentadaSemIPI() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(null);

		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		itemPedido.setAliquotaIPI(null);
		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertNull("O IPI nao foi configurado e deve ser nulo", itemPedido.getAliquotaIPI());
	}

	@Test
	public void testInclusaoItemPedidoRevendaComFrete() {
		Pedido p = gPedido.gerarPedidoRepresentacao();
		Integer idPed = null;
		try {
			idPed = pedidoService.inserirPedido(p).getId();
		} catch (BusinessException e) {
			printMensagens(e);
		}

		ItemPedido i = gPedido.gerarItemPedido(p.getRepresentada());
		i.setTipoVenda(TipoVenda.PECA);
		i.setPrecoVenda(100d);

		try {
			pedidoService.inserirItemPedido(idPed, i);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		double vPedido = pedidoService.pesquisarValorPedido(idPed);
		double vFrete = 10d;

		// Inserindo um valor de frete
		p = pedidoService.pesquisarPedidoById(idPed);
		p.setValorFrete(vFrete);
		try {
			pedidoService.inserirPedido(p);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		double vPedidoFrete = pedidoService.pesquisarValorPedido(idPed);
		assertTrue("O valor do pedido deve conter o valor do frete apos a inclusao do frete",
				vPedidoFrete == (vFrete + vPedido));

	}

	@Test
	public void testInclusaoItemPedidoRevendaSemComissaoRevenda() {
		Pedido pedido = gPedido.gerarPedidoRevendaComItem();
		Integer idPedido = pedido.getId();
		Integer idVendedor = pedido.getVendedor().getId();
		boolean throwed = false;
		try {
			comissaoService.inserirComissaoVendedor(idVendedor, null, null);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		ItemPedido i = gPedido.gerarItemPedido(pedido.getRepresentada());

		try {
			pedidoService.inserirItemPedido(idPedido, i);
		} catch (BusinessException e1) {
			throwed = true;
		}

		assertTrue(
				"O vendedor nao pode incluir um item de pedido de revenda pois ainda nao possui comissao de revenda e deve ser validado no sistema",
				throwed);

		try {
			comissaoService.inserirComissaoVendedor(idVendedor, 0.1, null);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		i = gPedido.gerarItemPedido(pedido.getRepresentada());

		try {
			pedidoService.inserirItemPedido(idPedido, i);
		} catch (BusinessException e1) {
			throwed = true;
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}
	}

	@Test
	public void testInclusaoItemPedidoSemIPIMaterialNacionalRepresentadaIPIOcasional() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.OCASIONAL);
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Material material = gerarMaterial(pedido.getRepresentada());
		material.setImportado(false);

		ItemPedido itemPedido = eBuilder.buildItemPedido();
		itemPedido.setMaterial(material);
		itemPedido.setAliquotaIPI(null);

		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertTrue("O IPI foi nao foi enviado, portanto, nao deve existir apos a inclusao do item do pedido",
				itemPedido.getAliquotaIPI() == null || ((double) itemPedido.getAliquotaIPI()) == 0d);
	}

	@Test
	public void testInclusaoItemSemIPIPedidoMaterialImportadoRepresentadaIPIOcasional() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getRepresentada().setTipoApresentacaoIPI(TipoApresentacaoIPI.OCASIONAL);
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Material material = gerarMaterial(pedido.getRepresentada());
		ItemPedido itemPedido = eBuilder.buildItemPedido();
		itemPedido.setMaterial(material);
		itemPedido.setAliquotaIPI(null);
		itemPedido.setFormaMaterial(FormaMaterial.CH);
		try {
			pedidoService.inserirItemPedido(pedido.getId(), itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		Double padrao = FormaMaterial.CH.getIpi();
		assertEquals(
				"O IPI de material importado nao foi enviado portanto deve ser utilizado o default apos a inclusao do item do pedido",
				padrao, itemPedido.getAliquotaIPI());
	}

	@Test
	public void testInclusaoOrcamentoClienteNovo() {
		Pedido pedido = gPedido.gerarOrcamentoSemCliente();
		Cliente cliente = new Cliente();
		cliente.setNomeFantasia("Vinicius");

		pedido.setCliente(cliente);
		try {
			pedido = pedidoService.inserirOrcamento(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		assertEquals("O orcamento deve estar em orcamento em digitacao apos a inclusao"
				+ pedido.getSituacaoPedido().getDescricao(), SituacaoPedido.ORCAMENTO_DIGITACAO,
				pedido.getSituacaoPedido());
	}

	@Test
	public void testInclusaoPedidoComContatoEmBranco() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		pedido.setContato(new Contato());

		boolean throwed = false;
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O pedido deve conter um contato com informacoes preenchidas", throwed);
	}

	@Test
	public void testInclusaoPedidoCompra() {
		Pedido pedido = gPedido.gerarPedidoCompra();

		assertEquals("Todo pedido de compra incluido deve ir para a digitacao", SituacaoPedido.DIGITACAO,
				pedido.getSituacaoPedido());
		assertEquals("O tipo do pedido de compra deve ser de compra apos a inclusao", TipoPedido.COMPRA,
				pedido.getTipoPedido());
	}

	@Test
	public void testInclusaoPedidoDataEntregaInvalida() {
		Pedido pedido = eBuilder.buildPedido();
		pedido.setDataEntrega(TestUtils.gerarDataOntem());
		boolean throwed = false;
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("A data de entrega nao pode ser anterior a data atual na inclusao de pedidos", throwed);
	}

	@Test
	public void testInclusaoPedidoDigitado() {
		Pedido pedido = gPedido.gerarPedidoSimples();
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertEquals("Todo pedido incluido deve ir para a digitacao", SituacaoPedido.DIGITACAO,
				pedido.getSituacaoPedido());
	}

	@Test
	public void testInclusaoPedidoDigitadoSemVendedorAssociado() {
		Pedido p = gPedido.gerarPedidoRepresentacao();
		Usuario outroVend = gPedido.gerarVendedor();
		outroVend = recarregarEntidade(Usuario.class, outroVend.getId());
		try {
			p = pedidoService.pesquisarPedidoById(pedidoService.copiarPedido(p.getId(), false));
		} catch (BusinessException e1) {
			printMensagens(e1);
		}
		p.setVendedor(outroVend);

		boolean throwed = false;
		try {
			pedidoService.inserirPedido(p);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O Pedido a ser incluido nao esta associado ao vendedor do cliente", throwed);
	}

	@Test
	public void testInclusaoPedidoNomeContatoObrigatorio() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		Contato contato = new Contato();
		contato.setNome("");
		pedido.setContato(contato);

		boolean throwed = false;
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O nome do contato eh obrigatorio para a inclusao do pedido", throwed);
	}

	public void testInclusaoPedidoOrcamento() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		pedido.getCliente().setId(null);

		// Incluindo o pedido no sistema para, posteriormente, inclui-lo como
		// orcamento.
		try {
			pedido = pedidoService.inserirOrcamento(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		assertNotEquals("Pedido deve ser incluido no sistema antes de virar um orcamento", null, pedido.getId());

		assertEquals("Pedido incluido deve ir para orcamento e esta definido como: "
				+ pedido.getSituacaoPedido().getDescricao(), SituacaoPedido.ORCAMENTO_DIGITACAO,
				pedido.getSituacaoPedido());

	}

	@Test
	public void testInclusaoPedidoRepresentacao() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertEquals(
				"Apos a inclusao de um pedido por representacao o tipo de pedido tem que ser configurado como representaacao",
				TipoPedido.REPRESENTACAO, pedido.getTipoPedido());
	}

	@Test
	public void testInclusaoPedidoRevenda() {
		Pedido pedido = gPedido.gerarPedidoSimples();
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		assertEquals("Apos a inclusao de um pedido por revenda o tipo de pedido tem que ser configurado como revenda",
				TipoPedido.REVENDA, pedido.getTipoPedido());
	}

	@Test
	public void testInclusaoPedidoTipoDeEntregaSemRedespacho() {
		Pedido pedido = eBuilder.buildPedido();
		pedido.setTipoEntrega(TipoEntrega.CIF_TRANS);
		boolean throwed = false;
		try {
			pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O pedido foi incluido uma transportadora para redespacho.", throwed);
	}

	@Test
	public void testItemPedidoAguardandoCompra() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(pedido.getRepresentada());

		Integer idPedido = pedido.getId();
		try {
			pedidoService.inserirItemPedido(idPedido, item1);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Set<Integer> ids = new TreeSet<Integer>();
		ids.add(item1.getId());
		try {

			// Estamos alterando o tipo de relacionamento da representada para
			// podermos efetuar a encomenda dos itens para o fornecedor.
			Representada representada = pedido.getRepresentada();
			representada.setTipoRelacionamento(TipoRelacionamento.REPRESENTACAO_FORNECIMENTO);
			representada.addContato(gPedido.gerarContato(ContatoRepresentada.class));

			pedidoService.comprarItemPedido(pedido.getVendedor().getId(), pedido.getRepresentada().getId(), ids);

		} catch (BusinessException e) {
			printMensagens(e);
		}

		situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(SituacaoPedido.ITEM_AGUARDANDO_MATERIAL, situacaoPedido);
	}

	@Test
	public void testPedidoCanceladoDataEntregaInvalida() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();

		try {
			// Inserindo o pedido no sistema
			pedido = pedidoService.inserirPedido(pedido);
			// Cancelando o pedido que sera recuperado adiante para o teste
			// unitario
			pedido.setSituacaoPedido(SituacaoPedido.CANCELADO);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}
		boolean throwed = false;
		try {
			// Alterando o pedido que ja foi cancelado no sistema existente no
			// sistema
			pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O pedido ja foi cancelado e nao pode ser alterado", throwed);
	}

	@Test
	public void testReencomendaItemPedido() {
		Pedido pedidoRevenda = gerarRevendaEncomendada()[1];
		List<ItemPedido> listaItem = pedidoService.pesquisarItemPedidoByIdPedido(pedidoRevenda.getId());
		Integer idItemPedido = listaItem.get(0).getId();
		try {
			pedidoService.reencomendarItemPedido(idItemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}
		ItemPedido itemPedido = pedidoService.pesquisarItemPedidoById(idItemPedido);
		assertEquals("Apos a reencomenda a quantidade reservada deve ser zero", new Integer("0"),
				itemPedido.getQuantidadeReservada());
		assertFalse("Apos a reencomenda a o item nao pode estar encomendado", itemPedido.isEncomendado());

		pedidoRevenda = pedidoService.pesquisarPedidoById(pedidoRevenda.getId());
		assertEquals("O pedido deve aguardar nova encomenda dos itens apos a reencomenda no empacotamento",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, pedidoRevenda.getSituacaoPedido());
	}

	@Test
	public void testReenvioEmailPedido() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		Integer idPedido = null;
		try {
			idPedido = pedidoService.inserirPedido(pedido).getId();
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			gPedido.gerarItemPedido(idPedido);
		} catch (BusinessException e2) {
			printMensagens(e2);
		}

		try {
			pedidoService.enviarPedido(pedido.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		pedido = recarregarEntidade(Pedido.class, pedido.getId());
		Date dtEnvio = pedido.getDataEnvio();
		try {
			pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		// Adicionando um novo item para reenvio
		ItemPedido i = gPedido.gerarItemPedido(pedido.getRepresentada());
		i.setFormaMaterial(FormaMaterial.CH);
		i.setQuantidade(70);

		try {
			pedidoService.inserirItemPedido(idPedido, i);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(pedido.getId(), new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		pedido = recarregarEntidade(Pedido.class, idPedido);
		Date dtReenvio = pedido.getDataEnvio();
		assertEquals("Apos o reenvio de um pedido a data de envio nao pode ser alterada",
				StringUtils.formatarData(dtReenvio), StringUtils.formatarData(dtEnvio));
	}

	@Test
	public void testRefazerPedidoComIPI() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();
		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Integer idPedidoRefeito = null;
		try {
			idPedidoRefeito = pedidoService.refazerPedido(idPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Pedido pedidoRefeito = pedidoService.pesquisarPedidoById(idPedidoRefeito);

		assertNotEquals("O pedido " + idPedido + " foi refeito e nao pode coincidir com o anterior", idPedido,
				idPedidoRefeito);

		assertEquals("Apos o pedido ser refeito ele deve estar em digitacao. Verifique as regras de negocios.",
				SituacaoPedido.DIGITACAO, pedidoRefeito.getSituacaoPedido());

		pedido = recarregarEntidade(Pedido.class, idPedido);
		assertEquals("O pedido " + idPedido + " foi refeito e deve estar na situacao " + SituacaoPedido.CANCELADO,
				SituacaoPedido.CANCELADO, pedido.getSituacaoPedido());

	}

	@Test
	public void testRefazerPedidoRepresentadaSemIPI() {
		Pedido pedido = gPedido.gerarPedidoRepresentacao();

		try {
			pedido = pedidoService.inserirPedido(pedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		Integer idPedido = pedido.getId();
		ItemPedido itemPedido = gPedido.gerarItemPedido(pedido.getRepresentada());
		try {
			pedidoService.inserirItemPedido(idPedido, itemPedido);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		Integer idPedidoRefeito = null;
		try {
			idPedidoRefeito = pedidoService.refazerPedido(idPedido);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		assertNotEquals("O pedido " + idPedido + " foi refeito e nao pode coincidir com o anterior", idPedido,
				idPedidoRefeito);

		pedido = recarregarEntidade(Pedido.class, idPedido);
		assertEquals("O pedido " + idPedido + " foi refeito e deve estar na situacao " + SituacaoPedido.CANCELADO,
				SituacaoPedido.CANCELADO, pedido.getSituacaoPedido());

	}

	@Test
	public void testRevendaComApenasUmItemEncomendado() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		Material mat = gerarMaterial(pedido.getRepresentada());

		ItemPedido iTubo = eBuilder.buildItemPedido();
		iTubo.setMaterial(mat);
		ItemPedido iPeca = eBuilder.buildItemPedidoPeca();
		iPeca.setMaterial(mat);

		Integer idPedido = pedido.getId();
		try {
			pedidoService.inserirItemPedido(idPedido, iTubo);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}
		try {
			pedidoService.inserirItemPedido(idPedido, iPeca);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(
				"O pedido nao contem itens no estoque e deve aguardar o setor de comprar encomendar os itens de um fornecedor",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Representada forn = gRepresentada.gerarFornecedor();
		gPedido.gerarAssociacaoMaterial(mat, forn.getId());

		Set<Integer> listaId = new HashSet<Integer>();
		listaId.add(iTubo.getId());
		try {
			pedidoService.comprarItemPedido(pedido.getComprador().getId(), forn.getId(), listaId);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals("O segundo item do pedido nao foi encomendado pelo setor de compras",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);
	}

	@Test
	public void testRevendaComTodosItensEncomendados() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(pedido.getRepresentada());
		ItemPedido item2 = eBuilder.buildItemPedidoPeca();
		item2.setMaterial(item1.getMaterial());

		Integer idPedido = pedido.getId();
		try {
			pedidoService.inserirItemPedido(idPedido, item1);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}
		try {
			pedidoService.inserirItemPedido(idPedido, item2);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(
				"O pedido nao contem itens no estoque e deve aguardar o setor de comprar encomendar os itens de um fornecedor",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Representada fornecedor = gRepresentada.gerarFornecedor();
		gPedido.gerarAssociacaoMaterial(item1.getMaterial(), fornecedor.getId());
		gPedido.gerarAssociacaoMaterial(item2.getMaterial(), fornecedor.getId());

		gPedido.gerarRevendedor();

		Set<Integer> listaId = new HashSet<Integer>();
		listaId.add(item1.getId());
		listaId.add(item2.getId());
		Integer idPedidoCompra = null;
		try {
			idPedidoCompra = pedidoService
					.comprarItemPedido(pedido.getComprador().getId(), fornecedor.getId(), listaId);
		} catch (BusinessException e) {
			printMensagens(e);
		}

		situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals("O segundo item do pedido nao foi encomendado pelo setor de compras",
				SituacaoPedido.ITEM_AGUARDANDO_MATERIAL, situacaoPedido);

		Pedido pedidoCompra = pedidoService.pesquisarCompraById(idPedidoCompra);

		assertEquals("Apos compra dos itens do pedido a encomenda foi efetuada", SituacaoPedido.DIGITACAO,
				pedidoCompra.getSituacaoPedido());

		try {
			pedidoCompra.setFormaPagamento("A VISTA");
			pedidoCompra.setDataEntrega(TestUtils.gerarDataAmanha());
			pedidoService.enviarPedido(idPedidoCompra, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		assertEquals("Apos o envio da compra dos itens do pedido a compra deve aguardar o recebimento",
				SituacaoPedido.COMPRA_AGUARDANDO_RECEBIMENTO, pedidoCompra.getSituacaoPedido());

		long totalItemRevenda = pedidoService.pesquisarTotalItemPedido(idPedido);
		long totalItemComprado = pedidoService.pesquisarTotalItemPedido(idPedidoCompra);
		assertEquals(totalItemRevenda, totalItemComprado);
	}

	@Test
	public void testRevendaEncomendada() {
		gerarRevendaEncomendada();
	}

	@Test
	public void testRevendaEncomendadaFornecedorInexistente() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(pedido.getRepresentada());

		Integer idPedido = pedido.getId();
		try {
			pedidoService.inserirItemPedido(idPedido, item1);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(
				"O pedido nao contem itens no estoque e deve aguardar o setor de comprar encomendar os itens de um fornecedor",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Representada fornecedor = pedido.getRepresentada();
		fornecedor.setTipoRelacionamento(TipoRelacionamento.REPRESENTACAO_FORNECIMENTO);

		Cliente revendedor = pedido.getCliente();
		revendedor.setTipoCliente(TipoCliente.REVENDEDOR);

		Set<Integer> listaId = new HashSet<Integer>();
		listaId.add(item1.getId());
		boolean throwed = false;
		try {
			pedidoService.comprarItemPedido(pedido.getComprador().getId(), -1, listaId);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O fornecedor para a compra nao existe no sistema", throwed);
	}

	@Test
	public void testRevendaEncomendadaFornecedorInvalido() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(pedido.getRepresentada());

		Integer idPedido = pedido.getId();
		try {
			pedidoService.inserirItemPedido(idPedido, item1);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(
				"O pedido nao contem itens no estoque e deve aguardar o setor de comprar encomendar os itens de um fornecedor",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Representada fornecedor = pedido.getRepresentada();
		fornecedor.setTipoRelacionamento(TipoRelacionamento.REPRESENTACAO);

		Cliente revendedor = pedido.getCliente();
		revendedor.setTipoCliente(TipoCliente.REVENDEDOR);

		Set<Integer> listaId = new HashSet<Integer>();
		listaId.add(item1.getId());
		boolean throwed = false;
		try {
			pedidoService.comprarItemPedido(pedido.getComprador().getId(), fornecedor.getId(), listaId);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O fornecedor eh invalido eh nao pode enfetuar encomenda de itens para pedido de compras", throwed);
	}

	@Test
	public void testRevendaEncomendadaItemPedidoInexistente() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(pedido.getRepresentada());

		Integer idPedido = pedido.getId();
		try {
			pedidoService.inserirItemPedido(idPedido, item1);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(
				"O pedido nao contem itens no estoque e deve aguardar o setor de comprar encomendar os itens de um fornecedor",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Representada fornecedor = pedido.getRepresentada();
		fornecedor.setTipoRelacionamento(TipoRelacionamento.FORNECIMENTO);

		Cliente revendedor = pedido.getCliente();
		revendedor.setTipoCliente(TipoCliente.REVENDEDOR);

		Set<Integer> listaId = new HashSet<Integer>();
		// Inncluindo um item inexistente
		listaId.add(-1);
		boolean throwed = false;
		try {
			pedidoService.comprarItemPedido(pedido.getComprador().getId(), fornecedor.getId(), listaId);
		} catch (BusinessException e) {
			throwed = true;
		}

		assertTrue("Um item inexsitente nao pode ser encomendado, e portanto deve ser validado", throwed);
		situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(
				"O item encomendado nao existe no estoque, entao devemos cria-lo antes de encomendar, portanto o pedido deve estar na mesma situacao",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);
	}

	@Test
	public void testRevendaEncomendadaListaIdItensNulos() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(pedido.getRepresentada());
		ItemPedido item2 = eBuilder.buildItemPedidoPeca();
		item2.setMaterial(item1.getMaterial());

		Integer idPedido = pedido.getId();
		try {
			pedidoService.inserirItemPedido(idPedido, item1);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}
		try {
			pedidoService.inserirItemPedido(idPedido, item2);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(
				"O pedido nao contem itens no estoque e deve aguardar o setor de comprar encomendar os itens de um fornecedor",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Representada fornecedor = pedido.getRepresentada();
		fornecedor.setTipoRelacionamento(TipoRelacionamento.REPRESENTACAO_FORNECIMENTO);

		Cliente revendedor = pedido.getCliente();
		revendedor.setTipoCliente(TipoCliente.REVENDEDOR);

		Set<Integer> listaId = new HashSet<Integer>();
		boolean throwed = false;
		try {
			pedidoService.comprarItemPedido(pedido.getComprador().getId(), fornecedor.getId(), listaId);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("A lista de ids dos itens nao pode ser nula", throwed);
	}

	@Test
	public void testRevendaEncomendadaRevendedorInexistente() {
		Pedido pedido = gPedido.gerarPedidoRevenda();
		ItemPedido item1 = gPedido.gerarItemPedido(pedido.getRepresentada());

		Integer idPedido = pedido.getId();
		try {
			pedidoService.inserirItemPedido(idPedido, item1);
		} catch (BusinessException e1) {
			printMensagens(e1);
		}

		try {
			pedidoService.enviarPedido(idPedido, new AnexoEmail(new byte[] {}));
		} catch (BusinessException e) {
			printMensagens(e);
		}

		SituacaoPedido situacaoPedido = pedidoService.pesquisarSituacaoPedidoById(idPedido);
		assertEquals(
				"O pedido nao contem itens no estoque e deve aguardar o setor de comprar encomendar os itens de um fornecedor",
				SituacaoPedido.ITEM_AGUARDANDO_COMPRA, situacaoPedido);

		Representada fornecedor = pedido.getRepresentada();
		fornecedor.setTipoRelacionamento(TipoRelacionamento.REPRESENTACAO);

		Set<Integer> listaId = new HashSet<Integer>();
		listaId.add(item1.getId());
		boolean throwed = false;
		try {
			pedidoService.comprarItemPedido(pedido.getComprador().getId(), fornecedor.getId(), listaId);
		} catch (BusinessException e) {
			throwed = true;
		}
		assertTrue("O revendedor nao existe no sistemea para efetuar a encomenda de itens para pedido de compras",
				throwed);
	}
}
