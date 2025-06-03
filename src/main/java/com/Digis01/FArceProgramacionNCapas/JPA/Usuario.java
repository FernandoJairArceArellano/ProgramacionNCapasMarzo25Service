package com.Digis01.FArceProgramacionNCapas.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Schema(description = "Entidad que representa a un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    @Schema(description = "Identificador único del usuario", example = "1")
    private int IdUsuario;

    @Column(name = "nombre")
    @Schema(description = "Nombre del usuario", example = "Fernando Jair")
    private String Nombre;

    @Column(name = "apellidopaterno")
    @Schema(description = "Apellido Paterno del usuario", example = "Arce")
    private String ApellidoPaterno;

    @Column(name = "apellidomaterno")
    @Schema(description = "Apellido Materno del usuario", example = "Arellano")
    private String ApellidoMaterno;

    @Column(name = "username")
    @Schema(description = "Username del usuario", example = "fernandoArce")
    private String Username;

    @Column(name = "email")
    @Schema(description = "Correo electrónico del usuario", example = "fernandoArce@mail.com")
    private String Email;

    @Column(name = "password")
    @Schema(description = "Contraseña del usuario", example = "hash contraseña")
    private String Password;

    @Column(name = "fnacimiento")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de nacimiento del usuario", example = "2000-01-01")
    private Date FNacimiento;

    @Column(name = "sexo")
    @Schema(description = "Sexo del usuario, por ejemplo 'M' o 'F'", example = "M")
    private char Sexo;

    @Column(name = "telefono")
    @Schema(description = "Teléfono del usuario", example = "5551234567")
    private String Telefono;

    @Column(name = "ncelular")
    @Schema(description = "Número de celular del usuario", example = "5512345678")
    private String NCelular;

    @Column(name = "curp")
    @Schema(description = "CURP del usuario", example = "AAAA001010HDFXXX01")
    private String CURP;

    @Column(name = "status")
    @Schema(description = "Estatus del usuario en el sistema", example = "1 o 0")
    private int Status;

    @Lob
    @Column(name = "imagen")
    @Schema(description = "Imagen de Usuario")
    private String Imagen;

    @JoinColumn(name = "idrol")
    @JsonIgnore
    @ManyToOne
    public Rol Rol;

    /*  Relación uno-a-muchos: un usuario puede tener muchas direcciones.
        mappedBy indica que la relación está mapeada por la propiedad 'Usuario' en Direccion.
        fetch LAZY evita cargar las direcciones automáticamente hasta que se necesiten.
    //@OneToMany(mappedBy = "Usuario", fetch = FetchType.LAZY)
    @OneToMany
    private List<Direccion> Direcciones;*/
    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public Date getFNacimiento() {
        return FNacimiento;
    }

    public void setFNacimiento(Date FNacimiento) {
        this.FNacimiento = FNacimiento;
    }

    public char getSexo() {
        return Sexo;
    }

    public void setSexo(char Sexo) {
        this.Sexo = Sexo;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getNCelular() {
        return NCelular;
    }

    public void setNCelular(String NCelular) {
        this.NCelular = NCelular;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public Rol getRol() {
        return Rol;
    }

    public void setRol(Rol Rol) {
        this.Rol = Rol;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }

    /*public List<Direccion> getDirecciones() {
        return Direcciones;
    }

    public void setDirecciones(List<Direccion> Direcciones) {
        this.Direcciones = Direcciones;
    }*/
}
