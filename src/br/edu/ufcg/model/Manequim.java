package br.edu.ufcg.model;

public class Manequim {

	private int id;
	private byte[] imagem;

	public Manequim() {}

	public Manequim(int id, byte[] imagem) {
		this.id = id;
		this.imagem = imagem;
	}

	public int getId() {
		return id;
	}
	
	public byte[] getImagem() {
		return imagem;
	}
	
	public void setCaminhoImagem(byte[] caminhoImagem) {
		this.imagem = caminhoImagem;
	}
	
	@Override
	public String toString() {
		return String.format("[id = %d]", this.id);
	}
}
