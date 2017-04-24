package br.univel.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import br.univel.model.Servidor;

public class PainelPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private static String ipServidor;
	private static Integer portaServidor;
	private static JTabbedPane painelAbas;

	public static final int LARGURA = 600;
	public static final int ALTURA = 450;
	private Dimension dimensaoTela = Toolkit.getDefaultToolkit().getScreenSize();

	public PainelPrincipal(String ipServidor, Integer portaServidor) {
		setIpServidor(ipServidor);
		setPortaServidor(portaServidor);

		this.setSize(LARGURA, ALTURA);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(LARGURA, ALTURA);
		this.setResizable(false);

		setLocation((dimensaoTela.width - this.getSize().width) / 2, (dimensaoTela.height - this.getSize().height) / 2);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente finalizar o servidor?", "Atenção", dialogButton);
				if (resposta == JOptionPane.YES_OPTION) {
					Servidor.getServidor().desconectarTodos();
				} else {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			}
		});

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 212, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		painelAbas = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		getContentPane().add(painelAbas, gbc_tabbedPane);

		PainelServidor servidor = new PainelServidor();
		PainelUsuarios usuarios = new PainelUsuarios();

		painelAbas.addTab("SERVIDOR", servidor);
		painelAbas.addTab("USUARIOS", usuarios);
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
	 * @param ipServidor
	 *            the ipServidor to set
	 */
	public static void setIpServidor(String ipServidor) {
		PainelPrincipal.ipServidor = ipServidor;
	}

	/**
	 * @param portaServidor
	 *            the portaServidor to set
	 */
	public static void setPortaServidor(Integer portaServidor) {
		PainelPrincipal.portaServidor = portaServidor;
	}

	/**
	 * @return the painelAbas
	 */
	public static JTabbedPane getPainelAbas() {
		return painelAbas;
	}

}
