package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Municipio;
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
public class MunicipioDAOImplementation implements IMunicipioDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result MunicipioByIdEstadoJPA(int IdEstado) {
        Result result = new Result();
        try {

            TypedQuery<com.Digis01.FArceProgramacionNCapas.JPA.Municipio> query
                    = entityManager.createQuery(
                            "FROM Municipio m WHERE m.Estado.IdEstado = :idEstado ORDER BY m.IdMunicipio",
                            com.Digis01.FArceProgramacionNCapas.JPA.Municipio.class
                    );
            query.setParameter("idEstado", IdEstado);

            List<com.Digis01.FArceProgramacionNCapas.JPA.Municipio> municipiosJPA = query.getResultList();
            result.objects = new ArrayList<>();

            for (com.Digis01.FArceProgramacionNCapas.JPA.Municipio municipioJPA : municipiosJPA) {
                Municipio municipio = new Municipio();
                municipio.setIdMunicipio(municipioJPA.getIdMunicipio());
                municipio.setNombre(municipioJPA.getNombre());
                result.objects.add(municipio);
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
