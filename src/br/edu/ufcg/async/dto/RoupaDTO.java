package br.edu.ufcg.async.dto;


public class RoupaDTO {

	private int id;
	private String codigo;
	private byte[] imagem;
	private String categoria;
	private String loja;

	public RoupaDTO() {}

	public RoupaDTO(int id, byte[] imagem, String categoria) {
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

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getLoja() {
		return loja;
	}

	public void setLoja(String loja) {
		this.loja = loja;
	}

	@Override
	public String toString() {
		return String.format("Roupa [id = %d, categoria = %s]", this.id, this.categoria);
	}

}
