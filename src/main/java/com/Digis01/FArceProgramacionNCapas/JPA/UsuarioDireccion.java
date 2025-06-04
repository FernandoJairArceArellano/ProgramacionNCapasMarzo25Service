package com.Digis01.FArceProgramacionNCapas.JPA;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Objeto que contiene información del usuario y su dirección")
public class UsuarioDireccion {

    @Schema(description = "Información del usuario a registrar")
    public Usuario Usuario;
    
    @Schema(description = "Dirección principal del usuario")
    public List<Direccion> Direcciones;
    
    @Schema(description = "Lista de direcciones adicionales del usuario")
    public Direccion Direccion;

}
