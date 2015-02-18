/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pdf;

/**
 *
 * @author f7057419
 * 1.0--Alinhamento da AR
 */
public class Oficio {
    
private String oficio;
private String protocolo;
private String sequencial;
private String data;
private String dataExtenso;
private String reu;
private String processo;  
private String Corpo;
private String destinario;  
private String endereco; 
private String linhas; 

    public String getReu() {
        return reu;
    }

    public void setReu(String reu) {
        this.reu = reu;
    }

    public String getDataExtenso() {
        return dataExtenso;
    }

    public void setDataExtenso(String dataExtenso) {
        this.dataExtenso = dataExtenso;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getSequencial() {
        return sequencial;
    }

    public void setSequencial(String sequencial) {
        this.sequencial = sequencial;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public String getCorpo() {
        return Corpo;
    }

    public void setCorpo(String Corpo) {
        this.Corpo = Corpo;
    }

    public String getDestinario() {
        return destinario;
    }

    public void setDestinario(String destinario) {
        this.destinario = destinario;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLinhas() {
        return linhas;
    }

    public void setLinhas(String linhas) {
        this.linhas = linhas;
    }
}