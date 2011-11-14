package br.edu.ufcg.model;

import java.io.Serializable;

/**
 * Enumeração das categorias das roupas
 */
public enum Categoria implements Serializable {

    BERMUDA("Bermuda", "molde_short.png"),  //segundo Savyo, ficaria como um short aumentado
    CALCA("Calça", "molde_calca.png"),
    CAMISA("Camisa", "molde_camisa.png"),
    CAMISA_MANGA_LONGA("Camisa manga longa", "molde_camisao.png"),
    CAMISETA("Camiseta", "molde_camiseta.png"),
    SAIA("Saia", "molde_saia.png"),
    SHORT("Short", "molde_short.png"),
    VESTIDO("Vestido", "molde_camiseta.png"); //segundo Savyo, vestido ficaria como uma camisa aumentada

    private String nome;
    private String molde;

    private Categoria(String nome, String molde) {
            this.nome = nome;
            this.molde = molde;
    }

    public String getNome() {
            return nome;
    }

    public void setNome(String nome) {
            this.nome = nome;
    }

    @Override
    public String toString() {
            return this.nome;
    }
   
    public String getMolde() {
            return molde;
    }


}
