package br.univel.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.univel.control.HibernateUtil;
import br.univel.control.ObjectDao;
import br.univel.model.dto.Servidor;

public class MainServidor extends JFrame {

	private JTextField tfIpServidor = new JTextField();
	private JNumberField tfPortaServidor = new JNumberField();

	public MainServidor() {
		HibernateUtil.getSession();

		Servidor servidor = (Servidor) ObjectDao.consultarByQuery("from servidor where server_id = 1");

		if (servidor == null) {
			tfIpServidor.enable(true);
			tfPortaServidor.enable(true);
		} else {
			tfIpServidor.enable(false);
			tfPortaServidor.enable(false);
			tfIpServidor.setText(servidor.getIpServer());
			tfPortaServidor.setText(servidor.getPortaServer().toString());
		}
	}

	/**
	 * Adiciona a açao salvar ao botão
	 * 
	 * @return ActionListener para o botao salvar
	 */
	public ActionListener actionConectar() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tfIpServidor.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "IP Invalido para o servidor!");
					return;
				}
				if (tfPortaServidor.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Porta Invalida para o servidor!");
					return;
				}

				Servidor servidor = new Servidor();
				servidor.setIdServer(1).setPortaServer(Integer.parseInt(tfPortaServidor.getText()))
						.setIpServer(tfIpServidor.getText());
				
				ObjectDao.incluir(servidor);
			}
		};
	}

	public static void main(String[] args) {
		new MainServidor();
	}
}
