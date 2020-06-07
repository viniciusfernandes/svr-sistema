package br.com.svr.vendas.login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.com.svr.service.constante.TipoAcesso;
import br.com.svr.service.entity.PerfilAcesso;
import br.com.svr.service.entity.Usuario;

@Component
@SessionScoped
public class UsuarioInfo {

	private Integer codigoUsuario;
	private boolean compraPermitida;
	private String email;
	private List<TipoAcesso> listaTipoAcesso;

	private String nome;
	private String nomeCompleto;
	private boolean usuarioAutenticado = false;
	private boolean vendaPermitida;

	public UsuarioInfo() {
		System.out.println("Instanciou usuario info: " + new SimpleDateFormat("HH:mm:sss").format(new Date()));
	}

	public Integer getCodigoUsuario() {
		return codigoUsuario;
	}

	public String getDescricaoLogin() {
		return nomeCompleto + " - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
	}

	public String getEmail() {
		return email;
	}

	public String getNome() {
		return nome;
	}

	public void inicializar(Usuario usuario) {
		System.out.println("Inicializando usuario info: " + usuario);
		if (usuario == null) {
			return;
		}

		codigoUsuario = usuario.getId();
		compraPermitida = usuario.isComprador();
		nomeCompleto = usuario.getNomeCompleto();
		nome = usuario.getNome();
		email = usuario.getEmail();
		vendaPermitida = usuario.isVendedor();

		usuarioAutenticado = true;
		listaTipoAcesso = new ArrayList<TipoAcesso>();

		if (usuario.getListaPerfilAcesso() != null) {
			TipoAcesso tipoAcesso = null;
			for (final PerfilAcesso perfil : usuario.getListaPerfilAcesso()) {
				tipoAcesso = TipoAcesso.valueOfBy(perfil.getDescricao());
				if (tipoAcesso != null) {
					listaTipoAcesso.add(tipoAcesso);
				}
			}
		}
	}

	public boolean isAcessoNaoPermitido(TipoAcesso... tipoAcesso) {
		if (tipoAcesso == null) {
			return false;
		}
		return usuarioAutenticado && !listaTipoAcesso.containsAll(Arrays.asList(tipoAcesso));
	}

	public boolean isAcessoPermitido(TipoAcesso... tipoAcesso) {
		if (tipoAcesso == null) {
			return false;
		}

		for (final TipoAcesso tipo : tipoAcesso) {
			if (usuarioAutenticado && listaTipoAcesso.contains(tipo)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAutenticado() {
		return usuarioAutenticado;
	}

	public boolean isCompraPermitida() {
		return compraPermitida;
	}

	public boolean isVendaPermitida() {
		return vendaPermitida;
	}

	public void limpar() {
		usuarioAutenticado = false;
		listaTipoAcesso = null;
	}
}
