package br.edu.ufcg.model;

public class Calibragem {

	private Categoria categoria;
	private Double posicaoX;
	private Double posicaoY;
	private Double altura;
	private Double largura;

	public Calibragem() {}

	public Calibragem(Categoria categoria, Double posicaoX, Double posicaoY,
			Double altura, Double largura) {
		this.categoria = categoria;
		this.posicaoX = posicaoX;
		this.posicaoY = posicaoY;
		this.altura = altura;
		this.largura = largura;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Double getPosicaoX() {
		return posicaoX;
	}

	public void setPosicaoX(Double posicaoX) {
		this.posicaoX = posicaoX;
	}

	public Double getPosicaoY() {
		return posicaoY;
	}

	public void setPosicaoY(Double posicaoY) {
		this.posicaoY = posicaoY;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Double getLargura() {
		return largura;
	}

	public void setLargura(Double largura) {
		this.largura = largura;
	}

	@Override
	public String toString() {
		return "Categoria = " + categoria + ", posx = " + posicaoX + ", posy = " + posicaoY;
	}
}
