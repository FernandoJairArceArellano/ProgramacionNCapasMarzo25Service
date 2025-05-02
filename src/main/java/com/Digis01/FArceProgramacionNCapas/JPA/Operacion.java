package com.Digis01.FArceProgramacionNCapas.JPA;

public class Operacion {
    private String tipo;
    private int operando1;
    private int operando2;
    private String resultado;

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getOperando1() {
        return operando1;
    }
    public void setOperando1(int operando1) {
        this.operando1 = operando1;
    }
    public int getOperando2() {
        return operando2;
    }
    public void setOperando2(int operando2) {
        this.operando2 = operando2;
    }
    public String getResultado() {
        return resultado;
    }
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}