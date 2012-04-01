package br.edu.ufcg.model;

public class Calibragem2 {

	private int id;

	private Integer idRoupa;
	public Integer left;
	public Integer top;
	public Integer right;
	public Integer bottom;

	public Calibragem2() {}

	public Calibragem2(Integer idRoupa, Integer left, Integer top,
			Integer right, Integer bottom) {
		this.idRoupa = idRoupa;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getRoupa() {
		return idRoupa;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return String.format("%s - %d %d %d %d", idRoupa, left, top, right, bottom);
	}

}
