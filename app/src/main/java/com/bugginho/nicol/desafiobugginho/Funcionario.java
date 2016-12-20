package com.bugginho.nicol.desafiobugginho;
import java.io.Serializable;
/**
 * Created by nicol on 18/12/2016.
 */

public class Funcionario implements Serializable {
    private String id;
    private String nome;
    private String cpf;
    private String cargo;
    private Double salario;
    private Double bonusSalario;

    public Funcionario(){

    }
    public Funcionario(String id, String nome, String cargo, String cpf, Double salario){
        this.id=id;
        this.nome=nome;
        this.cpf=cpf;
        this.cargo=cargo;
        this.salario=salario;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setNome(String nome)
    {
        this.nome=nome;
    }
    public void setCpf(String cpf)
    {
        this.cpf=cpf;
    }
    public void setCargo(String cargo){
        this.cargo=cargo;
    }
    public void setSalario(Double salario){
        this.salario=salario;
    }
    public void setBonusSalario(Double bonusSalario){
        this.bonusSalario=bonusSalario;
    }
    public String getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public String getCpf(){
        return cpf;
    }
    public Double getSalario(){
        return salario;
    }
    public String getCargo(){
        return cargo;
    }
    public double getBonusSalario(){
        return bonusSalario;
    }
}
