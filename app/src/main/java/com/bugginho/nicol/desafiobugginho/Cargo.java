package com.bugginho.nicol.desafiobugginho;

/**
 * Created by nicol on 18/12/2016.
 */

public class Cargo {
    private String id;
    private String nomeCargo;
    private Double bonus;

    public Cargo(){

    }

    public Cargo(String id, String nomeCargo, double bonus)
    {
        this.id=id;
        this.nomeCargo=nomeCargo;
        this.bonus=bonus;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setNomeCargo(String nomeCargo){
        this.nomeCargo=nomeCargo;
    }
    public void setBonus(Double bonus){
        this.bonus=bonus;
    }
    public String getId(){
        return id;
    }
    public String getNomeCargo(){
        return nomeCargo;
    }
    public Double getBonus(){
        return bonus;
    }
}
