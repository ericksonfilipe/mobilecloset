package br.edu.ufcg.model;

import java.io.Serializable;

public class Roupa implements Serializable {

	private static final long serialVersionUID = -1575837948040959794L;
	private int id;
	private String codigo;
	private byte[] imagem;
	private Categoria categoria;
	private Loja loja;

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	@Override
	public String toString() {
		return String.format("Roupa [id = %d, categoria = %s]", this.id, this.categoria);
	}

}
