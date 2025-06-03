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
@Schema(description = "Entidad que representa a un Estado en el sistema")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idestado")
    @Schema(description = "Identificador único del Estado", example = "1")
    private int IdEstado;

    @Column(name = "nombre")
    @Schema(description = "Nombre del Estado", example = "México")
    private String Nombre;

    @JoinColumn(name = "idpais")
    @ManyToOne
    @Schema(description = "Llave foranea del identificador único del Pais", example = "1")
    public Pais Pais;

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int IdEstado) {
        this.IdEstado = IdEstado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Pais getPais() {
        return Pais;
    }

    public void setPais(Pais Pais) {
        this.Pais = Pais;
    }

}
