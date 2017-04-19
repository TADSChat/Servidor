package br.univel.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import br.univel.model.Servidor;
import common.EntidadeServidor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Painel principal do servidor
 * 
 * @author Dread
 *
 */
public class PainelServidorView extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel jpServidor = new JPanel();

	// Tela de Log
	private static SimpleDateFormat dataHora = new SimpleDateFormat("dd/MM/yyyy H:mm:ss:SSS");
	private static JTextArea taLog = new JTextArea();

	// Tela do Servidor
	private static JButton buttonIniciarServico = new JButton("Iniciar");
	private static JButton buttonPararServico = new JButton("Parar");

	public PainelServidorView() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setForeground(Color.WHITE);

		jpServidor.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setSize(PainelPrincipal.LARGURA, PainelPrincipal.ALTURA);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 390, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblIp = new JLabel("IP:");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.anchor = GridBagConstraints.EAST;
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.gridx = 0;
		gbc_lblIp.gridy = 0;
		add(lblIp, gbc_lblIp);

		JLabel lblIpServidor = new JLabel(PainelPrincipal.getIpServidor());
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		add(lblIpServidor, gbc_lblNewLabel);

		JLabel lblPorta = new JLabel("PORTA:");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.anchor = GridBagConstraints.EAST;
		gbc_lblPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorta.gridx = 0;
		gbc_lblPorta.gridy = 1;
		add(lblPorta, gbc_lblPorta);

		JLabel lblPortaServidor = new JLabel(PainelPrincipal.getPortaServidor().toString());
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		add(lblPortaServidor, gbc_lblNewLabel_1);

		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDesconectar.insets = new Insets(0, 0, 5, 0);
		gbc_btnDesconectar.gridx = 2;
		gbc_btnDesconectar.gridy = 1;
		buttonPararServico.addActionListener(pararServidor());
		add(buttonPararServico, gbc_btnDesconectar);

		GridBagConstraints gbc_btnIniciar = new GridBagConstraints();
		gbc_btnIniciar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnIniciar.insets = new Insets(0, 0, 5, 0);
		gbc_btnIniciar.gridx = 2;
		gbc_btnIniciar.gridy = 0;
		buttonIniciarServico.setEnabled(false);
		buttonIniciarServico.addActionListener(iniciarServidor());
		add(buttonIniciarServico, gbc_btnIniciar);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridwidth = 3;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		add(panel, gbc_panel);

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		taLog.setEditable(false);

		scrollPane.setViewportView(taLog);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 392, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		jpServidor.setLayout(gbl_contentPane);

		Servidor.getServidor();
	}

	private ActionListener iniciarServidor() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Servidor.iniciarServidor();
			}
		};
	}

	private ActionListener pararServidor() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Servidor.pararServidor();
			}
		};
	}

	public static void setLog(String mensagem) {
		taLog.append(dataHora.format(new Date()));
		taLog.append(" " + mensagem + "\n");
	}

	/**
	 * @return the buttonIniciarServico
	 */
	public static JButton getButtonIniciarServico() {
		return buttonIniciarServico;
	}

	/**
	 * @return the buttonPararServico
	 */
	public static JButton getButtonPararServico() {
		return buttonPararServico;
	}
}
