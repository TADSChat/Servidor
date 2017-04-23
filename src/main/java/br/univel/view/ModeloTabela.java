package br.univel.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import common.EntidadeUsuario;

public class ModeloTabela extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Object[][] matrix;

	/**
	 * Código precisa ser revisado caso haja um cliente (chave) com lista nula
	 * ou vazia, pois assume-se que todas as chaves tenham no mínimo um arquivo.
	 * 
	 * @param dados
	 */
	public ModeloTabela(List<EntidadeUsuario> usuarios) {

		int tempCli = usuarios.size();
		matrix = new Object[tempCli][5];

		if (usuarios.size() > 2) {
			usuarios.sort((o1, o2) -> o1.getNome().compareTo(o2.getNome()));
		}

		int cont = 0;
		for (EntidadeUsuario usuario : usuarios) {
			matrix[cont][0] = usuario.getNome();
			matrix[cont][1] = usuario.getEmail();
			matrix[cont][2] = usuario.getStatus();
			matrix[cont][3] = usuario.getSenha();
			matrix[cont][4] = usuario.getId();
			cont++;
		}
	}

	@Override
	public String getColumnName(int i) {
		switch (i) {
		case 0:
			return "Nome";
		case 1:
			return "Email";
		case 2:
			return "Status";
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return matrix.length;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return matrix[arg0][arg1];
	}
}