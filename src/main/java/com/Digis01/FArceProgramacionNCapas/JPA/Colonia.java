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
@Schema(description = "Entidad que representa a un Municipio en el sistema")
public class Colonia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcolonia")
    @Schema(description = "Identificador único de la Colonia", example = "1")
    private int IdColonia;

    @Column(name = "nombre")
    @Schema(description = "Nombre de la Colonia", example = "Texcocana")
    private String Nombre;

    @Column(name = "codigopostal")
    @Schema(description = "Codigo postal perteneciente a la colonia", example = "56386")
    private String CodigoPostal;

    @JoinColumn(name = "idmunicipio")
    @ManyToOne
    @Schema(description = "Llave foranea del identificador único del Municipio", example = "1")
    public Municipio Municipio;

    public Municipio getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(Municipio Municipio) {
        this.Municipio = Municipio;
    }

    public int getIdColonia() {
        return IdColonia;
    }

    public void setIdColonia(int IdColonia) {
        this.IdColonia = IdColonia;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }

}
