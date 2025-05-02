package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RolDAOImplementation implements IRolDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAllJPA() {
        Result result = new Result();
        try {
            TypedQuery<Rol> queryRoles
                    = entityManager.createQuery("FROM Rol ORDER BY IdRol", Rol.class);

            List<Rol> roles = queryRoles.getResultList();
            result.objects = new ArrayList<>();
            for (Rol rolJPA : roles) {
                Rol rol = new Rol();
                rol.setIdRol(rolJPA.getIdRol());
                rol.setNombre(rolJPA.getNombre());
                result.objects.add(rol);
            }

            result.correct = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}
