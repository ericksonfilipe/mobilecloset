package br.edu.ufcg.model;

public class Roupa implements Armazenavel {

	private int id;
	private String caminhoImagem;
	private Categoria categoria;

	public Roupa() {}

	public Roupa(int id, String caminhoImagem, Categoria categoria) {
		this.id = id;
		this.caminhoImagem = caminhoImagem;
		this.categoria = categoria;
	}

	public String getCaminhoImagem() {
		return this.caminhoImagem;
	}

	public void setCaminhoImagem(String path) {
		this.caminhoImagem = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return String.format("Roupa [id = %d, path = %s, categoria = %s]", this.id, this.caminhoImagem, this.categoria);
	}

}
