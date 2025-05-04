package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Colonia;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ColoniaDAOImplementation implements IColoniaDAO {


    @Autowired
    EntityManager entityManager;


    @Override
    public Result ColoniaByIdMunicipioJPA(int IdMunicipio) {
        Result result = new Result();
        try {
            TypedQuery<com.Digis01.FArceProgramacionNCapas.JPA.Colonia> query
                    = entityManager.createQuery("FROM Colonia c WHERE c.Municipio.IdMunicipio = :idMunicipio",
                            com.Digis01.FArceProgramacionNCapas.JPA.Colonia.class);
            query.setParameter("idMunicipio", IdMunicipio);

            List<com.Digis01.FArceProgramacionNCapas.JPA.Colonia> coloniasJPA = query.getResultList();
            result.objects = new ArrayList<>();

            for (com.Digis01.FArceProgramacionNCapas.JPA.Colonia coloniaJPA : coloniasJPA) {
                Colonia colonia = new Colonia();
                colonia.setIdColonia(coloniaJPA.getIdColonia());
                colonia.setNombre(coloniaJPA.getNombre());
                colonia.setCodigoPostal(coloniaJPA.getCodigoPostal());

                result.objects.add(colonia);
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
