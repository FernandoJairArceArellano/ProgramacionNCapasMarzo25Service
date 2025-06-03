package com.Digis01.FArceProgramacionNCapas.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Schema(description = "Entidad que representa a una Direccion del sistema")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddireccion")
    @Schema(description = "Identificador único de una  direccion", example = "3")
    private int IdDireccion;

    @Column(name = "calle")
    @Schema(description = "Nombre de la calle", example = "Av Revolucion")
    private String Calle;

    @Column(name = "numerointerior")
    @Schema(description = "Numero interior", example = "º 23")
    private String NumeroInterior;

    @Column(name = "numeroexterior")
    @Schema(description = "Numero exterior", example = "º 2")
    private String NumeroExterior;

    @ManyToOne
    @JoinColumn(name = "idcolonia")
    @JsonIgnore
    public Colonia Colonia;

    @ManyToOne
    @JoinColumn(name = "idusuario")
    @JsonIgnore
    public Usuario Usuario;

    public int getIdDireccion() {
        return IdDireccion;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public Colonia getColonia() {
        return Colonia;
    }

    public void setColonia(Colonia Colonia) {
        this.Colonia = Colonia;
    }

    public Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(Usuario Usuario) {
        this.Usuario = Usuario;
    }

}
