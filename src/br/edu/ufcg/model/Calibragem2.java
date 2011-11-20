package br.edu.ufcg.model;

public class Calibragem2 {

	private Roupa roupa;
	public Integer left;
	public Integer top;
	public Integer right;
	public Integer bottom;

	public Calibragem2() {}

	public Calibragem2(Roupa roupa, Integer left, Integer top,
			Integer right, Integer bottom) {
		this.roupa = roupa;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}


	public Roupa getRoupa() {
		return roupa;
	}

	public void setRoupa(Roupa roupa) {
		this.roupa = roupa;
	}

	@Override
	public String toString() {
		return String.format("%s - %d %d %d %d", roupa, left, top, right, bottom);
	}
}
