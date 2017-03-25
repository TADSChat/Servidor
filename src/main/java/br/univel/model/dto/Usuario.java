package br.univel.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe persistida do Usuario com inteface fluente
 * 
 * @author Duh
 *
 */
@Entity
@Table(name = "user")
public class Usuario {

	@Id
	@GeneratedValue
	@Column(name = "user_id", columnDefinition = "serial")
	private Integer id;

	@Column(name = "user_name")
	private String nome;

	@Column(name = "user_login")
	private String login;

	@Column(name = "user_password")
	private String senha;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public Usuario setId(final Integer id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public Usuario setNome(final String nome) {
		this.nome = nome;
		return this;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public Usuario setLogin(final String login) {
		this.login = login;
		return this;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha the senha to set
	 */
	public Usuario setSenha(final String senha) {
		this.senha = senha;
		return this;
	}

	
}
