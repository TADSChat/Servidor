package br.univel.control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
	public synchronized static void adicionarLog(TipoLog tipoLog, String texto){
		try {
			FileWriter file = new FileWriter("log.txt",true);
			BufferedWriter conect = new BufferedWriter(file);
			conect.write(tipoLog.toString());
			conect.write(" - ");
			conect.write(texto);
			conect.newLine();
			conect.close();
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
