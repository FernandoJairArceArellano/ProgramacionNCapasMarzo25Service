package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Estado;
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
public class EstadoDAOImplementation implements IEstadoDAO {

    @Autowired
    EntityManager entityManager;

    @Override
    public Result EstadoByIdPaisJPA(int IdPais) {
        Result result = new Result();
        try {
            // Consulta JPQL
            TypedQuery<Estado> queryEstado = entityManager.createQuery(
                    "FROM Estado WHERE Pais.IdPais = :idPais ORDER BY IdEstado",Estado.class);
            queryEstado.setParameter("idPais", IdPais);
            result.object = queryEstado.getResultList();

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
