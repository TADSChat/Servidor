package br.univel.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class PainelPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private static String ipServidor;
	private static Integer portaServidor;
	private JTabbedPane tabbedPane;

	public static final int LARGURA = 600;
	public static final int ALTURA = 400;
	private Dimension dimensaoTela = Toolkit.getDefaultToolkit().getScreenSize();

	public PainelPrincipal(String ipServidor, Integer portaServidor) {
		setSize(LARGURA, ALTURA);
		setLocation((dimensaoTela.width - this.getSize().width) / 2, (dimensaoTela.height - this.getSize().height) / 2);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(LARGURA, ALTURA);
		this.setResizable(false);

		setIpServidor(ipServidor);
		setPortaServidor(portaServidor);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 212, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		getContentPane().add(tabbedPane, gbc_tabbedPane);

		PainelServidorView servidor = new PainelServidorView();
		PainelUsuariosView usuarios = new PainelUsuariosView();

		tabbedPane.addTab("SERVIDOR", servidor);
		tabbedPane.addTab("USUARIOS", usuarios);
	}

	/**
	 * @return the ipServidor
	 */
	public static String getIpServidor() {
		return ipServidor;
	}

	/**
	 * @return the portaServidor
	 */
	public static Integer getPortaServidor() {
		return portaServidor;
	}

	/**
	 * @param ipServidor the ipServidor to set
	 */
	public static void setIpServidor(String ipServidor) {
		PainelPrincipal.ipServidor = ipServidor;
	}

	/**
	 * @param portaServidor the portaServidor to set
	 */
	public static void setPortaServidor(Integer portaServidor) {
		PainelPrincipal.portaServidor = portaServidor;
	}

}
