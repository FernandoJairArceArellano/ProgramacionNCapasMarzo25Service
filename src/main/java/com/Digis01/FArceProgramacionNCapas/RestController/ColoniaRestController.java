package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.ColoniaDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coloniaapi/v1")
public class ColoniaRestController {
    
    @Autowired
    private ColoniaDAOImplementation coloniaDAOImplementation;
    
    @GetMapping("byidmunicipio/{IdMunicipio}")
    public ResponseEntity getMunicipioByIdEstado(@PathVariable("IdMunicipio") int IdMunicipio) {
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
