package com.Digis01.FArceProgramacionNCapas.RestController;

import com.Digis01.FArceProgramacionNCapas.DAO.DireccionDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.DAO.UsuarioDAOImplementation;
import com.Digis01.FArceProgramacionNCapas.JPA.Colonia;
import com.Digis01.FArceProgramacionNCapas.JPA.Direccion;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.ResultFile;
import com.Digis01.FArceProgramacionNCapas.JPA.Rol;
import com.Digis01.FArceProgramacionNCapas.JPA.Usuario;
import com.Digis01.FArceProgramacionNCapas.JPA.UsuarioDireccion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/usuarioapi/v1")
public class UsuarioRestController {

    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;

    @Autowired
    private DireccionDAOImplementation direccionDAOImplementation;

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

    @GetMapping("getById/{id}")
    public ResponseEntity getById(@PathVariable("id") int idUsuario) {
        Result result = usuarioDAOImplementation.GetByIdJPA(idUsuario);
        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("direccionById/{IdDireccion}")
    public ResponseEntity direccionById(@PathVariable int IdDireccion){
        Result result = direccionDAOImplementation.GetByIdJPA(IdDireccion);
        
        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }
    
    @DeleteMapping("deletedireccion/{IdDireccion}")
    public ResponseEntity deleteDireccionById(@PathVariable int IdDireccion) {
        Result result = direccionDAOImplementation.DeleteDireccionJPA(IdDireccion);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error" + result.errorMessage);
        }
    }

    @PostMapping("add")
    public ResponseEntity addUsuarioDireccion(@RequestBody UsuarioDireccion usuarioDireccion) {
        Result result = usuarioDAOImplementation.AddJPA(usuarioDireccion);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + result.errorMessage);
        }
    }

    @PostMapping("direccion/add")
    public ResponseEntity addDireccion(@RequestBody UsuarioDireccion usuarioDireccion) {
        Result result = direccionDAOImplementation.DireccionAddJPA(usuarioDireccion);

        if (result.correct) {
            return ResponseEntity.ok().body("Direccion agregada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.errorMessage);
        }
    }

    @PutMapping("update")
    public ResponseEntity updateUsuario(@RequestBody Usuario usuario) {
        Result result = usuarioDAOImplementation.UpdateJPA(usuario);

        if (result.correct) {
            return ResponseEntity.ok().body("Usuario actualizado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.errorMessage);
        }
    }

    @DeleteMapping("delete/{IdUsuario}")
    public ResponseEntity deleteUsuario(@PathVariable int IdUsuario) {

        Result result = usuarioDAOImplementation.DeleteJPA(IdUsuario);

        if (result.correct) {
            String mensaje = "Usuario eliminado correctamente.";
            return ResponseEntity.ok().body(mensaje);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cargaMasiva")
    public ResponseEntity CargaMasiva(@RequestParam MultipartFile archivo) {

        if (!archivo.isEmpty() || archivo != null) {

            try {
                String tipoArchivo = archivo.getOriginalFilename().split("\\.")[1];

                String root = System.getProperty("user.dir"); //Obtener direccion del proyecto en el equipo
                String path = "src/main/resources/static/archivos"; //Path relativo dentro del proyecto
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
                String absolutePath = root + "/" + path + "/" + fecha + archivo.getOriginalFilename();

                //Leer el archivo
                List<UsuarioDireccion> listaUsuarios = new ArrayList();
                if (tipoArchivo.equals("txt")) {
                    listaUsuarios = LecturaArchivoTXT(new File(absolutePath)); //método para leer la lista
                } else {
                    listaUsuarios = LecturaArchivoExcel(new File(absolutePath));
                }

                //Validar el archivo
                List<ResultFile> listaErrores = ValidarArchivo(listaUsuarios);

                if (listaErrores.isEmpty()) {
                    //Proceso mi archivo
                    archivo.transferTo(new File(absolutePath));
                    return ResponseEntity.ok(listaErrores); //ResultFile o Result?
                } else {
                    return ResponseEntity.noContent().build();
                }

            } catch (Exception ex) {
                return ResponseEntity.status(500).body("Todo mal");
            }

        }

        return null;
    }

    public List<UsuarioDireccion> LecturaArchivoTXT(File archivo) {

        //Logica para leer el archivo
        List<UsuarioDireccion> listaUsuarios = new ArrayList<>();

        try (FileReader fileReader = new FileReader(archivo); BufferedReader br = new BufferedReader(fileReader);) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split("\\|");

                // Validación rápida del número de campos esperados
                if (campos.length != 18) {
                    System.out.println("ERROR: Línea con número de campos incorrecto:");
                    System.out.println("Esperados: 18, Recibidos: " + campos.length);
                    System.out.println(linea);
                    continue;
                }

                UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
                usuarioDireccion.Usuario = new Usuario();

                usuarioDireccion.Usuario.setNombre(campos[0]);               // 1
                usuarioDireccion.Usuario.setApellidoPaterno(campos[1]);      // 2
                usuarioDireccion.Usuario.setApellidoMaterno(campos[2]);      // 3
                usuarioDireccion.Usuario.setImagen(null);               // 4

                // Fecha de nacimiento
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                usuarioDireccion.Usuario.setFNacimiento(formatter.parse(campos[4]));  // 5

                usuarioDireccion.Usuario.setNCelular(campos[5]);             // 6

                usuarioDireccion.Usuario.Rol = new Rol();
                usuarioDireccion.Usuario.Rol.setIdRol(Integer.parseInt(campos[6]));  // 7

                usuarioDireccion.Usuario.setCURP(campos[7]);                 // 8
                usuarioDireccion.Usuario.setUsername(campos[8]);            // 9
                usuarioDireccion.Usuario.setEmail(campos[9]);               // 10
                usuarioDireccion.Usuario.setPassword(campos[10]);           // 11

                String sexoStr = campos[11];                                 // 12
                usuarioDireccion.Usuario.setSexo(sexoStr != null && !sexoStr.isEmpty() ? sexoStr.charAt(0) : null);

                usuarioDireccion.Usuario.setTelefono(campos[12]);           // 13

                usuarioDireccion.Direccion = new Direccion();
                usuarioDireccion.Direccion.setCalle(campos[13]);            // 14
                usuarioDireccion.Direccion.setNumeroExterior(campos[14]);   // 15
                usuarioDireccion.Direccion.setNumeroInterior(campos[15]);   // 16

                usuarioDireccion.Direccion.Colonia = new Colonia();
                usuarioDireccion.Direccion.Colonia.setIdColonia(Integer.parseInt(campos[16])); // 17

                // El último campo puede ser ignorado 
                usuarioDireccion.Usuario.setStatus(Integer.parseInt(campos[17]));  // 18
                listaUsuarios.add(usuarioDireccion);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            listaUsuarios = null;
        }

        return listaUsuarios;
    }

    public List<UsuarioDireccion> LecturaArchivoExcel(File archivo) {
        List<UsuarioDireccion> listaUsuarios = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(archivo);) {
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
                    usuarioDireccion.Usuario = new Usuario();
                    usuarioDireccion.Usuario.setNombre(row.getCell(0).toString());
                    usuarioDireccion.Usuario.setApellidoPaterno(row.getCell(1).toString());
                    usuarioDireccion.Usuario.setApellidoMaterno(row.getCell(2).toString());
                    usuarioDireccion.Usuario.setEmail(row.getCell(3).toString());
                    usuarioDireccion.Usuario.Rol = new Rol();
                    usuarioDireccion.Usuario.Rol.setIdRol(Integer.parseInt(row.getCell(4).toString()));
                    //usuarioDireccion.Usuario.setStatus(row.getCell(3) != null ? (int) row.getCell(3).getNumericCellValue() : 0 );

                }
            }
        } catch (Exception ex) {
            System.out.println("Error al abrir el archivo");
        }

        return listaUsuarios;
    }

    public List<ResultFile> ValidarArchivo(List<UsuarioDireccion> listaUsuarios) {
        List<ResultFile> listaErrores = new ArrayList<>();

        if (listaUsuarios == null) {
            listaErrores.add(new ResultFile(0, "La lista es nula", "La lista es nula"));
        } else if (listaUsuarios.isEmpty()) {
            listaErrores.add(new ResultFile(0, "La lista está vacía", "La lista está vacía"));
        } else {
            int fila = 1;
            for (UsuarioDireccion usuarioDireccion : listaUsuarios) {
                if (usuarioDireccion.Usuario.getNombre() == null || usuarioDireccion.Usuario.getNombre().equals("")) {
                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getNombre(), "El nombre es un campo oligatorio"));
                }

                if (usuarioDireccion.Usuario.getApellidoPaterno() == null || usuarioDireccion.Usuario.getApellidoPaterno().equals("")) {
                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getApellidoPaterno(), "El Apellido Paterno es un campo oligatorio"));
                }

                if (usuarioDireccion.Usuario.getUsername() == null || usuarioDireccion.Usuario.getUsername().equals("")) {
                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getApellidoPaterno(), "El Username es un campo oligatorio"));
                }
                fila++;
            }
        }
        return listaErrores;
    }
}
