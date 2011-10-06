package br.edu.ufcg.model;

public class Manequim implements Storable {

	private int id;
	private String path;

	public Manequim() {}

	public Manequim(int id, String path) {
		this.id = id;
		setPath(path);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("[id = %d, Caminho = %s]", this.id, this.path);
	}
}
