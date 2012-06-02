package br.edu.ufcg.model;

public class Look {

	private int id;
	private byte[] imagem;
	
	//Estou salvando isso, pois se salvasse a Roupa, corria o risco do 
	//usuário deletar a roupa e ai ela não estaria mais no bd
	private byte[] logoLojaSuperior;
	private String nomeLojaSuperior;
	private String categoriaRoupaSuperior;
	private String codigoRoupaSuperior;
	
	private byte[] logoLojaInferior;
	private String nomeLojaInferior;
	private String categoriaRoupaInferior;
	private String codigoRoupaInferior;

	public Look() {}

	public Look(int id, byte[] imagem) {
		this.id = id;
		this.imagem = imagem;
	}

	public int getId() {
		return id;
	}
	
	public byte[] getImagem() {
		return imagem;
	}
	
	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public void setCaminhoImagem(byte[] caminhoImagem) {
		this.imagem = caminhoImagem;
	}

	public byte[] getLogoLojaSuperior() {
		return logoLojaSuperior;
	}

	public void setLogoLojaSuperior(byte[] logoLojaSuperior) {
		this.logoLojaSuperior = logoLojaSuperior;
	}

	public String getCodigoRoupaSuperior() {
		return codigoRoupaSuperior;
	}

	public void setCodigoRoupaSuperior(String codigoRoupaSuperior) {
		this.codigoRoupaSuperior = codigoRoupaSuperior;
	}

	public byte[] getLogoLojaInferior() {
		return logoLojaInferior;
	}

	public void setLogoLojaInferior(byte[] logoLojaInferior) {
		this.logoLojaInferior = logoLojaInferior;
	}

	public String getCodigoRoupaInferior() {
		return codigoRoupaInferior;
	}

	public void setCodigoRoupaInferior(String codigoRoupaInferior) {
		this.codigoRoupaInferior = codigoRoupaInferior;
	}

	public String getNomeLojaSuperior() {
		return nomeLojaSuperior;
	}

	public void setNomeLojaSuperior(String nomeLojaSuperior) {
		this.nomeLojaSuperior = nomeLojaSuperior;
	}

	public String getCategoriaRoupaSuperior() {
		return categoriaRoupaSuperior;
	}

	public void setCategoriaRoupaSuperior(String categoriaRoupaSuperior) {
		this.categoriaRoupaSuperior = categoriaRoupaSuperior;
	}

	public String getNomeLojaInferior() {
		return nomeLojaInferior;
	}

	public void setNomeLojaInferior(String nomeLojaInferior) {
		this.nomeLojaInferior = nomeLojaInferior;
	}

	public String getCategoriaRoupaInferior() {
		return categoriaRoupaInferior;
	}

	public void setCategoriaRoupaInferior(String categoriaRoupaInferior) {
		this.categoriaRoupaInferior = categoriaRoupaInferior;
	}

	@Override
	public String toString() {
		return String.format("[id = %d]", this.id);
	}
}
