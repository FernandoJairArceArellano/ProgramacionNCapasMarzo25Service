package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.MunicipioDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/municipioapi/v1")
public class MunicipioRestController {
    
    @Autowired
    private MunicipioDAOImplementation municipioDAOImplementation;

    @GetMapping("byidestado/{id}")
    public ResponseEntity getMunicipioByEstado(@PathVariable("id") int IdEstado) {
        Result result = municipioDAOImplementation.MunicipioByIdEstadoJPA(IdEstado);
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
