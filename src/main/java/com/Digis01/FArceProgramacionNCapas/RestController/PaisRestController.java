package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.PaisDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paisapi/v1")
public class PaisRestController {

    @Autowired
    private PaisDAOImplementation paisDAOImplementation;

    @GetMapping()
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
