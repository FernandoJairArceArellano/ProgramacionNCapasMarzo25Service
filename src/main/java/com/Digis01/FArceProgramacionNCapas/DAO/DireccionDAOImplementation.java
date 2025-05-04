package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Colonia;
import com.Digis01.FArceProgramacionNCapas.JPA.Direccion;
import com.Digis01.FArceProgramacionNCapas.JPA.Estado;
import com.Digis01.FArceProgramacionNCapas.JPA.Municipio;
import com.Digis01.FArceProgramacionNCapas.JPA.Pais;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.UsuarioDireccion;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DireccionDAOImplementation implements IDireccionDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetByIdJPA(int IdDireccion) {
        Result result = new Result();

        try {
            // Buscar la direccion y sus datos
            com.Digis01.FArceProgramacionNCapas.JPA.Direccion direccionJPA
                    = entityManager.find(com.Digis01.FArceProgramacionNCapas.JPA.Direccion.class, IdDireccion);

            if (direccionJPA != null) {
                Direccion direccionML = new Direccion();

                // Mapear datos básicos
                direccionML.setIdDireccion(direccionJPA.getIdDireccion());
                direccionML.setCalle(direccionJPA.getCalle());
                direccionML.setNumeroExterior(direccionJPA.getNumeroExterior());
                direccionML.setNumeroInterior(direccionJPA.getNumeroInterior());

                // Mapear la Colonia
                direccionML.Colonia = new Colonia();
                direccionML.Colonia.setIdColonia(direccionJPA.getColonia().getIdColonia());
                direccionML.Colonia.setCodigoPostal(direccionJPA.getColonia().getCodigoPostal());
                direccionML.Colonia.setNombre(direccionJPA.getColonia().getNombre());

                // Mapear el Municipio
                direccionML.Colonia.Municipio = new Municipio();
                direccionML.Colonia.Municipio.setIdMunicipio(direccionJPA.getColonia().getMunicipio().getIdMunicipio());
                direccionML.Colonia.Municipio.setNombre(direccionJPA.getColonia().getMunicipio().getNombre());

                // Mapear el Estado
                direccionML.Colonia.Municipio.Estado = new Estado();
                direccionML.Colonia.Municipio.Estado.setIdEstado(direccionJPA.getColonia().getMunicipio().getEstado().getIdEstado());
                direccionML.Colonia.Municipio.Estado.setNombre(direccionJPA.getColonia().getMunicipio().getEstado().getNombre());

                // Mapear el País
                direccionML.Colonia.Municipio.Estado.Pais = new Pais();
                direccionML.Colonia.Municipio.Estado.Pais.setIdPais(direccionJPA.getColonia().getMunicipio().getEstado().getPais().getIdPais());
                direccionML.Colonia.Municipio.Estado.Pais.setNombre(direccionJPA.getColonia().getMunicipio().getEstado().getPais().getNombre());

                // Asignar al result
                result.object = direccionML;
                result.correct = true;

            } else {
                result.correct = true;
                result.errorMessage = "Direccion no encontrada.";
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result DireccionAddJPA(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            // Buscar entidad Usuario existente
            com.Digis01.FArceProgramacionNCapas.JPA.Usuario usuarioJPA
                    = entityManager.find(com.Digis01.FArceProgramacionNCapas.JPA.Usuario.class, usuarioDireccion.Usuario.getIdUsuario());

            if (usuarioJPA == null) {
                result.errorMessage = ("Usuario no encontrado con ID: " + usuarioDireccion.Usuario.getIdUsuario());
            }

            // Buscar entidad Colonia
            com.Digis01.FArceProgramacionNCapas.JPA.Colonia coloniaJPA
                    = entityManager.find(com.Digis01.FArceProgramacionNCapas.JPA.Colonia.class, usuarioDireccion.Direccion.Colonia.getIdColonia());

            if (coloniaJPA == null) {
                result.errorMessage = ("Colonia no encontrada con ID: " + usuarioDireccion.Direccion.Colonia.getIdColonia());
            }

            // Crear entidad Dirección
            com.Digis01.FArceProgramacionNCapas.JPA.Direccion direccionJPA = new com.Digis01.FArceProgramacionNCapas.JPA.Direccion();

            direccionJPA.setCalle(usuarioDireccion.Direccion.getCalle());
            direccionJPA.setNumeroInterior(usuarioDireccion.Direccion.getNumeroInterior());
            direccionJPA.setNumeroExterior(usuarioDireccion.Direccion.getNumeroExterior());
            direccionJPA.setColonia(coloniaJPA);
            direccionJPA.setUsuario(usuarioJPA); // ASOCIA EL USUARIO!

            // Persistir la dirección
            entityManager.persist(direccionJPA);

            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result UpdateByIdJPA(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            // Buscar la dirección existente por IdDireccion
            com.Digis01.FArceProgramacionNCapas.JPA.Direccion direccionJPA = entityManager.find(
                    com.Digis01.FArceProgramacionNCapas.JPA.Direccion.class,
                    usuarioDireccion.Direccion.getIdDireccion()
            );

            if (direccionJPA == null) {
                result.correct = false;
                result.errorMessage = "Dirección no encontrada con ID: " + usuarioDireccion.Direccion.getIdDireccion();
                return result;
            }

            // Buscar el usuario existente
            com.Digis01.FArceProgramacionNCapas.JPA.Usuario usuarioJPA = entityManager.find(
                    com.Digis01.FArceProgramacionNCapas.JPA.Usuario.class,
                    usuarioDireccion.Usuario.getIdUsuario()
            );

            if (usuarioJPA == null) {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con ID: " + usuarioDireccion.Usuario.getIdUsuario();
                return result;
            }

            // Buscar la colonia existente
            com.Digis01.FArceProgramacionNCapas.JPA.Colonia coloniaJPA
                    = entityManager.find(com.Digis01.FArceProgramacionNCapas.JPA.Colonia.class, usuarioDireccion.Direccion.Colonia.getIdColonia()
                    );

            if (coloniaJPA == null) {
                result.correct = false;
                result.errorMessage = "Colonia no encontrada con ID: " + usuarioDireccion.Direccion.Colonia.getIdColonia();
                return result;
            }

            // Ahora actualizar los campos de la dirección encontrada
            direccionJPA.setCalle(usuarioDireccion.Direccion.getCalle());
            direccionJPA.setNumeroInterior(usuarioDireccion.Direccion.getNumeroInterior());
            direccionJPA.setNumeroExterior(usuarioDireccion.Direccion.getNumeroExterior());
            direccionJPA.setColonia(coloniaJPA);
            direccionJPA.setUsuario(usuarioJPA);

            // No necesitas hacer merge explícitamente, Hibernate se encarga en transacciones.
            // entityManager.merge(direccionJPA);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result DeleteDireccionJPA(int IdDireccion) {
        Result result = new Result();

        try {
            com.Digis01.FArceProgramacionNCapas.JPA.Direccion direccion
                    = entityManager.find(com.Digis01.FArceProgramacionNCapas.JPA.Direccion.class, IdDireccion);

            if (direccion != null) {
                entityManager.remove(direccion);
                result.correct = true;
                result.errorMessage = "Dirección eliminada correctamente.";
            } else {
                result.correct = false;
                result.errorMessage = "Dirección no encontrada con el ID: " + IdDireccion;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result.correct = false;
            result.errorMessage = "Error al eliminar la dirección.";
            result.ex = ex;
        }

        return result;
    }

}
