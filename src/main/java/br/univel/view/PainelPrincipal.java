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

	private String ipServidor;
	private Integer portaServidor;

	private JTable table;
	private JTabbedPane tabbedPane;

	public static final int LARGURA = 600;
	public static final int ALTURA = 600;
	private Dimension dimensaoTela = Toolkit.getDefaultToolkit().getScreenSize();
	
	public PainelPrincipal(String ipServidor, Integer portaServidor) {
		setSize(LARGURA, ALTURA);
		setLocation((dimensaoTela.width - this.getSize().width) / 2, (dimensaoTela.height - this.getSize().height) / 2);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(LARGURA, ALTURA);
		this.setResizable(false);
		
		this.ipServidor = ipServidor;
		this.portaServidor = portaServidor;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 212, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		add(tabbedPane, gbc_tabbedPane);

		PainelServidorView servidor = new PainelServidorView();
		PainelUsuariosView usuarios = new PainelUsuariosView();

		
		tabbedPane.addTab("SERVIDOR", servidor);
		tabbedPane.addTab("USUARIOS", usuarios);

		JPanel panelLog = new JPanel();
		GridBagConstraints gbc_panelLog = new GridBagConstraints();
		gbc_panelLog.fill = GridBagConstraints.BOTH;
		gbc_panelLog.gridx = 0;
		gbc_panelLog.gridy = 1;
		add(panelLog, gbc_panelLog);
		GridBagLayout gbl_panelLog = new GridBagLayout();
		gbl_panelLog.columnWidths = new int[] { 0, 0 };
		gbl_panelLog.rowHeights = new int[] { 0, 0 };
		gbl_panelLog.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelLog.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelLog.setLayout(gbl_panelLog);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panelLog.add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
	}
}
