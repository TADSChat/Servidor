package br.univel.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe persistida do Servidor com inteface fluente
 * 
 * @author EHDFREITAS
 *
 */
@Entity
@Table(name = "server")
public class Servidor {

	@Id
	@Column(name = "server_id")
	private Integer idServer;

	@Column(name = "server_ip")
	private String ipServer;

	@Column(name = "server_porta")
	private Integer portaServer;

	/**
	 * @return the idServer
	 */
	public Integer getIdServer() {
		return idServer;
	}

	/**
	 * @param idServer
	 *            the idServer to set
	 */
	public Servidor setIdServer(Integer idServer) {
		this.idServer = idServer;
		return this;
	}

	/**
	 * @return the ipServer
	 */
	public String getIpServer() {
		return ipServer;
	}

	/**
	 * @param ipServer
	 *            the ipServer to set
	 */
	public Servidor setIpServer(final String ipServer) {
		this.ipServer = ipServer;
		return this;
	}

	/**
	 * @return the portaServer
	 */
	public Integer getPortaServer() {
		return portaServer;
	}

	/**
	 * @param portaServer
	 *            the portaServer to set
	 */
	public Servidor setPortaServer(final Integer portaServer) {
		this.portaServer = portaServer;
		return this;
	}
}
