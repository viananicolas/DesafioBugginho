package com.bugginho.nicol.desafiobugginho;

/**
 * Created by nicol on 18/12/2016.
 */

public class Lucro {
    private String id;
    private Double lucroAnual;

    public Lucro(){

    }

    public Lucro(String id, Double lucroAnual)
    {
        this.id=id;
        this.lucroAnual=lucroAnual;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setLucroAnual(Double lucroAnual){
        this.lucroAnual=lucroAnual;
    }
    public String getId(){
        return id;
    }
    public Double getLucroAnual(){
        return lucroAnual;
    }
}
