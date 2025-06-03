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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.DateUtil;
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
@Tag(name = "Usuario", description = "Operaciones para gestionar usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;

    @Autowired
    private DireccionDAOImplementation direccionDAOImplementation;

    @GetMapping("saludo")
    @Operation(
            summary = "Saludo al Usuario",
            description = "Permite verificar el estatus del servicio"
    )
    public String Saludo() {
        return "Hola Mundo";
    }

    @GetMapping()
    @Operation(
            summary = "Obtiene todos los usuarios",
            description = "Permite buscar todos los usuarios en la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Usuarios encontrados correctamente"
        ),
        @ApiResponse(
                responseCode = "204",
                description = "No se encontraron usuarios",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Usuarios no encontrados",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
    })
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
    @Operation(
            summary = "Busqueda de usuario con sus direcciones por Id",
            description = "Permite buscar a un usurio por su Id"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado correctamente"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
    })
    public ResponseEntity getById(
            @Parameter(description = "Id del Usuario que se desea buscar", example = "3")
            @PathVariable("id") int idUsuario) {
        Result result = usuarioDAOImplementation.GetByIdJPA(idUsuario);
        //Result result = usuarioDAOImplementation.GetUsuarioById(idUsuario);
        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("getUsuarioById/{id}")
    @Operation(
            summary = "Busqueda de usuario por Id",
            description = "Permite buscar a un usurio por su Id"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
    })
    public ResponseEntity getUsuarioById(
            @Parameter(description = "Id del usuario a buscar", example = "1")
            @PathVariable("id") int idUsuario) {
        //Result result = usuarioDAOImplementation.GetByIdJPA(idUsuario);
        Result result = usuarioDAOImplementation.GetUsuarioById(idUsuario);
        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/getAllDinamico")
    @Operation(
            summary = "Buscar usuarios dinámicamente",
            description = "Permite buscar usuarios con filtros dinámicos usando un objeto Usuario como ejemplo."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
    })
    public ResponseEntity<Result> buscarUsuarios(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Criterios del usuario para búsqueda",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            )
            @RequestBody Usuario usuario) {

        Result result = usuarioDAOImplementation.GetAllDinamicoJPA(usuario);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("updateStatus")
    @Operation(
            summary = "Actualizar el estado (Status) de un usuario",
            description = "Permite actualizar el estado (activo/inactivo) de un usuario sin necesidad de eliminarlo de la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Estado del Usuario encontrado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
    })
    public ResponseEntity<Result> updateStatus(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto Usuario con el nuevo estado a actualizar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            )
            @RequestBody Usuario usuario) {

        Result result = usuarioDAOImplementation.UpdateStatusJPA(usuario);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("direccionById/{IdDireccion}")
    @Operation(
            summary = "Busqueda de una Direccion por Id perteneciente a un Usuario",
            description = "Permite buscar a la Direccion usurio por el Id de la Direccion"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Direccion encontrado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Direccion no encontrado",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
    })
    public ResponseEntity direccionById(
            @Parameter(description = "Id de la Direccion a buscar", example = "3")
            @PathVariable int IdDireccion) {
        Result result = direccionDAOImplementation.GetByIdJPA(IdDireccion);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

    @PutMapping("direccionUpdate/{IdDireccion}")
    @Operation(
            summary = "Actualizacion de una Direccion por su Id",
            description = "Permite buscar a la Direccion usurio por el Id de la Direccion"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Direccion encontrado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Direccion no encontrado",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
    })
    public ResponseEntity<Result> updateDireccion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto Direccion con valores a cambiar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Direccion.class))
            )
            @RequestBody UsuarioDireccion usuarioDireccion) {
        Result result = direccionDAOImplementation.UpdateByIdJPA(usuarioDireccion);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
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
    public ResponseEntity addDireccion(@RequestBody UsuarioDireccion usuarioDireccion) {
        Result result = usuarioDAOImplementation.AddJPA(usuarioDireccion);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.errorMessage);
        }
    }

    @PostMapping("direccion/add")
    public ResponseEntity addUsuarioDireccion(@RequestBody UsuarioDireccion usuarioDireccion) {
        Result result = direccionDAOImplementation.DireccionAddJPA(usuarioDireccion);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + result.errorMessage);
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

    @PostMapping("cargaMasiva")
    public ResponseEntity CargaMasiva(@RequestParam("archivo") MultipartFile archivo) {
        Result result = new Result();
        if (!archivo.isEmpty() || archivo != null) {

            try {
                String tipoArchivo = archivo.getOriginalFilename().split("\\.")[1];

                String root = System.getProperty("user.dir"); //Obtener direccion del proyecto en el equipo
                String path = "src/main/resources/static/archivos"; //Path relativo dentro del proyecto
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
                String absolutePath = root + "/" + path + "/" + fecha + archivo.getOriginalFilename();
                archivo.transferTo(new File(absolutePath));
                //Leer el archivo
                List<UsuarioDireccion> listaUsuarios = new ArrayList();

                if (tipoArchivo.equals("txt")) {
                    listaUsuarios = LecturaArchivoTXT(new File(absolutePath)); //método para leer la lista
                } else {
                    listaUsuarios = LecturaArchivoExcel(new File(absolutePath));
                }

                //Validar el archivo
                //List<ResultFile> listaErrores = new ArrayList<>();
                List<ResultFile> listaErrores = ValidarArchivo(listaUsuarios);

                if (listaErrores.isEmpty()) {
                    //Proceso mi archivo
                    result.correct = true;
                    result.object = absolutePath;
                    return ResponseEntity.ok(result); //ResultFile o Result?
                } else {
                    result.correct = false;
                    result.objects = new ArrayList();

                    for (ResultFile error : listaErrores) {
                        result.objects.add(error);
                    }

                    return ResponseEntity.status(400).body(result);
                }

            } catch (Exception ex) {
                return ResponseEntity.status(500).body("Todo mal");
            }

        } else {
            result.correct = false;
            return ResponseEntity.status(400).body(result);

        }

    }

    public List<UsuarioDireccion> LecturaArchivoTXT(File archivo) {

        //Logica para leer el archivo
        List<UsuarioDireccion> listaUsuarios = new ArrayList<>();

        try (FileReader fileReader = new FileReader(archivo); BufferedReader bufferedReader = new BufferedReader(fileReader);) {
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
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
                    usuarioDireccion.Usuario.setImagen(null);

                    if (DateUtil.isCellDateFormatted(row.getCell(4))) {
                        usuarioDireccion.Usuario.setFNacimiento(row.getCell(4).getDateCellValue());
                    } else {
                        double fechaTexto = row.getCell(4).getNumericCellValue();
                        Date formato = DateUtil.getJavaDate(fechaTexto);
                        usuarioDireccion.Usuario.setFNacimiento(formato);
                    }

                    usuarioDireccion.Usuario.setNCelular(row.getCell(5).toString());

                    usuarioDireccion.Usuario.Rol = new Rol();
                    usuarioDireccion.Usuario.Rol.setIdRol(Integer.parseInt(row.getCell(6).toString()));
                    //usuarioDireccion.Usuario.Rol.setIdRol(Integer.parseInt(row.getCell(6).getStringCellValue()));
                    usuarioDireccion.Usuario.setCURP(row.getCell(7).toString());
                    usuarioDireccion.Usuario.setUsername(row.getCell(8).toString());
                    usuarioDireccion.Usuario.setEmail(row.getCell(9).toString());
                    usuarioDireccion.Usuario.setPassword(row.getCell(10).toString());
                    usuarioDireccion.Usuario.setSexo(row.getCell(11).toString().charAt(0));
                    usuarioDireccion.Usuario.setTelefono(row.getCell(12).toString());
                    usuarioDireccion.Direccion.setCalle(row.getCell(13).toString());
                    usuarioDireccion.Direccion.setNumeroExterior(row.getCell(14).toString());
                    usuarioDireccion.Direccion.setNumeroInterior(row.getCell(15).toString());
                    usuarioDireccion.Direccion.Colonia = new Colonia();
                    usuarioDireccion.Direccion.Colonia.setIdColonia(Integer.parseInt(row.getCell(16).toString()));
                    usuarioDireccion.Usuario.setStatus(Integer.parseInt(row.getCell(17).toString()));
                    listaUsuarios.add(usuarioDireccion);
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
                String nombre = usuarioDireccion.Usuario.getNombre();
                String apellidoPaterno = usuarioDireccion.Usuario.getApellidoPaterno();
                String apellidoMaterno = usuarioDireccion.Usuario.getApellidoMaterno();
                Date fNacimiento = usuarioDireccion.Usuario.getFNacimiento();
                String nCelular = usuarioDireccion.Usuario.getNCelular();
                int idRol = usuarioDireccion.Usuario.Rol.getIdRol();
                String curp = usuarioDireccion.Usuario.getCURP();
                String userName = usuarioDireccion.Usuario.getUsername();
                String email = usuarioDireccion.Usuario.getEmail();
                String password = usuarioDireccion.Usuario.getPassword();
                char sexo = usuarioDireccion.Usuario.getSexo();
                String telefono = usuarioDireccion.Usuario.getTelefono();
                int status = usuarioDireccion.Usuario.getStatus();
                //String imagen = usuarioDireccion.Usuario.getImagen();

                if (nombre == null || nombre.trim().isEmpty()) {
                    listaErrores.add(new ResultFile(fila, nombre, "El nombre es obligatorio"));
                }

                if (apellidoPaterno == null || apellidoPaterno.trim().isEmpty()) {
                    listaErrores.add(new ResultFile(fila, apellidoPaterno, "El apellido paterno es obligatorio"));
                }

                if (apellidoMaterno == null || apellidoMaterno.trim().isEmpty()) {
                    listaErrores.add(new ResultFile(fila, apellidoMaterno, "El apellido materno es obligatorio"));
                }

                if (fNacimiento == null) {
                    listaErrores.add(new ResultFile(fila, "", "La fecha de nacimiento es obligatoria"));
                }

                if (nCelular == null || nCelular.trim().isEmpty()) {
                    listaErrores.add(new ResultFile(fila, nCelular, "El número de celular es obligatorio"));
                } else if (!nCelular.matches("^\\d{10}$")) {
                    listaErrores.add(new ResultFile(fila, nCelular, "El número de celular debe tener 10 dígitos"));
                }

                if (curp == null || curp.trim().isEmpty()) {
                    listaErrores.add(new ResultFile(fila, curp, "El CURP es obligatorio"));
                }

                if (userName == null || userName.trim().isEmpty()) {
                    listaErrores.add(new ResultFile(fila, userName, "El nombre de usuario es obligatorio"));
                }

                if (email == null || email.trim().isEmpty()) {
                    listaErrores.add(new ResultFile(fila, email, "El correo electrónico es obligatorio"));
                }

                if (password == null || password.trim().isEmpty()) {
                    listaErrores.add(new ResultFile(fila, password, "La contraseña es obligatoria"));
                }

                if (sexo != 'M' && sexo != 'F') {
                    listaErrores.add(new ResultFile(fila, String.valueOf(sexo), "El sexo debe ser 'M' o 'F'"));
                }

                if (telefono != null && !telefono.trim().isEmpty() && !telefono.matches("^\\d{10}$")) {
                    listaErrores.add(new ResultFile(fila, telefono, "El teléfono debe tener 10 dígitos si se proporciona"));
                }

                if (idRol <= 0) {
                    listaErrores.add(new ResultFile(fila, String.valueOf(idRol), "El rol debe ser un ID válido"));
                }

                if (status != 0 && status != 1) {
                    listaErrores.add(new ResultFile(fila, String.valueOf(status), "El status debe ser 0 (inactivo) o 1 (activo)"));
                }

                fila++;
            }
        }
        return listaErrores;
    }

    // Procesar archivo
    @PostMapping("/CargaMasiva/Procesar")
    public ResponseEntity<Result> ProcesarArchivo(@RequestBody String absolutePath) {
        Result result = new Result();

        try {
            String tipoArchivo = absolutePath.split("\\.")[1];
            List<UsuarioDireccion> listaUsuarios = new ArrayList();

            if (tipoArchivo.equals("txt")) {
                // Metodo para leer una lista
                listaUsuarios = LecturaArchivoTXT(new File(absolutePath));
            } else {
                listaUsuarios = LecturaArchivoExcel(new File(absolutePath));
            }

            for (UsuarioDireccion usuarioDireccion : listaUsuarios) {
                System.out.println("Estoy agregando un nuevo usuario y direccion");
                usuarioDAOImplementation.AddJPA(usuarioDireccion);
            }
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
        }

        return ResponseEntity.ok(result);
    }
}
