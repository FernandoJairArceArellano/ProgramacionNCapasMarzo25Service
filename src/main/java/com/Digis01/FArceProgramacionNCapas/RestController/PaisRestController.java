package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.PaisDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paisapi/v1")
@Tag(name = "Pais", description = "Operaciones relacionadas con los Paises")
public class PaisRestController {

    @Autowired
    private PaisDAOImplementation paisDAOImplementation;

    @GetMapping()
    @Operation(
            summary = "Obtiene todos los Paises",
            description = "Retorna todos los Paises registrados en la base de datos."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Paises encontrados correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
        ),
        @ApiResponse(
                responseCode = "204",
                description = "No se encontraron Paises",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Error al recuperar los datos",
                content = @Content
        )
    })
    public ResponseEntity getPais() {
        Result result = paisDAOImplementation.GetAllJPA();
        if (result.correct) {
            if (result.objects.isEmpty()) {
                return ResponseEntity.status(204).body(null);
            } else {
                return ResponseEntity.ok(result);
            }
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}
