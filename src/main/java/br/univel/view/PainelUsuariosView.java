package br.univel.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import br.univel.control.ObjectDao;
import common.EntidadeUsuario;
import common.Status;

public class PainelUsuariosView extends JPanel {

	private static final long serialVersionUID = 1L;

	private DefaultTableModel modelo = createModel();
	private List<EntidadeUsuario> listaUsuarios = new ArrayList<>();
	private JPanel panel_1;
	private JTable tabelaUsuarios;
	private JButton btnNovo;
	private JButton btnAlterar;
	private JButton btnExcluir;
	private JButton btnDesconectar;

	public PainelUsuariosView() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 113, 112, 112, 113, 0 };
		gridBagLayout.rowHeights = new int[] { 274, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 4;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_1.add(scrollPane, gbc_scrollPane);

		tabelaUsuarios = new JTable();
		DefaultTableModel modelo = createModel();
		tabelaUsuarios.setModel(modelo);
		tabelaUsuarios.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Nome", "Email", "Status" }));
		tabelaUsuarios.getColumnModel().getColumn(1).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(100);
		tabelaUsuarios.getColumnModel().getColumn(2).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(70);
		scrollPane.setViewportView(tabelaUsuarios);

		btnNovo = new JButton("Novo");
		GridBagConstraints gbc_btnNovo = new GridBagConstraints();
		gbc_btnNovo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNovo.anchor = GridBagConstraints.NORTH;
		gbc_btnNovo.insets = new Insets(0, 0, 0, 5);
		gbc_btnNovo.gridx = 0;
		gbc_btnNovo.gridy = 1;
		add(btnNovo, gbc_btnNovo);

		btnAlterar = new JButton("Alterar");
		GridBagConstraints gbc_btnAlterar = new GridBagConstraints();
		gbc_btnAlterar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAlterar.anchor = GridBagConstraints.NORTH;
		gbc_btnAlterar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAlterar.gridx = 1;
		gbc_btnAlterar.gridy = 1;
		add(btnAlterar, gbc_btnAlterar);

		btnExcluir = new JButton("Excluir");
		GridBagConstraints gbc_btnExcluir = new GridBagConstraints();
		gbc_btnExcluir.insets = new Insets(0, 0, 0, 5);
		gbc_btnExcluir.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExcluir.anchor = GridBagConstraints.NORTH;
		gbc_btnExcluir.gridx = 2;
		gbc_btnExcluir.gridy = 1;
		add(btnExcluir, gbc_btnExcluir);

		btnDesconectar = new JButton("Desconectar");
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDesconectar.anchor = GridBagConstraints.NORTH;
		gbc_btnDesconectar.gridx = 3;
		gbc_btnDesconectar.gridy = 1;
		add(btnDesconectar, gbc_btnDesconectar);

		atualizarTabela();
	}

	/**
	 * Definir modelo da tabela
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DefaultTableModel createModel() {
		return (new DefaultTableModel(new Object[][] {}, new String[] { "Nome", "Email", "Status" }) {

			private static final long serialVersionUID = 1L;

			Class[] columnTypes = new Class[] { String.class, String.class, String.class };

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

		listaUsuarios.forEach(usuario -> {
			usuario.setStatus(Status.OFFLINE);
			modelo.addRow(new String[] { usuario.getEmail(), usuario.getStatus().toString() });
		});

		tabelaUsuarios.getColumnModel().getColumn(0).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(175);
		tabelaUsuarios.getColumnModel().getColumn(1).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(175);
		tabelaUsuarios.getColumnModel().getColumn(2).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(50);
		tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabelaUsuarios.setDefaultEditor(Object.class, null);
	}
}