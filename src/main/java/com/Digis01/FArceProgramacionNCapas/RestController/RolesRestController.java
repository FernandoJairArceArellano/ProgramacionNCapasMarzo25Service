package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.RolDAOImplementation;
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
@RequestMapping("/rolapi/v1")
@Tag(name = "Roles", description = "Operaciones relacionadas con los roles del Usuario")
public class RolesRestController {

    @Autowired
    private RolDAOImplementation rolDAOImplementation;

    @Operation(
            summary = "Obtener todos los roles",
            description = "Retorna todos los roles registrados en la base de  datos."
    )

    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Roles encontrados correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
        ),
        @ApiResponse(
                responseCode = "204",
                description = "No se encontraron roles",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Error al recuperar los roles",
                content = @Content
        )
    })

    @GetMapping()
    public ResponseEntity GetAllRoles() {
        Result result = rolDAOImplementation.GetAllJPA();
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
