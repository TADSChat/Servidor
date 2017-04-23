package br.univel.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.univel.control.HibernateUtil;
import br.univel.control.ObjectDao;
import common.EntidadeServidor;

/**
 * Classe para execu��o do servidor
 * 
 * @author Dread
 *
 */
public class MainServidor extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField tfIpServidor = new JTextField();
	private JNumberField nfPortaServidor = new JNumberField();
	private InetAddress IP;

	Dimension dimensaoTela = Toolkit.getDefaultToolkit().getScreenSize();

	public MainServidor() {
		HibernateUtil.getSession();

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 200);
		this.setResizable(false);
		this.setLocation((dimensaoTela.width - this.getSize().width) / 2,
				(dimensaoTela.height - this.getSize().height) / 2);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 10, 10, 10);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 29, 0, 29, 31 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		panel.setLayout(gbl_panel);

		JLabel lblNewLabel = new JLabel("IP do Servidor");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		tfIpServidor = new JTextField();
		GridBagConstraints gbc_tfIpServidor = new GridBagConstraints();
		gbc_tfIpServidor.anchor = GridBagConstraints.NORTH;
		gbc_tfIpServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfIpServidor.insets = new Insets(0, 0, 5, 0);
		gbc_tfIpServidor.gridx = 0;
		gbc_tfIpServidor.gridy = 1;
		panel.add(tfIpServidor, gbc_tfIpServidor);
		tfIpServidor.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Porta do Servidor");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		nfPortaServidor = new JNumberField();
		GridBagConstraints gbc_tfPortaServidor = new GridBagConstraints();
		gbc_tfPortaServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfPortaServidor.insets = new Insets(0, 0, 5, 0);
		gbc_tfPortaServidor.gridx = 0;
		gbc_tfPortaServidor.gridy = 3;
		panel.add(nfPortaServidor, gbc_tfPortaServidor);
		nfPortaServidor.setColumns(10);

		JButton btnIniciarServidor = new JButton("Iniciar");
		GridBagConstraints gbc_btnIniciarServidor = new GridBagConstraints();
		gbc_btnIniciarServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnIniciarServidor.anchor = GridBagConstraints.NORTH;
		gbc_btnIniciarServidor.gridx = 0;
		gbc_btnIniciarServidor.gridy = 4;
		panel.add(btnIniciarServidor, gbc_btnIniciarServidor);
		HibernateUtil.getSession();

		try {
			IP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Erro ao ler IP do servidor! Ver arquivo de log.");
		}

		EntidadeServidor servidor = (EntidadeServidor) ObjectDao
				.consultarByQuery(String.format("from EntidadeServidor where server_ip = '%s'", IP.getHostAddress()));

		tfIpServidor.setText(IP.getHostAddress());
		tfIpServidor.setEnabled(false);

		if (servidor == null) {
			nfPortaServidor.setEnabled(true);
			btnIniciarServidor.addActionListener(actionCriar());
		} else {
			nfPortaServidor.setEnabled(false);
			nfPortaServidor.setText(servidor.getPortaServer().toString());
			btnIniciarServidor.addActionListener(actionConectar());
			iniciarServidor();
		}
		nfPortaServidor.grabFocus();
	}

	/**
	 * Adiciona a a�ao para criar registro ao bot�o
	 * 
	 * @return ActionListener para o botao salvar
	 */
	public ActionListener actionCriar() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tfIpServidor.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "IP Invalido para o servidor!");
					return;
				}
				if (nfPortaServidor.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Porta Invalida para o servidor!");
					return;
				}

				EntidadeServidor servidor = new EntidadeServidor();
				servidor.setPortaServer(Integer.parseInt(nfPortaServidor.getText())).setIpServer(IP.getHostAddress());

				ObjectDao.incluir(servidor);

				dispose();
				new PainelPrincipal(tfIpServidor.getText(), nfPortaServidor.getNumber());
			}

		};
	}

	/**
	 * Adiciona a a�ao para conectar no bot�o
	 * 
	 * @return ActionListener para o botao salvar
	 */
	public ActionListener actionConectar() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				iniciarServidor();
			}
		};
	}

	protected void iniciarServidor() {
		dispose();
		new PainelPrincipal(tfIpServidor.getText(), nfPortaServidor.getNumber());
	}

	/**
	 * Classe main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new MainServidor();
	}
}
