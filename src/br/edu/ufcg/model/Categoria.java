package br.edu.ufcg.model;

/**
 * Enumera��o das categorias das roupas
 */
public enum Categoria {

	BERMUDA("Bermuda"),
	CALCA("Cal�a"),
	CAMISA("Camisa"),
	CAMISA_MANGA_LONGA("Camisa manga longa"),
	CAMISETA("Camiseta"),
	SAIA("Saia"),
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
