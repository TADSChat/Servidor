package br.univel.model;

import java.io.File;
import java.rmi.NoSuchObjectException;
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
						e.printStackTrace();
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
		PainelServidor.setLog(String.format("Usuario %s esta tentando se concetar", usuario.getEmail()));
		String senha = Criptografia.criptografar(usuario.getSenha());

		EntidadeUsuario usuarioValido = (EntidadeUsuario) ObjectDao.consultarByQuery(
				String.format("from EntidadeUsuario where user_email like '%s' and user_password like '%s'",
						usuario.getEmail(), senha));

		if (usuarioValido == null) {
			PainelServidor.setLog(
					String.format("Usuario [%s] tentou se conectar, mas não possui cadastro", usuario.getEmail()));
			return null;
		}

		if (mapaUsuarios.get(usuarioValido.getId()) != null) {
			PainelServidor.setLog(
					String.format("Usuario %s tentou se conectar com uma sessao ja ativa", usuarioValido.getEmail()));
			return null;
		}

		usuarioValido.setStatus(Status.ONLINE);
		usuarioValido.setIpConexao(usuario.getIpConexao());
		usuarioValido.setPortaConexao(usuario.getPortaConexao());
		listaUsuarios.add(usuarioValido);
		mapaUsuarios.put(usuarioValido.getId(), interfaceUsuario);

		PainelUsuarios.atualizarTabela();
		atualizarUsuarios();

		PainelServidor.setLog(String.format("Usuario %s se conectou", usuarioValido.getEmail()));

		return usuarioValido;
	}

	@Override
	public void desconectarChat(EntidadeUsuario usuario) throws RemoteException {
		if (mapaUsuarios.get(usuario.getId()) == null) {
			PainelServidor
					.setLog(String.format("Usuario %s tentou se desconectar sem uma sessao ativa", usuario.getEmail()));
			return;
		}

		index = -1;
		listaUsuarios.forEach(usuarioLista -> {
			if (usuarioLista.getId().equals(usuario.getId())) {
				index = listaUsuarios.indexOf(usuarioLista);
				System.out.println("DEsconectando o usuario " + index);
			}
		});

		if (index >= 0) {
			listaUsuarios.remove(index);
			System.out.println("removendo indice " + index);
		}

		mapaUsuarios.remove(usuario.getId());
		PainelServidor.setLog(String.format("Usuario %s se desconectou", usuario.getEmail()));

		PainelUsuarios.atualizarTabela();
		atualizarUsuarios();
	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, EntidadeUsuario destinatario, String mensagem)
			throws RemoteException {
		if (mapaUsuarios.get(destinatario.getId()) == null) {
			PainelServidor.setLog(String.format("Usuario %s tentou enviar uma mensagem ao usuario inativo %s",
					remetente.getEmail(), destinatario.getEmail()));
			return;
		}

		mapaUsuarios.get(destinatario.getId()).receberMensagem(remetente, mensagem);
		PainelServidor.setLog(String.format("Usuario %s enviou uma mensagem ao usuario %s", remetente.getEmail(),
				destinatario.getEmail()));
	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, String mensagem) throws RemoteException {
		for (Entry<Integer, InterfaceUsuario> usuarios : mapaUsuarios.entrySet()) {
			if (usuarios.getKey().equals(remetente.getId())) {
				continue;
			} else {
				try {
					usuarios.getValue().receberMensagem(remetente, mensagem);
				} catch (Exception ex) {
					PainelServidor.setLog(String.format("Erro ao enviar a mensagem de %s para todos os contatos",
							remetente.getEmail()));
				}
			}
		}
		PainelServidor
				.setLog(String.format("Usuario %s enviou uma mensagem para todos os contatos", remetente.getEmail()));
	}

	@Override
	public boolean atualizarStatus(EntidadeUsuario usuario) throws RemoteException {
		if (!mapaUsuarios.containsKey(usuario.getId())) {
			PainelServidor.setLog(
					String.format("Usuario %s tentou alterar o status sem estar conectado", usuario.getEmail()));
			return false;
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

			PainelServidor.setLog(String.format("Usuario %s alterou o status de %s para %s", usuario.getNome(),
					statusAntigo.toString(), usuario.getStatus()));

			PainelUsuarios.atualizarTabela();
			atualizarUsuarios();

			return true;
		}

		return false;
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

			registry = null;
			meuServidor = null;
			servidor = null;
			mapaUsuarios = null;
			listaUsuarios = null;

			PainelServidor.getButtonIniciarServico().setEnabled(true);
			PainelServidor.getButtonPararServico().setEnabled(false);

			PainelServidor.setLog("Servidor Finalizado!");
		} catch (NoSuchObjectException e) {
			PainelServidor.setLog("Erro ao desligar o servidor!\n" + e.toString());
		}
	}

	@Override
	public void enviarArquivo(EntidadeUsuario remetente, EntidadeUsuario destinatario, File arquivo)
			throws RemoteException {

		if (mapaUsuarios.get(destinatario.getId()) == null) {
			PainelServidor.setLog(String.format("Usuario %s tentou enviar um arquivo ao usuario inativo %s",
					remetente.getNome(), destinatario.getNome()));
			return;
		}

		mapaUsuarios.get(destinatario.getId()).receberArquivo(remetente, arquivo);
		PainelServidor.setLog(String.format("Usuario %s enviou um arquivo ao usuario %s", remetente.getNome(),
				destinatario.getNome()));

	}

	@Override
	public InterfaceUsuario buscarDestinatario(EntidadeUsuario remetente, EntidadeUsuario destinatario)
			throws RemoteException {
		if (mapaUsuarios.get(destinatario.getId()) == null) {
			PainelServidor.setLog(String.format("Usuario %s tentou enviar um arquivo ao usuario inativo %s",
					remetente.getNome(), destinatario.getNome()));
			return null;
		}

		PainelServidor.setLog(String.format("Usuario %s solicitou o envio de um arquivo ao usuario %s",
				remetente.getNome(), destinatario.getNome()));
		return mapaUsuarios.get(destinatario.getId());
	}

	@Override
	public boolean alterarSenha(EntidadeUsuario usuario) throws RemoteException {
		if (!mapaUsuarios.containsKey(usuario.getId())) {
			PainelServidor
					.setLog(String.format("Usuario %s tentou alterar a senha sem estar conectado", usuario.getNome()));
			return false;
		}

		ObjectDao.alterar(usuario);

		PainelServidor.setLog(String.format("Usuario %s alterou a senha", usuario.getNome()));

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
				PainelServidor.setLog(String.format("Usuario [%s] foi desconectado do servidor", usuario.getNome()));
				desconectarChat(usuario);
			} catch (Exception e1) {
				PainelServidor.setLog(
						String.format("Erro ao desconectar o usuario [%s] \n %s", usuario.getNome(), e.toString()));
			}
		}
	}
}
