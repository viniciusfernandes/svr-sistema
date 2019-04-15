package br.com.svr.service.validacao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import br.com.svr.util.NumeroUtils;

public final class ValidadorInformacao {

	public static void addMensagem(List<String> listaMensagem, String mensagem, Object valIdentif) {
		if (valIdentif == null) {
			listaMensagem.add(mensagem);
		} else {
			listaMensagem.add("Item " + valIdentif + ". " + mensagem);
		}
	}

	public static void preencherListaMensagemValidacao(Object obj, List<String> listaMensagem) {
		InformacaoValidavel informacao = null;
		if ((informacao = obj.getClass().getAnnotation(InformacaoValidavel.class)) == null) {
			throw new IllegalArgumentException("A classe " + obj.getClass()
					+ " n�o pode ser validada pelo mecanismo de verificac�o de preenchimento dos campos. "
					+ "No caso em que o campo seja um ENUM, remova o atributo cascata = true");
		}

		// variavel sera utilizada tambem para verificar se o campo eh uma
		// String
		int COMPRIMENTO_STRING = -1;
		Field[] camposValidaveis = recuperarCamposValidaveis(obj);
		boolean isString = false;
		int[] tamanhos = null;
		String[] opcoes = null;
		boolean ok = false;
		Object valIdentif = !informacao.campoIdentificacao().isEmpty() ? recuperarConteudo(
				informacao.campoIdentificacao(), obj) : null;

		Object valorCond = null;
		String nomeCond = null;
		if (!informacao.campoCondicional().isEmpty()) {
			valorCond = recuperarConteudo(informacao.campoCondicional(), obj);
			nomeCond = informacao.nomeExibicaoCampoCondicional();
		}

		for (Field campo : camposValidaveis) {
			informacao = campo.getAnnotation(InformacaoValidavel.class);

			if (informacao == null) {
				continue;
			}

			Object conteudoCampo = recuperarConteudo(campo, obj);
			// Esse bloco deve preceder todos os outros pois eh uma
			// pre-avaliacao do conteudo dos campos e podera alterar o
			// conteudo
			// de acordo com a marcacao, sendo que esse novo conteudo sera
			// revalidado posteiormente
			if (valorCond != null) {
				if (informacao.tiposNaoPermitidos().length > 0) {
					for (String c : informacao.tiposNaoPermitidos()) {
						if (conteudoCampo != null && valorCond.equals(c)) {
							// listaMensagem.add("\"" +
							// informacao.nomeExibicao() +
							// "\" n�o deve ser preenchido para o \"" +
							// nomeCond
							// + " = " + valorCond + "\"");

							// No caso em que um determinado tipo nao for
							// permitido iremos anular o conteudo para
							// facilitar
							// a implementacao de regras de negocio,
							// evitando
							// assim novas verificacoes de nulidade do
							// conteudo
							// evitando uma quantidade de if/else
							conteudoCampo = null;
							setConteudo(campo, obj, null);
							break;
						}
					}
				}

				if (informacao.tiposObrigatorios().length > 0) {
					for (String c : informacao.tiposObrigatorios()) {
						if (conteudoCampo == null && valorCond.equals(c)) {
							addMensagem(listaMensagem, "\"" + informacao.nomeExibicao() + "\" � obrigat�rio para \""
									+ nomeCond + " = " + valorCond + "\"", valIdentif);
							break;
						}
					}
				}

				if (informacao.tiposPermitidos().length > 0) {
					ok = true;
					// Verificando se o tipo selecionado eh diferente de
					// todos
					// os tipos permitidos para depois invalidar
					for (String c : informacao.tiposPermitidos()) {
						// Condicao indicando que encontrou o tipo na lista
						// de
						// tipos permitidos
						if (ok = valorCond.equals(c)) {
							break;
						}
					}

					if (conteudoCampo != null && !ok) {
						addMensagem(listaMensagem, "\"" + informacao.nomeExibicao()
								+ "\" n�o deve ser preenchido para o \"" + nomeCond + " = " + valorCond + "\"",
								valIdentif);
					}
				}
			}

			if (informacao.obrigatorio() && conteudoCampo == null) {
				addMensagem(listaMensagem, informacao.nomeExibicao() + " � obrigat�rio", valIdentif);
				continue;
			}

			if ((informacao.relacionamentoObrigatorio() && (conteudoCampo == null || recuperarId(conteudoCampo) == null))) {
				addMensagem(listaMensagem, informacao.nomeExibicao() + " deve ser associado", valIdentif);
				continue;
			}

			if (informacao.numerico() && informacao.estritamentePositivo()
					&& (conteudoCampo != null && Double.valueOf(conteudoCampo.toString()) <= 0)) {
				addMensagem(listaMensagem, informacao.nomeExibicao() + " deve ser positivo", valIdentif);
				continue;
			}

			if (informacao.numerico()
					&& informacao.intervaloNumerico().length > 0
					&& (conteudoCampo != null && (Double) conteudoCampo >= informacao.intervaloNumerico()[0] && (Double) conteudoCampo <= informacao
							.intervaloNumerico()[1])) {
				addMensagem(listaMensagem,
						informacao.nomeExibicao() + " deve estar dentro o intervalo "
								+ informacao.intervaloNumerico()[0] + " � " + informacao.intervaloNumerico()[1],
						valIdentif);

				continue;
			}

			if (informacao.numerico() && informacao.positivo()
					&& (conteudoCampo != null && Double.valueOf(conteudoCampo.toString()) < 0)) {
				addMensagem(listaMensagem, informacao.nomeExibicao() + " n�o deve ser negativo", valIdentif);
				continue;
			}

			if (informacao.iteravel()) {
				Collection<?> lista = (Collection<?>) conteudoCampo;
				if (lista != null) {
					for (Object object : lista) {
						preencherListaMensagemValidacao(object, listaMensagem);
					}
				}
				continue;
			}

			// Essa variavel sera sempre ZERO no caso em que o conteudo nao
			// seja
			// uma
			// string
			isString = conteudoCampo instanceof String;
			if (isString && informacao.trim()) {
				conteudoCampo = trim(campo, obj, conteudoCampo);
			}

			COMPRIMENTO_STRING = isString ? conteudoCampo.toString().trim().length() : -1;
			// Esse bloco deve anteceder a validacao do comprimento da
			// String
			// pois ele complementa seu conteudo com prefixos
			if (COMPRIMENTO_STRING > 0 && informacao.prefixo().length() > 0 && informacao.tamanho() > 0
					&& COMPRIMENTO_STRING < informacao.tamanho()) {
				int qtde = informacao.tamanho() - COMPRIMENTO_STRING;
				StringBuilder s = new StringBuilder();
				for (int i = 0; i < qtde; i++) {
					s.append(informacao.prefixo());
				}
				conteudoCampo = s.append(conteudoCampo.toString()).toString();
				COMPRIMENTO_STRING = conteudoCampo.toString().length();
				setConteudo(campo, obj, conteudoCampo);
			}

			// Muito importante que essa validacao seja feita antes das
			// outras
			// pois apos a remocao do caracteres invalidos esses valores
			// deverao
			// ser validados, por isso esse condicional nao tem a instrucao
			// de
			// fim de execucao "continue"
			if (COMPRIMENTO_STRING > 0 && informacao.substituicao().length > 1) {
				conteudoCampo = conteudoCampo.toString().replaceAll(informacao.substituicao()[0],
						informacao.substituicao()[1]);
				COMPRIMENTO_STRING = conteudoCampo.toString().length();

				setConteudo(campo, obj, conteudoCampo);
			}

			try {
				if (informacao.intervaloComprimento().length > 0
						&& COMPRIMENTO_STRING >= 0
						&& (COMPRIMENTO_STRING < informacao.intervaloComprimento()[0] || COMPRIMENTO_STRING > informacao
								.intervaloComprimento()[1])) {

					addMensagem(listaMensagem,
							informacao.nomeExibicao() + " deve conter de " + informacao.intervaloComprimento()[0]
									+ " a " + informacao.intervaloComprimento()[1] + " caracteres. Foi enviado "
									+ COMPRIMENTO_STRING + " caracteres", valIdentif);
					continue;
				}
			} catch (Exception e) {
				System.out.println("xxxxxxxxxxx");
			}

			if (conteudoCampo != null && informacao.tamanho() >= 0 && COMPRIMENTO_STRING != informacao.tamanho()) {
				addMensagem(listaMensagem, informacao.nomeExibicao() + " deve conter apenas " + informacao.tamanho()
						+ " caracteres. Foi enviado " + COMPRIMENTO_STRING + " caracteres", valIdentif);
				continue;
			}

			tamanhos = informacao.tamanhos();
			if (tamanhos.length > 0) {
				ok = false;
				for (int i = 0; i < tamanhos.length; i++) {
					if (COMPRIMENTO_STRING == tamanhos[i]) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					addMensagem(listaMensagem,
							informacao.nomeExibicao() + " deve conter um dos tamanhos \"" + Arrays.toString(tamanhos)
									+ "\" mas cont�m o tamanho de \"" + COMPRIMENTO_STRING + "\"", valIdentif);
				}
				continue;
			}

			opcoes = informacao.opcoes();
			if (opcoes.length > 0) {
				ok = false;
				for (int i = 0; i < opcoes.length; i++) {
					if (conteudoCampo.equals(opcoes[i])) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					addMensagem(listaMensagem,
							informacao.nomeExibicao() + " deve conter um dos valores \"" + Arrays.toString(opcoes)
									+ "\" mas cont�m o valores de \"" + conteudoCampo + "\"", valIdentif);
				}
				continue;
			}

			if (informacao.decimal().length >= 2 && conteudoCampo != null) {
				campo.setAccessible(true);
				try {
					campo.set(obj, NumeroUtils.arredondar((Double) conteudoCampo, informacao.decimal()[1]));
				} catch (Exception e) {
					addMensagem(listaMensagem, "Falha no arredondamento decimal do campo " + campo, valIdentif);
				} finally {
					campo.setAccessible(false);
				}
				continue;
			}

			if (!TipoDocumento.NAO_EH_DOCUMENTO.equals(informacao.tipoDocumento()) && COMPRIMENTO_STRING > 0
					&& !ValidadorDocumento.isValido(informacao.tipoDocumento(), conteudoCampo.toString())) {
				addMensagem(listaMensagem, informacao.nomeExibicao() + " n�o � v�lido", valIdentif);
				continue;
			}

			if (COMPRIMENTO_STRING > 0 && informacao.padrao().length > 0) {
				ok = false;
				for (String p : informacao.padrao()) {
					if (ok = conteudoCampo.toString().matches(p)) {
						break;
					}
				}

				if (!ok) {
					addMensagem(listaMensagem,
							informacao.nomeExibicao()
									+ " n�o est� no formato padronizado correto"
									+ (informacao.padraoExemplo().length() > 0 ? " \"" + informacao.padraoExemplo()
											+ "\"" : "") + ". Enviado \"" + conteudoCampo + "\"", valIdentif);

				}
				continue;
			}

			if (informacao.cascata() && conteudoCampo != null) {
				preencherListaMensagemValidacao(conteudoCampo, listaMensagem);
			}
		}
	}

	private static Field[] recuperarCamposValidaveis(Object obj) {
		final InformacaoValidavel informacao = obj.getClass().getAnnotation(InformacaoValidavel.class);
		final Field[] camposClasse = obj.getClass().getDeclaredFields();

		if (informacao.validarHierarquia()) {
			Field[] camposSuperClasse = obj.getClass().getSuperclass().getDeclaredFields();
			Field[] campos = new Field[camposClasse.length + camposSuperClasse.length];

			for (int i = 0; i < camposClasse.length; i++) {
				campos[i] = camposClasse[i];
			}

			int k = camposClasse.length;
			for (int i = 0; i < camposSuperClasse.length; i++) {
				campos[i + k] = camposSuperClasse[i];

			}

			return campos;
		} else {
			return camposClasse;
		}
	}

	private static Object recuperarConteudo(Field campo, Object obj) {
		try {
			campo.setAccessible(true);
			Object conteudoCampo = campo.get(obj);
			return conteudoCampo;
		} catch (Exception e) {
			throw new IllegalStateException("O valor do campo " + campo.getName() + " do objeto " + obj.getClass()
					+ " n�o pode ser acessado", e);
		} finally {
			campo.setAccessible(false);
		}
	}

	private static Object recuperarConteudo(String nomeCampo, Object obj) {
		Field campo = null;
		try {
			campo = obj.getClass().getDeclaredField(nomeCampo);
			campo.setAccessible(true);
			Object conteudoCampo = campo.get(obj);
			return conteudoCampo;
		} catch (Exception e) {
			throw new IllegalStateException("O valor do campo pelo nome \"" + nomeCampo + "\" do objeto "
					+ obj.getClass() + " n�o pode ser acessado", e);
		} finally {
			if (campo != null) {
				campo.setAccessible(false);
			}
		}
	}

	private static Object recuperarId(Object conteudoCampo) {
		try {
			return conteudoCampo.getClass().getMethod("getId").invoke(conteudoCampo, (Object[]) null);
		} catch (Exception e) {
			throw new IllegalStateException(
					"O objeto do tipo "
							+ conteudoCampo.getClass()
							+ " n�o possui um de acesso ao campo de identifica��o ID. Implementar um metodo de acesso getId(), mas no caso de ENUM, substitua pelo atributo \"obrigat�rio\"");
		}

	}

	private static void setConteudo(Field campo, Object obj, Object valor) {
		try {
			campo.setAccessible(true);
			campo.set(obj, valor);
		} catch (Exception e) {
			throw new IllegalStateException("O campo " + campo.getName() + " do objeto " + obj.getClass()
					+ " n�o pode ter seu novo valor configurado", e);
		} finally {
			campo.setAccessible(false);
		}
	}

	private static Object trim(Field campo, Object obj, Object conteudoCampo) {
		conteudoCampo = conteudoCampo.toString().trim();
		try {
			campo.setAccessible(true);
			campo.set(obj, conteudoCampo);
		} catch (Exception e) {
			throw new IllegalStateException("O valor do campo " + campo.getName() + " do objeto " + obj.getClass()
					+ " nao pode ter os espacos em branco removidos cujo conteudo eh \"" + conteudoCampo + "\"", e);
		} finally {
			campo.setAccessible(false);
		}
		return conteudoCampo;
	}

	public static void validar(Object obj) throws InformacaoInvalidaException {
		List<String> listaMensagem = new ArrayList<String>(20);
		preencherListaMensagemValidacao(obj, listaMensagem);

		if (listaMensagem.size() != 0) {
			throw new InformacaoInvalidaException(listaMensagem);
		}
	}
}
