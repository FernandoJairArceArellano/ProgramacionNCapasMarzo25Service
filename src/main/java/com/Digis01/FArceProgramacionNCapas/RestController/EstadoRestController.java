package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.EstadoDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadoapi/v1")
public class EstadoRestController {
    
    @Autowired
    private EstadoDAOImplementation estadoDAOImplementation;

    @GetMapping("byidpais/{IdPais}")
    public ResponseEntity getEstadoByIdPais(@PathVariable("IdPais") int IdPais) {
        Result result = estadoDAOImplementation.EstadoByIdPaisJPA(IdPais);
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
