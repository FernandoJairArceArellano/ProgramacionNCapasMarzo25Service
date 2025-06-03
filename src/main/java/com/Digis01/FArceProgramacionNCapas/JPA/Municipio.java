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
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmunicipio")
    @Schema(description = "Identificador único del Municipio", example = "1")
    private int IdMunicipio;

    @Column(name = "nombre")
    @Schema(description = "Nombre del Municipio", example = "Texcoco")
    private String Nombre;

    @JoinColumn(name = "idestado")
    @ManyToOne
    @Schema(description = "Llave foranea del identificador único del Estado", example = "1")
    public Estado Estado;

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(int IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Estado getEstado() {
        return Estado;
    }

    public void setEstado(Estado Estado) {
        this.Estado = Estado;
    }

}
