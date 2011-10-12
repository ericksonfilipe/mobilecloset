package br.edu.ufcg.model;

public class Manequim implements Armazenavel {

	private int id;
	private String caminhoImagem;

	public Manequim() {}

	public Manequim(int id, String path) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String getCaminhoImagem() {
		return caminhoImagem;
	}
	
	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}
	
	@Override
	public String toString() {
		return String.format("[id = %d, Caminho = %s]", this.id, this.caminhoImagem);
	}
}
