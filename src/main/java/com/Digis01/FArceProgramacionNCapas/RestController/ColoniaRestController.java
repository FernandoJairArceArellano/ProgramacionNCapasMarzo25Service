package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.ColoniaDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coloniaapi/v1")
@Tag(name = "Colonia", description = "Operaciones relacionadas con las Colonias")
public class ColoniaRestController {

    @Autowired
    private ColoniaDAOImplementation coloniaDAOImplementation;

    @GetMapping("byidmunicipio/{IdMunicipio}")
    @Operation(
            summary = "Obtiene todos las Colonias",
            description = "Retorna todos las Colonias registrados en la base de datos relacionados con el Id de un Municipio"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Colonias encontrados correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
        ),
        @ApiResponse(
                responseCode = "204",
                description = "No se encontraron Municipios",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Error al recuperar los datos del Municipio",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
    })
    public ResponseEntity getMunicipioByIdEstado(
            @Parameter(description = "Id del Municipio, para buscar todos las Colonias relacionados", example = "1")
            @PathVariable("IdMunicipio") int IdMunicipio) {
        Result result = coloniaDAOImplementation.ColoniaByIdMunicipioJPA(IdMunicipio);
        if (result.correct) {
            if (result.object == null) {
                return ResponseEntity.status(204).body(null);
            } else {
                return ResponseEntity.ok(result);
            }
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}
