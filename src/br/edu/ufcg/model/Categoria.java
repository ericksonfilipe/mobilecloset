package br.edu.ufcg.model;

import java.io.Serializable;

/**
 * Enumeração das categorias das roupas
 */
public enum Categoria implements Serializable {

	BERMUDA("Bermuda"),
	CALCA("Calça"),
	CAMISA("Camisa"),
	CAMISA_MANGA_LONGA("Camisa manga longa"),
	CAMISETA("Camiseta"),
	SAIA("Saia"),
	SHORT("Short"),
	VESTIDO("Vestido");

	private String nome;

	private Categoria(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return this.nome;
	}

}
