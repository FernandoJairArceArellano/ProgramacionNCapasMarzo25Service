package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.UsuarioDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.Usuario;
import com.Digis01.FArceProgramacionNCapas.JPA.UsuarioDireccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demoapi")
public class UsuarioRestController {

    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;

    @GetMapping("saludo")
    public String Saludo() {
        return "Hola Mundo";
    }

    @GetMapping()
    public ResponseEntity GetAll() {
        Result result = usuarioDAOImplementation.GetAllJPA();
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

    @GetMapping("/Usuario/GetById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int idUsuario) {
        Result result = usuarioDAOImplementation.GetByIdJPA(idUsuario);
        if (result.correct) {
            UsuarioDireccion usuarioDireccion = (UsuarioDireccion) result.object;
            return ResponseEntity.ok(usuarioDireccion);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/Usuario/Direccion/{id}")
    public ResponseEntity<?> getUsuarioDirecciones(@PathVariable("id") int idUsuario) {
        Result result = usuarioDAOImplementation.DireccionesByIdUsuarioJPA(idUsuario);

        if (result.correct) {
            UsuarioDireccion usuarioDireccion = (UsuarioDireccion) result.object;
            return ResponseEntity.ok(usuarioDireccion);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + result.errorMessage);
        }
    }

    @PostMapping("/Usuario/Add")
    public ResponseEntity addUsuarioDireccion(@RequestBody UsuarioDireccion usuarioDireccion) {
        Result result = usuarioDAOImplementation.AddJPA(usuarioDireccion);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + result.errorMessage);
        }
    }

    @PutMapping("/Usuario/Update")
    public ResponseEntity<?> updateUsuario(@RequestBody Usuario usuario) {
        Result result = usuarioDAOImplementation.UpdateJPA(usuario);

        if (result.correct) {
            return ResponseEntity.ok().body("Usuario actualizado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.errorMessage);
        }
    }

    @DeleteMapping("/Usuario/Delete/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") int idUsuario) {
        Result result = usuarioDAOImplementation.DeleteJPA(idUsuario);

        if (result.correct) {
            return ResponseEntity.ok().body("Usuario eliminado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.errorMessage);
        }
    }

}
