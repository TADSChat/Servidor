package br.univel.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.univel.control.HibernateUtil;
import br.univel.control.ObjectDao;
import br.univel.model.dto.Servidor;

/**
 * Classe para execução do servidor
 * @author Dread
 *
 */
public class MainServidor extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField tfIpServidor = new JTextField();
	private JNumberField tnPortaServidor = new JNumberField();
	
	Dimension dimensaoTela = Toolkit.getDefaultToolkit().getScreenSize();

	public MainServidor() {
		HibernateUtil.getSession();
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(300, 200);
		this.setResizable(false);
		setLocation((dimensaoTela.width - this.getSize().width) / 2, (dimensaoTela.height - this.getSize().height) / 2);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblNewLabel = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		tfIpServidor = new JTextField();
		GridBagConstraints gbc_tfIpServidor = new GridBagConstraints();
		gbc_tfIpServidor.insets = new Insets(0, 0, 5, 0);
		gbc_tfIpServidor.gridx = 0;
		gbc_tfIpServidor.gridy = 1;
		panel.add(tfIpServidor, gbc_tfIpServidor);
		tfIpServidor.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		tnPortaServidor = new JNumberField();
		GridBagConstraints gbc_tfPortaServidor = new GridBagConstraints();
		gbc_tfPortaServidor.insets = new Insets(0, 0, 5, 0);
		gbc_tfPortaServidor.gridx = 0;
		gbc_tfPortaServidor.gridy = 3;
		panel.add(tnPortaServidor, gbc_tfPortaServidor);
		tnPortaServidor.setColumns(10);

		JButton btnConnect = new JButton("New button");
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.gridx = 0;
		gbc_btnConnect.gridy = 4;
		panel.add(btnConnect, gbc_btnConnect);
		HibernateUtil.getSession();

		Servidor servidor = (Servidor) ObjectDao.consultarByQuery("from Servidor where server_id = 1");

		if (servidor == null) {
			tfIpServidor.enable(true);
			tnPortaServidor.enable(true);
			btnConnect.addActionListener(actionCriar());
		} else {
			tfIpServidor.enable(false);
			tnPortaServidor.enable(false);
			tfIpServidor.setText(servidor.getIpServer());
			tnPortaServidor.setText(servidor.getPortaServer().toString());
			btnConnect.addActionListener(actionConectar());
		}
	}

	/**
	 * Adiciona a açao para criar registro ao botão
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
				if (tnPortaServidor.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Porta Invalida para o servidor!");
					return;
				}

				Servidor servidor = new Servidor();
				servidor.setIdServer(1).setPortaServer(Integer.parseInt(tnPortaServidor.getText()))
						.setIpServer(tfIpServidor.getText());

				ObjectDao.incluir(servidor);
				conectar(servidor.getIpServer(), servidor.getPortaServer());
			}

		};
	}

	/**
	 * Adiciona a açao para conectar no botão
	 * 
	 * @return ActionListener para o botao salvar
	 */
	public ActionListener actionConectar() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				conectar(tfIpServidor.getText(), tnPortaServidor.getNumber());
			}
		};
	}

	
	/**
	 * Executa a conexao do servidor
	 * @param ipServer
	 * @param portaServer
	 */
	private void conectar(String ipServer, Integer portaServer) {
		this.dispose();
		new PainelServidor(ipServer, portaServer);
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
