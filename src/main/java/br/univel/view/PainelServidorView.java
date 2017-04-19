package br.univel.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import common.EntidadeServidor;
import common.EntidadeUsuario;
import common.InterfaceServidor;
import common.InterfaceUsuario;
import common.Status;

/**
 * Painel principal do servidor
 * 
 * @author Dread
 *
 */
public class PainelServidorView extends JPanel implements InterfaceServidor {

	private static final long serialVersionUID = 1L;



	private JTabbedPane jtpTabs = new JTabbedPane();

	private JPanel jpServidor = new JPanel();
	private JPanel jpUsuarios = new PainelUsuariosView();
	private JPanel jpLog = new JPanel();

	
	private Map<EntidadeUsuario, InterfaceUsuario> mapaUsuarios = new HashMap<>();
	private InterfaceServidor interfaceServidor;
	private Registry registry;

	// Tela de Log
	private static SimpleDateFormat dataHora = new SimpleDateFormat("dd/MM/yyyy H:mm:ss:SSS");
	private static JTextArea taLog = new JTextArea();

	// Tela do Servidor
	private JButton buttonIniciarServico = new JButton();
	private JButton buttonPararServico = new JButton();

	private EntidadeServidor servidor = new EntidadeServidor();

	public PainelServidorView() {
		jpServidor.setBorder(new EmptyBorder(5, 5, 5, 5));

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 392, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		jpServidor.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("IP");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		jpServidor.add(lblNewLabel, gbc_lblNewLabel);

		JTextField tfIpServidor = new JTextField();
		tfIpServidor.setText(servidor.getIpServer());
		tfIpServidor.setEnabled(false);
		tfIpServidor.setMinimumSize(new Dimension(200, 24));
		tfIpServidor.setPreferredSize(new Dimension(200, 24));
		GridBagConstraints gbc_tfIpServidor = new GridBagConstraints();
		gbc_tfIpServidor.gridwidth = 2;
		gbc_tfIpServidor.insets = new Insets(0, 0, 5, 5);
		gbc_tfIpServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfIpServidor.gridx = 1;
		gbc_tfIpServidor.gridy = 0;
		jpServidor.add(tfIpServidor, gbc_tfIpServidor);

		buttonIniciarServico = new JButton("Iniciar Serviço");
		buttonIniciarServico.setEnabled(false);
		buttonIniciarServico.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarServico();
			}
		});
		GridBagConstraints gbc_buttonIniciarServico = new GridBagConstraints();
		gbc_buttonIniciarServico.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonIniciarServico.insets = new Insets(0, 0, 5, 0);
		gbc_buttonIniciarServico.gridx = 3;
		gbc_buttonIniciarServico.gridy = 0;
		jpServidor.add(buttonIniciarServico, gbc_buttonIniciarServico);

		JLabel lblNewLabel_1 = new JLabel("Porta");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		jpServidor.add(lblNewLabel_1, gbc_lblNewLabel_1);

		JTextField tfPorta = new JTextField();
		//tfPorta.setText(servidor.getPortaServer().toString());
		tfPorta.setEnabled(false);
		tfPorta.setPreferredSize(new Dimension(200, 24));
		tfPorta.setMinimumSize(new Dimension(200, 24));
		GridBagConstraints gbc_tfPorta = new GridBagConstraints();
		gbc_tfPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfPorta.gridwidth = 2;
		gbc_tfPorta.insets = new Insets(0, 0, 5, 5);
		gbc_tfPorta.gridx = 1;
		gbc_tfPorta.gridy = 1;
		jpServidor.add(tfPorta, gbc_tfPorta);

		buttonPararServico = new JButton("Parar Serviço");
		buttonPararServico.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pararServico();
			}
		});

		GridBagConstraints gbc_buttonPararServico = new GridBagConstraints();
		gbc_buttonPararServico.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonPararServico.insets = new Insets(0, 0, 5, 0);
		gbc_buttonPararServico.gridx = 3;
		gbc_buttonPararServico.gridy = 1;
		jpServidor.add(buttonPararServico, gbc_buttonPararServico);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 4;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		jpServidor.add(panel, gbc_panel);

	}

	private void adicionarComponentesLog() {
		GridBagLayout gbl_jpLog = new GridBagLayout();
		gbl_jpLog.columnWidths = new int[] { 0, 0 };
		gbl_jpLog.rowHeights = new int[] { 0, 0 };
		gbl_jpLog.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_jpLog.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		jpLog.setLayout(gbl_jpLog);

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		jpLog.add(scrollPane_1, gbc_scrollPane_1);

		scrollPane_1.setViewportView(taLog);
	}

	private void iniciarServico() {
		try {
			interfaceServidor = (InterfaceServidor) UnicastRemoteObject.exportObject(this, 0);
			registry = LocateRegistry.createRegistry(servidor.getPortaServer());
			registry.rebind(InterfaceServidor.NOME, interfaceServidor);

			setLog("Servidor iniciado");

			buttonIniciarServico.setEnabled(false);
			buttonPararServico.setEnabled(true);

		} catch (RemoteException e) {
			setLog("Erro ao iniciar servidor, verificar se porta nao esta sendo usada! \n" + e.getMessage());
		}
	}

	protected void pararServico() {
		try {
			UnicastRemoteObject.unexportObject(this, true);
			UnicastRemoteObject.unexportObject(registry, true);

			buttonIniciarServico.setEnabled(true);
			buttonPararServico.setEnabled(false);

			setLog("Serviço encerrado. Todos os clientes foram desconectados!");

		} catch (RemoteException e) {
			setLog("Erro ao interromper servidor! \n" + e.getMessage());
		}
	}

	protected static void setLog(String mensagem) {
		taLog.append(dataHora.format(new Date()));
		taLog.append(" " + mensagem + "\n");
	}

	private void atualizarStatusUsuarios() {
		try {
			for (InterfaceUsuario usuario : mapaUsuarios.values()) {
				usuario.receberListaParticipantes(new ArrayList<EntidadeUsuario>(mapaUsuarios.keySet()));
			}
		} catch (RemoteException e) {
			setLog("Erro ao atualizar lista de usuarios");
			return;
		}
		setLog("Atualizando status dos usuarios");
	}

	@Override
	public void conectarChat(EntidadeUsuario usuario, InterfaceUsuario interfaceUsuario) throws RemoteException {
		if (mapaUsuarios.get(usuario) != null) {
			setLog(String.format("Usuario %s tentou se conectar com uma sessao ja ativa", usuario.getNome()));
			return;
		}

		usuario.setStatus(Status.ONLINE);
		mapaUsuarios.put(usuario, interfaceUsuario);

		atualizarStatusUsuarios();

		setLog(String.format("Usuario %s se conectou", usuario.getNome()));
	}

	@Override
	public void desconectarChat(EntidadeUsuario usuario) throws RemoteException {
		if (mapaUsuarios.get(usuario) == null) {
			setLog(String.format("Usuario %s tentou se desconectar sem uma sessao ativa", usuario.getNome()));
			return;
		}

		mapaUsuarios.remove(usuario);

		atualizarStatusUsuarios();
	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, EntidadeUsuario destinatario, String mensagem)
			throws RemoteException {
		if (mapaUsuarios.get(destinatario) == null) {
			setLog(String.format("Usuario %s tentou enviar uma mensagem ao usuario inativo %s", remetente.getNome(),
					destinatario.getNome()));
			return;
		}

		mapaUsuarios.get(destinatario).receberMensagem(remetente, mensagem);
		setLog(String.format("Usuario %s enviou uma mensagem ao usuario %s", remetente.getNome(),
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
					setLog(String.format("Erro ao enviar a mensagem de %s para todos os contatos",
							remetente.getNome()));
				}
			}
		}
		setLog(String.format("Usuario %s enviou uma mensagem para todos os contatos", remetente.getNome()));
	}

	@Override
	public void enviarArquivo(EntidadeUsuario remetente, EntidadeUsuario destinatario, File arquivo)
			throws RemoteException {

		if (mapaUsuarios.get(destinatario) == null) {
			setLog(String.format("Usuario %s tentou enviar um arquivo ao usuario inativo %s", remetente.getNome(),
					destinatario.getNome()));
			return;
		}

		mapaUsuarios.get(destinatario).receberArquivo(remetente, arquivo);
		setLog(String.format("Usuario %s enviou um arquivo ao usuario %s", remetente.getNome(),
				destinatario.getNome()));
	}

	@Override
	public void atualizarStatus(EntidadeUsuario usuario) throws RemoteException {
		if (mapaUsuarios.get(usuario) == null) {
			setLog(String.format("Usuario %s tentou alterar o status sem estar conectado", usuario.getNome()));
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

		setLog(String.format("Usuario %s alterou o status de %s para %s", usuario.getNome(), statusAntigo,
				usuario.getStatus()));
	}
}
