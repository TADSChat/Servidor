package br.univel.model;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.univel.control.Md5Util;
import br.univel.control.ObjectDao;
import br.univel.view.PainelPrincipal;
import br.univel.view.PainelServidor;
import common.Arquivo;
import common.EntidadeUsuario;
import common.InterfaceServidor;
import common.InterfaceUsuario;
import common.Status;

public class Servidor implements InterfaceServidor, Runnable {

	private static String ipServidor;
	private static Integer portaServidor;
	private static InterfaceServidor meuServidor;

	private Thread threadMonitor;
	private static Servidor servidor;
	private static Registry registry;

	private static Map<EntidadeUsuario, InterfaceUsuario> mapaUsuarios = new HashMap<>();

	private Servidor(String ipServidor, Integer portaServidor) {
		this.ipServidor = ipServidor;
		this.portaServidor = portaServidor;
		this.threadMonitor = new Thread(this);
		this.threadMonitor.start();
	}

	/**
	 * @return the mapaUsuarios
	 */
	public static Map<EntidadeUsuario, InterfaceUsuario> getMapaUsuarios() {
		return mapaUsuarios;
	}

	private void atualizarStatusUsuarios() {
		if (mapaUsuarios != null) {
			try {
				for (InterfaceUsuario usuario : mapaUsuarios.values()) {
					usuario.receberListaParticipantes(new ArrayList<EntidadeUsuario>(mapaUsuarios.keySet()));
				}
			} catch (Exception e) {
				PainelServidor.setLog("Erro ao atualizar lista de usuarios \n " + e.toString());
				return;
			}
		}
		PainelServidor.setLog("Atualizando status dos usuarios");
	}

	@Override
	public EntidadeUsuario conectarChat(EntidadeUsuario usuario, InterfaceUsuario interfaceUsuario)
			throws RemoteException {
		PainelServidor.setLog("Alguem esta tentando se conectar");
		String senha = Md5Util.getMD5Checksum(usuario.getSenha());
		EntidadeUsuario usuarioValido = (EntidadeUsuario) ObjectDao.consultarByQuery(
				String.format("from EntidadeUsuario where user_email like '%s' and user_password like '%s'",
						usuario.getEmail(), senha));
		if (usuarioValido == null) {
			PainelServidor.setLog(String.format("Usuario %s inexistente, mas tentou se conectar", usuario.getNome()));
			return null;
		}

		if (mapaUsuarios.get(usuarioValido) != null) {
			PainelServidor.setLog(
					String.format("Usuario %s tentou se conectar com uma sessao ja ativa", usuarioValido.getNome()));
			return null;
		}

		usuarioValido.setStatus(Status.ONLINE);
		mapaUsuarios.put(usuarioValido, interfaceUsuario);

		atualizarStatusUsuarios();

		PainelServidor.setLog(String.format("Usuario %s se conectou", usuario.getNome()));

		return usuarioValido;
	}

	@Override
	public void desconectarChat(EntidadeUsuario usuario) throws RemoteException {
		if (mapaUsuarios.get(usuario) == null) {
			PainelServidor
					.setLog(String.format("Usuario %s tentou se desconectar sem uma sessao ativa", usuario.getNome()));
			return;
		}

		mapaUsuarios.remove(usuario);

		atualizarStatusUsuarios();
	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, EntidadeUsuario destinatario, String mensagem)
			throws RemoteException {
		if (mapaUsuarios.get(destinatario) == null) {
			PainelServidor.setLog(String.format("Usuario %s tentou enviar uma mensagem ao usuario inativo %s",
					remetente.getNome(), destinatario.getNome()));
			return;
		}

		mapaUsuarios.get(destinatario).receberMensagem(remetente, mensagem);
		PainelServidor.setLog(String.format("Usuario %s enviou uma mensagem ao usuario %s", remetente.getNome(),
				destinatario.getNome()));
	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, String mensagem) throws RemoteException {
		for (Entry<EntidadeUsuario, InterfaceUsuario> usuarios : mapaUsuarios.entrySet()) {
			if (usuarios.getKey().equals(remetente)) {
				continue;
			} else {
				try {
					usuarios.getValue().receberMensagem(remetente, mensagem);
				} catch (Exception ex) {
					PainelServidor.setLog(String.format("Erro ao enviar a mensagem de %s para todos os contatos",
							remetente.getNome()));
				}
			}
		}
		PainelServidor
				.setLog(String.format("Usuario %s enviou uma mensagem para todos os contatos", remetente.getNome()));
	}

	@Override
	public void atualizarStatus(EntidadeUsuario usuario) throws RemoteException {
		if (mapaUsuarios.get(usuario) == null) {
			PainelServidor
					.setLog(String.format("Usuario %s tentou alterar o status sem estar conectado", usuario.getNome()));
			return;
		}

		String statusAntigo = "";
		for (Entry<EntidadeUsuario, InterfaceUsuario> oldUsuario : mapaUsuarios.entrySet()) {
			EntidadeUsuario usuarioAux = oldUsuario.getKey();
			if (usuarioAux.getId() == usuario.getId()) {
				statusAntigo = usuario.getStatus().toString();
				break;
			}
		}

		PainelServidor.setLog(String.format("Usuario %s alterou o status de %s para %s", usuario.getNome(),
				statusAntigo, usuario.getStatus()));
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

			PainelServidor.getButtonIniciarServico().setEnabled(true);
			PainelServidor.getButtonPararServico().setEnabled(false);

			PainelServidor.setLog("Servidor Finalizado!");
		} catch (NoSuchObjectException e) {
			PainelServidor.setLog("Erro ao desligar o servidor!\n" + e.toString());
		}
	}

	public void enviarArquivo(EntidadeUsuario remetente, EntidadeUsuario destinatario, Arquivo arquivo)
			throws RemoteException {
		if (mapaUsuarios.get(destinatario) == null) {
			PainelServidor.setLog(String.format("Usuario %s tentou enviar um arquivo ao usuario inativo %s",
					remetente.getNome(), destinatario.getNome()));
			return;
		}

		mapaUsuarios.get(destinatario).receberArquivo(remetente, arquivo);
		PainelServidor.setLog(String.format("Usuario %s enviou um arquivo ao usuario %s", remetente.getNome(),
				destinatario.getNome()));

	}

	@Override
	public InterfaceUsuario buscarDestinatario(EntidadeUsuario remetente, EntidadeUsuario destinatario)
			throws RemoteException {
		if (mapaUsuarios.get(destinatario) == null) {
			PainelServidor.setLog(String.format("Usuario %s tentou enviar um arquivo ao usuario inativo %s",
					remetente.getNome(), destinatario.getNome()));
			return null;
		}

		PainelServidor.setLog(String.format("Usuario %s solicitou o envio de um arquivo ao usuario %s", remetente.getNome(),
				destinatario.getNome()));
		return mapaUsuarios.get(destinatario);
	}
}
