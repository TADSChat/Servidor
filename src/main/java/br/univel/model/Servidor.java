package br.univel.model;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.univel.control.ObjectDao;
import br.univel.view.PainelPrincipal;
import br.univel.view.PainelServidor;
import br.univel.view.PainelUsuarios;
import common.Criptografia;
import common.EntidadeUsuario;
import common.InterfaceServidor;
import common.InterfaceUsuario;
import common.Status;
import common.TipoMensagem;

public class Servidor implements InterfaceServidor, Runnable {

	private static String ipServidor;
	private static Integer portaServidor;
	private static InterfaceServidor meuServidor;

	private Thread threadMonitor;
	private int index = -1;
	private static Servidor servidor;
	private static Registry registry;

	private static Map<Integer, InterfaceUsuario> mapaUsuarios = new HashMap<>();
	private static List<EntidadeUsuario> listaUsuarios;
	private static EntidadeUsuario usuarioRetorno;

	private Servidor(String ipServidor, Integer portaServidor) {
		this.ipServidor = ipServidor;
		this.portaServidor = portaServidor;
		this.listaUsuarios = PainelUsuarios.getListaUsuarios();
		this.threadMonitor = new Thread(this);
		this.threadMonitor.start();
	}

	/**
	 * @return the mapaUsuarios
	 */
	public static Map<Integer, InterfaceUsuario> getMapaUsuarios() {
		return mapaUsuarios;
	}

	private void atualizarUsuarios() {
		if (mapaUsuarios != null) {
			try {
				for (InterfaceUsuario usuario : mapaUsuarios.values()) {
					try {
						usuario.receberListaParticipantes(listaUsuarios);
					} catch (Exception e) {
						PainelServidor.setLog("Usuario inativo foi removido da lista \n" + e.toString());
						mapaUsuarios.remove(mapaUsuarios.entrySet());
						continue;
					}

					usuario.receberListaParticipantes(listaUsuarios);
				}
			} catch (NullPointerException nullPointer) {
				return;
			} catch (Exception exception) {
				PainelServidor.setLog("Erro ao atualizar lista de usuarios \n " + exception.toString());
				return;
			}
		}
		PainelServidor.setLog("Atualizando status dos usuarios");
	}

	public static EntidadeUsuario getUsuario(final Integer idUsuario) {
		usuarioRetorno = null;
		PainelUsuarios.getListaUsuarios().forEach(usuario -> {
			if (usuario.getId() == idUsuario) {
				usuarioRetorno = usuario;
				return;
			}
		});
		return usuarioRetorno;
	}

	@Override
	public EntidadeUsuario conectarChat(EntidadeUsuario usuario, InterfaceUsuario interfaceUsuario)
			throws RemoteException {
		if (usuario == null) {
			return null;
		}
		PainelServidor.setLog(String.format("Usuario [%s] esta tentando se conectar", usuario.getEmail()));
		String senha = Criptografia.criptografar(usuario.getSenha());

		EntidadeUsuario usuarioValido = (EntidadeUsuario) ObjectDao.consultarByQuery(
				String.format("from EntidadeUsuario where user_email like '%s'", usuario.getEmail(), senha));

		if (usuarioValido == null) {
			PainelServidor.setLog(
					String.format("Usuario [%s] tentou se conectar, mas nao possui cadastro", usuario.getEmail()));
			throw new RemoteException("Usuario nao cadastrado!");
		}

		if (!usuarioValido.getSenha().equals(senha)) {
			PainelServidor.setLog(
					String.format("Usuario [%s] tentou se conectar com uma senha invalida", usuario.getEmail()));
			throw new RemoteException("Senha invalida!");
		}

		if (mapaUsuarios.get(usuarioValido.getId()) != null) {
			PainelServidor.setLog(String.format("Usuario %s [%s] tentou se conectar com uma sessao ja ativa",
					usuarioValido.getNome(), usuarioValido.getEmail()));
			throw new RemoteException("Este usuario ja este conectado!");
		}

		usuarioValido.setStatus(Status.ONLINE);
		usuarioValido.setIpConexao(usuario.getIpConexao());
		usuarioValido.setPortaConexao(usuario.getPortaConexao());
		listaUsuarios.add(usuarioValido);
		mapaUsuarios.put(usuarioValido.getId(), interfaceUsuario);

		PainelUsuarios.atualizarTabela();
		atualizarUsuarios();

		PainelServidor
				.setLog(String.format("Usuario %s [%s] se conectou", usuarioValido.getNome(), usuarioValido.getEmail()));

		return usuarioValido;
	}

	@Override
	public void desconectarChat(EntidadeUsuario usuario) throws RemoteException {
		if (mapaUsuarios.get(usuario.getId()) == null) {
			PainelServidor.setLog(String.format("Usuario %s [%s] tentou se desconectar sem uma sessao ativa",
					usuario.getNome(), usuario.getEmail()));
			throw new RemoteException("Voce nao possui uma sessao ativa!");
		}

		index = -1;
		listaUsuarios.forEach(usuarioLista -> {
			if (usuarioLista.getId().equals(usuario.getId())) {
				index = listaUsuarios.indexOf(usuarioLista);
			}
		});

		if (index >= 0) {
			listaUsuarios.remove(index);
		}

		mapaUsuarios.remove(usuario.getId());
		PainelServidor.setLog(String.format("Usuario %s [%s] se desconectou", usuario.getNome(), usuario.getEmail()));

		PainelUsuarios.atualizarTabela();
		atualizarUsuarios();
	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, EntidadeUsuario destinatario, String mensagem)
			throws RemoteException {
		if (mapaUsuarios.get(destinatario.getId()) == null) {
			PainelServidor.setLog(String.format("Usuario %s [%s] tentou enviar uma mensagem ao usuario inativo %s [%s]",
					remetente.getNome(), remetente.getEmail(), destinatario.getNome(), destinatario.getEmail()));
			throw new RemoteException("Este usuario esta desconectado!");
		}

		mapaUsuarios.get(destinatario.getId()).receberMensagem(remetente, TipoMensagem.PRIVADA, mensagem);
		PainelServidor.setLog(String.format("Usuario %s [%s] enviou uma mensagem ao usuario %s [%s]", remetente.getNome(),
				remetente.getEmail(), destinatario.getNome(), destinatario.getEmail()));
	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, String mensagem) throws RemoteException {
		for (Entry<Integer, InterfaceUsuario> usuarios : mapaUsuarios.entrySet()) {
			if (usuarios.getKey().equals(remetente.getId())) {
				continue;
			} else {
				try {
					usuarios.getValue().receberMensagem(remetente, TipoMensagem.PUBLICA, mensagem);
				} catch (Exception ex) {
					PainelServidor.setLog(String.format("Erro ao enviar a mensagem de %s [%s] para todos os contatos",
							remetente.getNome(), remetente.getEmail()));
				}
			}
		}
		PainelServidor.setLog(String.format("Usuario %s [%s] enviou uma mensagem para todos os contatos",
				remetente.getNome(), remetente.getEmail()));
	}

	@Override
	public boolean atualizarStatus(EntidadeUsuario usuario) throws RemoteException {
		if (!mapaUsuarios.containsKey(usuario.getId())) {
			PainelServidor.setLog(String.format("Usuario %s [%s] tentou alterar o status sem estar conectado",
					usuario.getNome(), usuario.getEmail()));
			throw new RemoteException("Sessao expirada, tente relogar!");
		}

		index = -1;
		listaUsuarios.forEach(usuarioLista -> {
			if (usuarioLista.getId().equals(usuario.getId())) {
				index = listaUsuarios.indexOf(usuarioLista);
			}
		});

		if (index >= 0) {
			EntidadeUsuario usuarioAtualizar = listaUsuarios.get(index);
			String statusAntigo = usuarioAtualizar.getStatus().toString();

			usuarioAtualizar.setStatus(usuario.getStatus());
			listaUsuarios.set(index, usuarioAtualizar);

			PainelServidor.setLog(String.format("Usuario %s [%s] alterou o status de %s para %s", usuario.getNome(),
					usuario.getEmail(), statusAntigo.toString(), usuario.getStatus()));

			PainelUsuarios.atualizarTabela();
			atualizarUsuarios();

			return true;
		}

		throw new RemoteException("Sessao expirada, tente relogar!");
	}

	@Override
	public void run() {
		try {
			meuServidor = (InterfaceServidor) UnicastRemoteObject.exportObject(this, 0);
			registry = LocateRegistry.createRegistry(portaServidor);
			registry.rebind(InterfaceServidor.NOME, meuServidor);
			PainelServidor.setLog("Servidor iniciado com sucesso!");
		} catch (RemoteException e) {
			PainelServidor.setLog("Erro ao startar o servidor: \n" + e.toString());
		}

	}

	/**
	 * @return the servidor
	 */
	public static Servidor getServidor() {
		if (servidor == null) {
			servidor = new Servidor(PainelPrincipal.getIpServidor(), PainelPrincipal.getPortaServidor());
		}
		return servidor;
	}

	public static void iniciarServidor() {
		mapaUsuarios = new HashMap<>();
		getServidor();
		PainelServidor.getButtonIniciarServico().setEnabled(false);
		PainelServidor.getButtonPararServico().setEnabled(true);
	}

	public static void pararServidor() {
		try {
			UnicastRemoteObject.unexportObject(registry, true);
			Servidor.getServidor().desconectarTodos();

			registry = null;
			meuServidor = null;
			servidor = null;
			mapaUsuarios = null;
			listaUsuarios = null;
			
			PainelServidor.getButtonIniciarServico().setEnabled(true);
			PainelServidor.getButtonPararServico().setEnabled(false);

			PainelServidor.setLog("Servidor Finalizado!");
		} catch (Exception e) {
			PainelServidor.setLog("Erro ao desligar o servidor!\n" + e.toString());
		}
	}

	@Override
	public boolean alterarSenha(EntidadeUsuario usuario) throws RemoteException {
		if (!mapaUsuarios.containsKey(usuario.getId())) {
			PainelServidor.setLog(String.format("Usuario %s [%s] tentou alterar a senha sem estar conectado",
					usuario.getNome(), usuario.getEmail()));
			throw new RemoteException("Sessï¿½o expirada, tente relogar!");
		}

		ObjectDao.alterar(usuario);

		PainelServidor.setLog(String.format("Usuario %s [%s] alterou a senha", usuario.getNome(), usuario.getEmail()));

		return true;
	}

	/**
	 * @return the listaUsuarios
	 */
	public static synchronized List<EntidadeUsuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void desconectarUsuario(EntidadeUsuario usuario) {
		try {
			for (Entry<Integer, InterfaceUsuario> usuarioMapa : mapaUsuarios.entrySet()) {
				if (usuarioMapa.getKey().equals(usuario.getId())) {
					usuarioMapa.getValue().desconectarForcado();
				}
			}
		} catch (Exception e) {
			try {
				PainelServidor.setLog(String.format("Usuario %s [%s] foi desconectado do servidor", usuario.getNome(),
						usuario.getEmail()));
				desconectarChat(usuario);
			} catch (Exception e1) {
				PainelServidor.setLog(String.format("Erro ao desconectar o usuario %s [%s] \n %s", usuario.getNome(),
						usuario.getEmail(), e.toString()));
			}
		}
	}

	public void desconectarTodos() {
		try {
			for (Entry<Integer, InterfaceUsuario> usuarioMapa : mapaUsuarios.entrySet()) {
				usuarioMapa.getValue().desconectarForcado();
			}
			PainelServidor.setLog("Todos os usuários foram desconectados.");
		} catch (Exception e) {
			PainelServidor.setLog(String.format("Erro ao desconectar todos os usuarios \n %s", e.toString()));
		}

	}
}
