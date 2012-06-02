package br.edu.ufcg.model;

public class Calibragem {

	private Categoria categoria;
	public Integer left;
	public Integer top;
	public Integer right;
	public Integer bottom;

	public Calibragem() {}

	public Calibragem(Categoria categoria, Integer left, Integer top,
			Integer right, Integer bottom) {
		this.categoria = categoria;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return String.format("%s - %d %d %d %d", categoria, left, top, right, bottom);
	}
}
