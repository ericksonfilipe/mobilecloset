package br.edu.ufcg.model;

public class Roupa implements Storable {

	private int id;
	private String path;
	private Categoria categoria;

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
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
		return String.format("Roupa [id = %d, path = %s, categoria = %s]", this.id, this.path, this.categoria);
	}

}
