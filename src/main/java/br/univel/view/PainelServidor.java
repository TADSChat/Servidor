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
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import br.univel.control.ObjectDao;
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
public class PainelServidor extends JFrame implements InterfaceServidor {

	private static final long serialVersionUID = 1L;

	public static final int LARGURA = 600;
	public static final int ALTURA = 600;

	private JTabbedPane jtpTabs = new JTabbedPane();

	private JPanel jpServidor = new JPanel();
	private JPanel jpUsuarios = new JPanel();
	private JPanel jpLog = new JPanel();

	private Dimension dimensaoTela = Toolkit.getDefaultToolkit().getScreenSize();
	private SimpleDateFormat dataHora = new SimpleDateFormat("dd/MM/yyyy H:mm:ss:SSS");
	private DefaultTableModel modelo = createModel();
	private List<EntidadeUsuario> listaUsuarios = new ArrayList<>();
	private Map<String, InterfaceUsuario> mapaUsuarios = new HashMap<>();
	private InterfaceServidor interfaceServidor;
	private Registry registry;

	// Tela de Log
	private JTextArea taLog = new JTextArea();

	// Tela do Servidor
	private JButton buttonIniciarServico = new JButton();
	private JButton buttonPararServico = new JButton();

	// Tela de Usuarios
	private JTable tabelaUsuarios;
	private JTextField tfNome;
	private JTextField tfEmail;
	private JTextField tfSenha;
	private JTextField tfConfSenha;
	private JButton btnNovo;
	private JButton btnAlterar;

	private EntidadeServidor servidor = new EntidadeServidor();

	public PainelServidor(String ipServer, Integer portaServer) {
		setSize(LARGURA, ALTURA);
		setLocation((dimensaoTela.width - this.getSize().width) / 2, (dimensaoTela.height - this.getSize().height) / 2);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(LARGURA, ALTURA);
		this.setResizable(false);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setLog("Aplicação servidor encerrada!");
			}
		});

		servidor.setIpServer(ipServer).setPortaServer(portaServer);
		iniciarServico();

		adicionarComponentesServidor();
		adicionarComponentesUsuario();
		adicionarComponentesLog();
		atualizarTabela();

		this.getContentPane().add(jtpTabs, BorderLayout.CENTER);

		jtpTabs.addTab("SERVIDOR", jpServidor);
		jtpTabs.addTab("USUARIOS", jpUsuarios);
		jtpTabs.addTab("LOG", jpLog);
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

	private void adicionarComponentesUsuario() {
		GridBagLayout gbl_jpUsuarios = new GridBagLayout();
		gbl_jpUsuarios.columnWidths = new int[] { 271, 0, 0 };
		gbl_jpUsuarios.rowHeights = new int[] { 0, 0 };
		gbl_jpUsuarios.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_jpUsuarios.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		jpUsuarios.setLayout(gbl_jpUsuarios);

		JScrollPane spUsuarios = new JScrollPane();
		GridBagConstraints gbc_spUsuarios = new GridBagConstraints();
		gbc_spUsuarios.insets = new Insets(0, 0, 0, 5);
		gbc_spUsuarios.fill = GridBagConstraints.BOTH;
		gbc_spUsuarios.gridx = 0;
		gbc_spUsuarios.gridy = 0;
		jpUsuarios.add(spUsuarios, gbc_spUsuarios);

		tabelaUsuarios = new JTable();
		tabelaUsuarios.setModel(modelo);

		spUsuarios.setColumnHeaderView(tabelaUsuarios);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		jpUsuarios.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 70, 74, 59, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblNewLabel_2 = new JLabel("Nome");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);

		tfNome = new JTextField();
		tfNome.setEnabled(false);
		GridBagConstraints gbc_tfNome = new GridBagConstraints();
		gbc_tfNome.gridwidth = 3;
		gbc_tfNome.insets = new Insets(0, 0, 5, 0);
		gbc_tfNome.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfNome.gridx = 1;
		gbc_tfNome.gridy = 0;
		panel.add(tfNome, gbc_tfNome);
		tfNome.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Email");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		panel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		tfEmail = new JTextField();
		tfEmail.setEnabled(false);
		GridBagConstraints gbc_tfEmail = new GridBagConstraints();
		gbc_tfEmail.gridwidth = 3;
		gbc_tfEmail.insets = new Insets(0, 0, 5, 0);
		gbc_tfEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfEmail.gridx = 1;
		gbc_tfEmail.gridy = 1;
		panel.add(tfEmail, gbc_tfEmail);
		tfEmail.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Senha");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 2;
		panel.add(lblNewLabel_4, gbc_lblNewLabel_4);

		tfSenha = new JTextField();
		tfSenha.setEnabled(false);
		GridBagConstraints gbc_tfSenha = new GridBagConstraints();
		gbc_tfSenha.gridwidth = 3;
		gbc_tfSenha.insets = new Insets(0, 0, 5, 0);
		gbc_tfSenha.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfSenha.gridx = 1;
		gbc_tfSenha.gridy = 2;
		panel.add(tfSenha, gbc_tfSenha);
		tfSenha.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("Conf. Senha");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 3;
		panel.add(lblNewLabel_5, gbc_lblNewLabel_5);

		tfConfSenha = new JTextField();
		tfConfSenha.setEnabled(false);
		GridBagConstraints gbc_tfConfSenha = new GridBagConstraints();
		gbc_tfConfSenha.gridwidth = 3;
		gbc_tfConfSenha.insets = new Insets(0, 0, 5, 0);
		gbc_tfConfSenha.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfConfSenha.gridx = 1;
		gbc_tfConfSenha.gridy = 3;
		panel.add(tfConfSenha, gbc_tfConfSenha);
		tfConfSenha.setColumns(10);

		btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				incluirUsuario();
			}
		});
		GridBagConstraints gbc_btnNovo = new GridBagConstraints();
		gbc_btnNovo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNovo.insets = new Insets(0, 0, 0, 5);
		gbc_btnNovo.gridx = 1;
		gbc_btnNovo.gridy = 4;
		panel.add(btnNovo, gbc_btnNovo);

		btnAlterar = new JButton("Alterar");
		btnAlterar.setEnabled(false);
		btnAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterarUsuario();
			}
		});
		GridBagConstraints gbc_btnAlterar = new GridBagConstraints();
		gbc_btnAlterar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAlterar.anchor = GridBagConstraints.NORTH;
		gbc_btnAlterar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAlterar.gridx = 2;
		gbc_btnAlterar.gridy = 4;
		panel.add(btnAlterar, gbc_btnAlterar);

		JButton btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setEnabled(false);
		btnDesconectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				desconectarUsuario();
			}
		});
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDesconectar.gridx = 3;
		gbc_btnDesconectar.gridy = 4;
		panel.add(btnDesconectar, gbc_btnDesconectar);
	}

	private void adicionarComponentesServidor() {
		jpServidor.setBorder(new EmptyBorder(5, 5, 5, 5));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		tfPorta.setText(servidor.getPortaServer().toString());
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

	protected void setLog(String mensagem) {
		taLog.append(dataHora.format(new Date()));
		taLog.append(" " + mensagem + "\n");
	}

	/**
	 * Definir modelo da tabela
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DefaultTableModel createModel() {
		return (new DefaultTableModel(new Object[][] {}, new String[] { "Email", "Status" }) {

			private static final long serialVersionUID = 1L;

			Class[] columnTypes = new Class[] { String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
	}

	/**
	 * Atualizar tabela
	 */
	private void atualizarTabela() {

		listaUsuarios = (List<EntidadeUsuario>) ObjectDao.listar("from EntidadeUsuario");

		listaUsuarios.forEach(usuario -> modelo.addRow(new String[] { usuario.getEmail(), Status.OFFLINE.toString() }));

		tabelaUsuarios.getColumnModel().getColumn(0).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(100);
		tabelaUsuarios.getColumnModel().getColumn(1).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(70);
		tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabelaUsuarios.setDefaultEditor(Object.class, null);
	}

	protected void desconectarUsuario() {
		// TODO Auto-generated method stub

	}

	protected void alterarUsuario() {
		// TODO Auto-generated method stub

	}

	protected void incluirUsuario() {
		tfNome.setEnabled(true);
		tfEmail.setEnabled(true);
		tfSenha.setEnabled(true);
		tfConfSenha.setEnabled(true);
		btnAlterar.setEnabled(true);

		btnNovo.setText("Salvar");
		btnNovo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvarUsuario();
			}
		});
		btnAlterar.setText("Cancelar");
		btnAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnNovo.setText("Novo");
				btnAlterar.setText("Alterar");
				tfNome.setEnabled(false);
				tfEmail.setEnabled(false);
				tfSenha.setEnabled(false);
				tfConfSenha.setEnabled(false);
				btnAlterar.setEnabled(false);

				btnNovo.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						incluirUsuario();
					}
				});
				btnAlterar.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						alterarUsuario();
					}
				});
				
			}
		});
	}

	protected void salvarUsuario() {
		if (tfNome.getText().equals("") || tfEmail.getText().equals("") || tfSenha.getText().equals("")
				|| tfConfSenha.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos!");
			return;
		}

		if (!tfSenha.getText().equals(tfConfSenha.getText())) {
			JOptionPane.showMessageDialog(null, "Senhas diferentes!");
			return;
		}

		EntidadeUsuario usuario = new EntidadeUsuario();
		usuario.setEmail(tfEmail.getText()).setNome(tfNome.getText()).setSenha(tfSenha.getText());

		ObjectDao.incluir(usuario);
		atualizarTabela();
	}

	@Override
	public void conectarChat(EntidadeUsuario usuario, InterfaceUsuario interfaceUsuario) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void desconectarChat(EntidadeUsuario usuario) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, EntidadeUsuario destinatario, String mensagem)
			throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void enviarMensagem(EntidadeUsuario remetente, String mensagem) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void enviarArquivo(EntidadeUsuario remetente, EntidadeUsuario destinatario, File arquivo)
			throws RemoteException {
		// TODO Auto-generated method stub

	}
}
