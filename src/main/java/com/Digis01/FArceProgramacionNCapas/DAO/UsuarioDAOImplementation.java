package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Colonia;
import com.Digis01.FArceProgramacionNCapas.JPA.Direccion;
import com.Digis01.FArceProgramacionNCapas.JPA.Estado;
import com.Digis01.FArceProgramacionNCapas.JPA.Municipio;
import com.Digis01.FArceProgramacionNCapas.JPA.Pais;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.Rol;
import com.Digis01.FArceProgramacionNCapas.JPA.Usuario;
import com.Digis01.FArceProgramacionNCapas.JPA.UsuarioDireccion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.sql.CallableStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// Logica de base de datos
@Repository
public class UsuarioDAOImplementation implements IUsuarioDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAllJPA() {
        Result result = new Result();

        try {
            List<Usuario> usuarios = entityManager
                    .createQuery("FROM Usuario ORDER BY IdUsuario", Usuario.class)
                    .getResultList();

            List<UsuarioDireccion> listaUsuarioDireccion = new ArrayList<>();

            for (Usuario usuario : usuarios) {
                UsuarioDireccion ud = new UsuarioDireccion();
                ud.Usuario = usuario;
                //ud.Direcciones = usuario.getDirecciones();

                List<Direccion> direcciones = entityManager
                        .createQuery("FROM Direccion WHERE Usuario.IdUsuario = :id", Direccion.class)
                        .setParameter("id", usuario.getIdUsuario())
                        .getResultList();
                ud.Direcciones = direcciones;
                listaUsuarioDireccion.add(ud);
            }

            result.objects = new ArrayList<Object>(listaUsuarioDireccion);
            result.correct = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetByIdJPA(int idUsuario) {
        Result result = new Result();

        try {
            // Buscar al usuario por ID
            Usuario usuario = entityManager
                    .createQuery("FROM Usuario WHERE IdUsuario = :idUsuario", Usuario.class)
                    .setParameter("idUsuario", idUsuario)
                    .getSingleResult();

            if (usuario != null) {
                UsuarioDireccion ud = new UsuarioDireccion();
                ud.Usuario = usuario;

                // Buscar direcciones del usuario
                List<Direccion> direcciones = entityManager
                        .createQuery("FROM Direccion WHERE Usuario.IdUsuario = :id", Direccion.class)
                        .setParameter("id", idUsuario)
                        .getResultList();

                ud.Direcciones = direcciones;

                result.object = ud;
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado.";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result AddJPA(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            // Guardar usuario
            entityManager.persist(usuarioDireccion.Usuario);

            usuarioDireccion.Direccion.Usuario = usuarioDireccion.Usuario;
            // Guardar dirección
            entityManager.persist(usuarioDireccion.Direccion);

            result.correct = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result UpdateJPA(Usuario usuario) {
        Result result = new Result();

        try {
            // Buscar usuario existente
            Usuario usuarioJPA = entityManager.find(Usuario.class, usuario.getIdUsuario());

            if (usuarioJPA != null) {
                // Actualizar campos
                usuarioJPA.setNombre(usuario.getNombre());
                usuarioJPA.setApellidoPaterno(usuario.getApellidoPaterno());
                usuarioJPA.setApellidoMaterno(usuario.getApellidoMaterno());
                usuarioJPA.setFNacimiento(usuario.getFNacimiento());
                usuarioJPA.setNCelular(usuario.getNCelular());
                usuarioJPA.setCURP(usuario.getCURP());
                usuarioJPA.setUsername(usuario.getUsername());
                usuarioJPA.setEmail(usuario.getEmail());
                usuarioJPA.setPassword(usuario.getPassword());
                usuarioJPA.setSexo(usuario.getSexo());
                usuarioJPA.setTelefono(usuario.getTelefono());
                usuarioJPA.setImagen(usuario.getImagen());
                usuarioJPA.setStatus(usuario.getStatus());

                // Rol
                if (usuario.getRol() != null) {
                    usuarioJPA.Rol = entityManager.getReference(usuario.getRol().getClass(), usuario.getRol().getIdRol());
                }

                // Se guarda automáticamente al estar dentro de una transacción
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con ID: " + usuario.getIdUsuario();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            result.correct = false;
            result.errorMessage = "Error al actualizar el usuario.";
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result DeleteJPA(int idUsuario) {
        Result result = new Result();

        try {
            // Busqueda de usuario por ID
            Usuario usuarioJPA = entityManager.find(Usuario.class, idUsuario);

            if (usuarioJPA != null) {
                // Eliminar direcciones del usuario
                List<Direccion> queryDirecciones = entityManager.createQuery("FROM Direccion WHERE Usuario.IdUsuario = :idUsuario", Direccion.class)
                        .setParameter("idUsuario", idUsuario)
                        .getResultList();

                for (Direccion direccion : queryDirecciones) {
                    entityManager.remove(direccion);
                }

                // Eliminar el usuario después de eliminar sus direcciones
                entityManager.remove(usuarioJPA);
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result UpdateStatusJPA(Usuario usuario) {
        Result result = new Result();
        try {
            Usuario usuarioJPA = entityManager.find(Usuario.class, usuario.getIdUsuario());
            if (usuarioJPA != null) {
                // Actualizar campo de Status
                usuarioJPA.setStatus(usuario.getStatus());

                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con Id: " + usuario.getIdUsuario();
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result GetAllDinamicoJPA(Usuario usuario) {
        Result result = new Result();

        try {

            // Construccion de query
            StringBuilder jpql = new StringBuilder("SELECT u FROM Usuario u WHERE 1=1");

            Map<String, Object> parametros = new HashMap();

            // Validacion de datos que se recibe segun los filtros
            if (usuario.getNombre() != null && !usuario.getNombre().isEmpty()) {
                jpql.append(" AND u.Nombre LIKE :nombre");
                parametros.put("nombre", "%" + usuario.getNombre() + "%");
            }

            if (usuario.getApellidoPaterno() != null && !usuario.getApellidoPaterno().isEmpty()) {
                jpql.append(" AND u.ApellidoPaterno LIKE :apellidoPaterno");
                parametros.put("apellidoPaterno", "%" + usuario.getApellidoPaterno() + "%");
            }

            if (usuario.getApellidoMaterno() != null && !usuario.getApellidoMaterno().isEmpty()) {
                jpql.append(" AND u.ApellidoMaterno LIKE :apellidoMaterno");
                parametros.put("apellidoMaterno", "%" + usuario.getApellidoMaterno() + "%");
            }

            if (usuario.getRol() != null && usuario.getRol().getIdRol() > 0) {
                jpql.append(" AND u.Rol.IdRol = :idRol");
                parametros.put("idRol", usuario.getRol().getIdRol());
            }

            // Filtro de status lógico (0 = inactivo, 1 = activo)
            if (usuario.getStatus() == 0 || usuario.getStatus() == 1) {
                jpql.append(" AND u.Status = :status");
                parametros.put("status", usuario.getStatus());
            }

            // Ordenar los resultados
            jpql.append(" ORDER BY u.IdUsuario");

            // Creaccion de queery con armado
            TypedQuery<Usuario> query = entityManager.createQuery(jpql.toString(), Usuario.class);

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }

            List<Usuario> usuariosJPA = query.getResultList();
            result.objects = new ArrayList<>();

            // Procesar datos de cada usuario
            for (Usuario usuarioJPA : usuariosJPA) {
                UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
                Usuario user = new Usuario();

                // Mapear datos del usuario
                user.setIdUsuario(usuarioJPA.getIdUsuario());
                user.setNombre(usuarioJPA.getNombre());
                user.setApellidoPaterno(usuarioJPA.getApellidoPaterno());
                user.setApellidoMaterno(usuarioJPA.getApellidoMaterno());
                user.setImagen(usuarioJPA.getImagen());
                user.setUsername(usuarioJPA.getUsername());
                user.setEmail(usuarioJPA.getEmail());
                user.setPassword(usuarioJPA.getPassword());
                user.setFNacimiento(usuarioJPA.getFNacimiento());
                user.setSexo(usuarioJPA.getSexo());
                user.setTelefono(usuarioJPA.getTelefono());
                user.setNCelular(usuarioJPA.getNCelular());
                user.setCURP(usuarioJPA.getCURP());
                user.setStatus(usuarioJPA.getStatus());

                if (usuarioJPA.getRol() != null) {
                    Rol rol = new Rol();
                    rol.setIdRol(usuarioJPA.getRol().getIdRol());
                    rol.setNombre(usuarioJPA.getRol().getNombre());
                    user.setRol(rol);
                }

                usuarioDireccion.Usuario = user;

                // Cargar de Direcciones
                TypedQuery<Direccion> queryDireccion
                        = entityManager.createQuery("FROM Direccion d WHERE d.Usuario.IdUsuario = :IdUsuario", Direccion.class);
                queryDireccion.setParameter("IdUsuario", user.getIdUsuario());

                List<Direccion> direccionesJPA = queryDireccion.getResultList();

                usuarioDireccion.Direcciones = new ArrayList<>();

                for (Direccion direccionJPA : direccionesJPA) {
                    Direccion direccion = new Direccion();
                    direccion.setIdDireccion(direccionJPA.getIdDireccion());
                    direccion.setCalle(direccionJPA.getCalle() != null ? direccionJPA.getCalle() : "Sin dirección registrada");
                    direccion.setNumeroExterior(direccionJPA.getNumeroExterior() != null ? direccionJPA.getNumeroExterior() : "");
                    direccion.setNumeroInterior(direccionJPA.getNumeroInterior() != null ? direccionJPA.getNumeroInterior() : "");

                    direccion.Colonia = new Colonia();
                    direccion.Colonia.setIdColonia(direccionJPA.getColonia().getIdColonia());
                    direccion.Colonia.setNombre(direccionJPA.getColonia().getNombre());
                    direccion.Colonia.setCodigoPostal(direccionJPA.getColonia().getCodigoPostal());

                    direccion.Colonia.Municipio = new Municipio();
                    direccion.Colonia.Municipio.setNombre(direccionJPA.getColonia().getMunicipio().getNombre());

                    direccion.Colonia.Municipio.Estado = new Estado();
                    direccion.Colonia.Municipio.Estado.setNombre(direccionJPA.getColonia().getMunicipio().getEstado().getNombre());

                    direccion.Colonia.Municipio.Estado.Pais = new Pais();
                    direccion.Colonia.Municipio.Estado.Pais.setNombre(direccionJPA.getColonia().getMunicipio().getEstado().getPais().getNombre());

                    usuarioDireccion.Direcciones.add(direccion);
                }

                result.objects.add(usuarioDireccion);
            }

            result.correct = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            result.correct = false;
            result.errorMessage = "Error al obtener usuarios dinámicos con JPA.";
            result.ex = ex;
        }

        return result;
    }

}
