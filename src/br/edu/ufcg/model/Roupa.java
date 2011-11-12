package br.edu.ufcg.model;

public class Roupa {

	private int id;
	private byte[] imagem;
	private Categoria categoria;

	public Roupa() {}

	public Roupa(int id, byte[] imagem, Categoria categoria) {
		this.id = id;
		this.imagem = imagem;
		this.categoria = categoria;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public byte[] getImagem() {
		return this.imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return String.format("Roupa [id = %d, categoria = %s]", this.id, this.categoria);
	}

}
