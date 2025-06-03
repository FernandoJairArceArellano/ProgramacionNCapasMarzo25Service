package com.Digis01.FArceProgramacionNCapas.JPA;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Objeto de respuesta para las operaciones del sistema")
public class Result<T> {

    @Schema(description = "Indica si la operacion fue exitosa", example = "true")
    public boolean correct; // true / false

    @Schema(description = "Mensaje de erro si la operacion falla", example = "Error en la conexion")
    public String errorMessage; // mensaje descriptivo

    @Schema(description = "Excepcion capturada si una operacion fallo", nullable = true)
    public Exception ex; // maneja toda la exception

    @Schema(description = "Objeto individual devuelto por la operación (cuando no es una lista)", nullable = true)
    public T object;

    @Schema(description = "Lista de objetos devueltos por la operación (usualmente una lista de roles)", nullable = true)
    public List<T> objects;

}
