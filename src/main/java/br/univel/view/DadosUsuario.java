package br.univel.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.univel.control.Md5Util;
import br.univel.control.ObjectDao;
import common.EntidadeUsuario;

public class DadosUsuario extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField tfNome;
	private JTextField tfEmail;
	private JPasswordField tfSenha;
	private JPasswordField tfConfSenha;

	private static DadosUsuario dadosUsuario;
	private EntidadeUsuario usuario;
	private Boolean incluir = true;

	private DadosUsuario(EntidadeUsuario usuario) {
		this.usuario = usuario;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 10, 10, 10);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 87, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblNewLabel = new JLabel("Nome");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		tfNome = new JTextField();
		GridBagConstraints gbc_tfNome = new GridBagConstraints();
		gbc_tfNome.anchor = GridBagConstraints.NORTH;
		gbc_tfNome.insets = new Insets(0, 0, 5, 0);
		gbc_tfNome.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfNome.gridx = 1;
		gbc_tfNome.gridy = 0;
		panel.add(tfNome, gbc_tfNome);
		tfNome.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Email");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		tfEmail = new JTextField();
		GridBagConstraints gbc_tfEmail = new GridBagConstraints();
		gbc_tfEmail.anchor = GridBagConstraints.NORTH;
		gbc_tfEmail.insets = new Insets(0, 0, 5, 0);
		gbc_tfEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfEmail.gridx = 1;
		gbc_tfEmail.gridy = 1;
		panel.add(tfEmail, gbc_tfEmail);
		tfEmail.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Senha");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);

		tfSenha = new JPasswordField();
		GridBagConstraints gbc_tfSenha = new GridBagConstraints();
		gbc_tfSenha.anchor = GridBagConstraints.NORTH;
		gbc_tfSenha.insets = new Insets(0, 0, 5, 0);
		gbc_tfSenha.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfSenha.gridx = 1;
		gbc_tfSenha.gridy = 2;
		panel.add(tfSenha, gbc_tfSenha);

		JLabel lblNewLabel_3 = new JLabel("Confirmar Senha");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;
		panel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		tfConfSenha = new JPasswordField();
		GridBagConstraints gbc_tfConfSenha = new GridBagConstraints();
		gbc_tfConfSenha.anchor = GridBagConstraints.NORTH;
		gbc_tfConfSenha.insets = new Insets(0, 0, 5, 0);
		gbc_tfConfSenha.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfConfSenha.gridx = 1;
		gbc_tfConfSenha.gridy = 3;
		panel.add(tfConfSenha, gbc_tfConfSenha);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(salvarUsuario());
		GridBagConstraints gbc_btnSalvar = new GridBagConstraints();
		gbc_btnSalvar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSalvar.gridwidth = 2;
		gbc_btnSalvar.anchor = GridBagConstraints.NORTH;
		gbc_btnSalvar.insets = new Insets(0, 0, 5, 0);
		gbc_btnSalvar.gridx = 0;
		gbc_btnSalvar.gridy = 4;
		panel.add(btnSalvar, gbc_btnSalvar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(cancelarAcao());
		GridBagConstraints gbc_bntCancelar = new GridBagConstraints();
		gbc_bntCancelar.fill = GridBagConstraints.HORIZONTAL;
		gbc_bntCancelar.gridwidth = 2;
		gbc_bntCancelar.anchor = GridBagConstraints.NORTH;
		gbc_bntCancelar.gridx = 0;
		gbc_bntCancelar.gridy = 5;
		panel.add(btnCancelar, gbc_bntCancelar);
		
		configurarCampos();
	}

	private void configurarCampos() {
		if (usuario != null) {
			tfNome.setText(usuario.getNome());
			tfEmail.setText(usuario.getEmail());
			tfSenha.setText("");
			tfConfSenha.setText("");
			incluir = false;
		}
	}

	private ActionListener cancelarAcao() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tfNome.setText("");
				tfEmail.setText("");
				tfSenha.setText("");
				tfConfSenha.setText("");
				PainelPrincipal.getPainelAbas().remove(2);
			}
		};
	}

	private ActionListener salvarUsuario() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String senha = "";
				if (tfNome.getText().equals("") || tfEmail.getText().equals("") || tfSenha.getPassword().equals("")
						|| tfConfSenha.getPassword().equals("")) {
					JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos!");
					return;
				}

				if (usuario == null) {
					if (!Arrays.equals(tfSenha.getPassword(), tfConfSenha.getPassword())) {
						JOptionPane.showMessageDialog(null, "As senhas não coincidem!");
						tfEmail.transferFocus();
						tfSenha.setText("");
						tfConfSenha.setText("");
						return;
					}
					senha = Md5Util.getMD5Checksum(String.valueOf(tfSenha.getPassword()));
				} else {
					if (!Arrays.equals(null, tfSenha.getPassword())){
						if (!Arrays.equals(tfSenha.getPassword(), tfConfSenha.getPassword())) {
							JOptionPane.showMessageDialog(null, "As senhas não coincidem!");
							tfEmail.transferFocus();
							tfSenha.setText("");
							tfConfSenha.setText("");
							return;
						}	
						senha = Md5Util.getMD5Checksum(String.valueOf(tfSenha.getPassword()));
					} else {
						senha = usuario.getSenha();
					}
				}

				if (incluir){
					ObjectDao.incluir(usuario);										
				} else {
					ObjectDao.alterar(usuario);					
				}

				PainelPrincipal.getPainelAbas().remove(2);
				PainelUsuarios.atualizarTabela();
			}
		};
	}

	/**
	 * @return the dadosUsuario
	 */
	public synchronized static DadosUsuario getDadosUsuario(EntidadeUsuario usuario) {
		if (dadosUsuario == null) {
			dadosUsuario = new DadosUsuario(usuario);
		}
		return dadosUsuario;
	}

}
