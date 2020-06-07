package br.com.svr.vendas.controller;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.svr.service.AutenticacaoService;
import br.com.svr.service.entity.Usuario;
import br.com.svr.service.exception.AutenticacaoException;
import br.com.svr.vendas.controller.anotacao.AcessoLivre;
import br.com.svr.vendas.controller.anotacao.Servico;
import br.com.svr.vendas.login.UsuarioInfo;

@Resource
public class LoginController extends AbstractController {

	@Servico
	private AutenticacaoService autenticacaoService;
	private final HttpSession sessao;
	// Variavel necessaria para efetuar a inicializacao das infos do usuario
	// autenticado.
	private final UsuarioInfo usuarioInfo;

	public LoginController(Result result, UsuarioInfo usuarioInfo, HttpSession httpSession) {
		super(result, usuarioInfo);
		this.usuarioInfo = usuarioInfo;
		sessao = httpSession;
	}

	@AcessoLivre
	@Post("login/entrar")
	public void logar(String email, String senha) {
		try {
			final Usuario usuario = autenticacaoService.autenticar(email, senha);
			System.out.println("Logou usuario: " + usuario != null);
			if (usuario == null || !usuario.isAtivo()) {
				gerarListaMensagemAlerta("Falha na autenticação. Usuario/Senha inválido(s)");
			}

			usuarioInfo.inicializar(usuario);
			redirecTo(MenuController.class).menuHome();
			System.out.println("Chegou usuario: " + email + " e senha: " + senha + ". Recuperou o usuario: "
					+ (usuario == null ? false : usuario.getEmail() + " com a senha: " + usuario.getSenha()));

		}
		catch (final AutenticacaoException e) {
			this.gerarListaMensagemErro(e.getListaMensagem());
			forwardTo(ErroController.class).erroHome();
		}
	}

}
