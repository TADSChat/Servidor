package br.univel.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import br.univel.control.ObjectDao;
import br.univel.model.Servidor;
import common.EntidadeUsuario;
import common.InterfaceUsuario;
import common.Status;

public class PainelUsuarios extends JPanel {

	private static final long serialVersionUID = 1L;

	// private static DefaultTableModel modelo = createModel();
	private static List<EntidadeUsuario> listaUsuarios = new ArrayList<>();
	private JPanel panel_1;
	private static ModeloTabela modelo;
	private static JTable tabelaUsuarios;
	private JButton btnNovo;
	private JButton btnAlterar;
	private JButton btnExcluir;
	private JButton btnDesconectar;

	public PainelUsuarios() {
		this.setSize(PainelPrincipal.LARGURA, PainelPrincipal.ALTURA);

		adicionarComponentes();

		atualizarTabela();
	}

	private void adicionarComponentes() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 150, 150, 150, 251, 0 };
		gridBagLayout.rowHeights = new int[] { 373, 25, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
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
		gbl_panel_1.columnWidths = new int[] { 588, 0 };
		gbl_panel_1.rowHeights = new int[] { 369, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_1.add(scrollPane, gbc_scrollPane);

		tabelaUsuarios = new JTable();
		atualizarTabela();
		scrollPane.setViewportView(tabelaUsuarios);

		btnNovo = new JButton("Novo");
		btnNovo.addActionListener(novoUsuario());
		GridBagConstraints gbc_btnNovo = new GridBagConstraints();
		gbc_btnNovo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNovo.anchor = GridBagConstraints.NORTH;
		gbc_btnNovo.insets = new Insets(0, 0, 0, 5);
		gbc_btnNovo.gridx = 0;
		gbc_btnNovo.gridy = 1;
		add(btnNovo, gbc_btnNovo);

		btnAlterar = new JButton("Alterar");
		btnAlterar.addActionListener(alterarUsuario());
		GridBagConstraints gbc_btnAlterar = new GridBagConstraints();
		gbc_btnAlterar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAlterar.anchor = GridBagConstraints.NORTH;
		gbc_btnAlterar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAlterar.gridx = 1;
		gbc_btnAlterar.gridy = 1;
		add(btnAlterar, gbc_btnAlterar);

		btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(excluirUsuario());
		GridBagConstraints gbc_btnExcluir = new GridBagConstraints();
		gbc_btnExcluir.insets = new Insets(0, 0, 0, 5);
		gbc_btnExcluir.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExcluir.anchor = GridBagConstraints.NORTH;
		gbc_btnExcluir.gridx = 2;
		gbc_btnExcluir.gridy = 1;
		add(btnExcluir, gbc_btnExcluir);

		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(desconectarUsuario());
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDesconectar.anchor = GridBagConstraints.NORTH;
		gbc_btnDesconectar.gridx = 3;
		gbc_btnDesconectar.gridy = 1;
		add(btnDesconectar, gbc_btnDesconectar);

		DadosUsuario.getDadosUsuario(new EntidadeUsuario());

	}

	private ActionListener desconectarUsuario() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EntidadeUsuario usuario = getUsuarioTabela();

				if (usuario == null) {
					return;
				}

				if (usuario.getStatus().equals(Status.OFFLINE)) {
					return;
				}

				Servidor.getServidor().desconectarUsuario(usuario);

			}
		};
	}

	private ActionListener excluirUsuario() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EntidadeUsuario usuario = getUsuarioTabela();

				if (usuario == null) {
					return;
				}

				if (!usuario.getStatus().equals(Status.OFFLINE)) {
					JOptionPane.showMessageDialog(null, "Desconecte o usuario altes de exclui-lo!");
					return;
				}

				int dialogButton = JOptionPane.YES_NO_OPTION;
				int resposta = JOptionPane.showConfirmDialog(null,
						String.format("Confirma a exclusao do usuário %s?", usuario.getNome()), "Atenção",
						dialogButton);

				if (resposta == JOptionPane.YES_OPTION) {
					ObjectDao.getObjectDao().excluir(usuario);
					atualizarTabela();
				}
			}
		};

	}

	private ActionListener alterarUsuario() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EntidadeUsuario usuario = getUsuarioTabela();

				if (usuario == null) {
					return;
				}

				if (!usuario.getStatus().equals(Status.OFFLINE)) {
					JOptionPane.showMessageDialog(null, "Desconecte o usuario altes de exclui-lo!");
					return;
				}

				DadosUsuario dados = DadosUsuario.getDadosUsuario(usuario);

				PainelPrincipal.getPainelAbas().add("USUARIOS", dados);
				PainelPrincipal.getPainelAbas().setSelectedIndex(2);
				PainelPrincipal.getPainelAbas().setEnabledAt(1, false);
			}
		};
	}

	private ActionListener novoUsuario() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DadosUsuario dados = DadosUsuario.getDadosUsuario(new EntidadeUsuario());

				PainelPrincipal.getPainelAbas().add("NOVO USUARIO", dados);
				PainelPrincipal.getPainelAbas().setSelectedIndex(2);
				PainelPrincipal.getPainelAbas().setEnabledAt(1, false);
			}
		};
	}

	private EntidadeUsuario getUsuarioTabela() {
		if (tabelaUsuarios.getSelectedRow() < 0) {
			JOptionPane.showMessageDialog(null, "Nenhum usuario foi selecionado!");
			return null;
		}

		Integer linha = tabelaUsuarios.getSelectedRow();

		return new EntidadeUsuario().setNome(tabelaUsuarios.getModel().getValueAt(linha, 0).toString())
				.setEmail(tabelaUsuarios.getModel().getValueAt(linha, 1).toString())
				.setStatus(Status.valueOf(tabelaUsuarios.getModel().getValueAt(linha, 2).toString()))
				.setSenha(tabelaUsuarios.getModel().getValueAt(linha, 3).toString())
				.setId(Integer.parseInt(tabelaUsuarios.getModel().getValueAt(linha, 4).toString()));
	}

	/**
	 * Atualizar tabela
	 */
	public static void atualizarTabela() {

		listaUsuarios = null;
		listaUsuarios = (List<EntidadeUsuario>) ObjectDao.listar("from EntidadeUsuario");

		listaUsuarios.forEach(usuario -> {

			if (Servidor.getServidor().getMapaUsuarios() != null) {

				if (Servidor.getServidor().getMapaUsuarios().containsKey(usuario.getId())) {
					Servidor.getListaUsuarios().forEach(usuarioAux -> {
						if (usuarioAux.getId() == usuario.getId()) {
							usuario.setStatus(usuarioAux.getStatus());
						}
					});
				} else {
					usuario.setStatus(Status.OFFLINE);
				}
			} else {
				usuario.setStatus(Status.OFFLINE);
			}
		});
		modelo = new ModeloTabela(listaUsuarios);
		tabelaUsuarios.setModel(modelo);

		tabelaUsuarios.getColumnModel().getColumn(0).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(175);
		tabelaUsuarios.getColumnModel().getColumn(1).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(175);
		tabelaUsuarios.getColumnModel().getColumn(2).setResizable(false);
		tabelaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(50);
		tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabelaUsuarios.setDefaultEditor(Object.class, null);
	}

	public static List<EntidadeUsuario> getListaUsuarios() {
		return listaUsuarios;
	}
}