package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Estado;
import com.Digis01.FArceProgramacionNCapas.JPA.Municipio;
import com.Digis01.FArceProgramacionNCapas.JPA.Pais;
import com.Digis01.FArceProgramacionNCapas.JPA.UsuarioDireccion;
import com.Digis01.FArceProgramacionNCapas.JPA.Colonia;
import com.Digis01.FArceProgramacionNCapas.JPA.Direccion;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DireccionDAOImplementation implements IDireccionDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetByIdJPA(int IdDireccion) {
        Result result = new Result();

        try {
            Direccion direccion = entityManager
                    .createQuery("FROM Direccion WHERE IdDireccion = :id", Direccion.class)
                    .setParameter("id", IdDireccion)
                    .getSingleResult();

            result.object = direccion;
            result.correct = true;

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
    public Result DireccionAddJPA(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            // Buscar entidad Usuario existente
            Usuario usuarioJPA
                    = entityManager.find(Usuario.class, usuarioDireccion.Usuario.getIdUsuario());

            if (usuarioJPA == null) {
                result.errorMessage = ("Usuario no encontrado con ID: " + usuarioDireccion.Usuario.getIdUsuario());
            }

            // Buscar entidad Colonia
            Colonia coloniaJPA = entityManager.find(Colonia.class, usuarioDireccion.Direccion.Colonia.getIdColonia());

            if (coloniaJPA == null) {
                result.errorMessage = ("Colonia no encontrada con ID: " + usuarioDireccion.Direccion.Colonia.getIdColonia());
            }

            // Crear entidad Dirección
            Direccion direccionJPA = new Direccion();

            direccionJPA.setCalle(usuarioDireccion.Direccion.getCalle());
            direccionJPA.setNumeroInterior(usuarioDireccion.Direccion.getNumeroInterior());
            direccionJPA.setNumeroExterior(usuarioDireccion.Direccion.getNumeroExterior());
            direccionJPA.setColonia(coloniaJPA);
            //direccionJPA.setUsuario(usuarioJPA); // ASOCIA EL USUARIO!

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
            Direccion direccionJPA = entityManager.find(
                    Direccion.class,
                    usuarioDireccion.Direccion.getIdDireccion()
            );

            if (direccionJPA == null) {
                result.correct = false;
                result.errorMessage = "Dirección no encontrada con ID: " + usuarioDireccion.Direccion.getIdDireccion();
                return result;
            }

            // Buscar el usuario existente
            Usuario usuarioJPA = entityManager.find(
                    Usuario.class,
                    usuarioDireccion.Usuario.getIdUsuario()
            );

            if (usuarioJPA == null) {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con ID: " + usuarioDireccion.Usuario.getIdUsuario();
                return result;
            }

            // Buscar la colonia existente
            Colonia coloniaJPA
                    = entityManager.find(Colonia.class, usuarioDireccion.Direccion.Colonia.getIdColonia()
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
            //direccionJPA.setUsuario(usuarioJPA);

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
            Direccion direccion = entityManager.find(Direccion.class, IdDireccion);

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
