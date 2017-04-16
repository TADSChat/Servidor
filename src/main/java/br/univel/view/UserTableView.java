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

public class UserTableView extends JPanel {

	private DefaultTableModel modelo = createModel();
	private List<EntidadeUsuario> listaUsuarios = new ArrayList<>();

	private JTable tabelaUsuarios;
	private JTextField tfNome;
	private JTextField tfEmail;
	private JTextField tfSenha;
	private JTextField tfConfSenha;
	private JButton btnNovo;
	private JButton btnAlterar;

	public UserTableView() {
		GridBagLayout gbl_jpUsuarios = new GridBagLayout();
		gbl_jpUsuarios.columnWidths = new int[] { 271, 0, 0 };
		gbl_jpUsuarios.rowHeights = new int[] { 0, 0 };
		gbl_jpUsuarios.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_jpUsuarios.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		this.setLayout(gbl_jpUsuarios);

		JScrollPane spUsuarios = new JScrollPane();
		GridBagConstraints gbc_spUsuarios = new GridBagConstraints();
		gbc_spUsuarios.insets = new Insets(0, 0, 0, 5);
		gbc_spUsuarios.fill = GridBagConstraints.BOTH;
		gbc_spUsuarios.gridx = 0;
		gbc_spUsuarios.gridy = 0;
		this.add(spUsuarios, gbc_spUsuarios);

		tabelaUsuarios = new JTable();
		tabelaUsuarios.setModel(modelo);

		spUsuarios.setColumnHeaderView(tabelaUsuarios);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		this.add(panel, gbc_panel);
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

		btnNovo.removeAll();
		btnAlterar.removeAll();

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
				limparTela();
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

		limparTela();
	}

	private void limparTela() {
		tfNome.setText("");
		tfEmail.setText("");
		tfSenha.setText("");
		tfConfSenha.setText("");
		btnNovo.setText("Novo");
		btnAlterar.setText("Alterar");
		tfNome.setEnabled(false);
		tfEmail.setEnabled(false);
		tfSenha.setEnabled(false);
		tfConfSenha.setEnabled(false);
		btnAlterar.setEnabled(false);

		btnNovo.removeAll();
		btnAlterar.removeAll();
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
}
