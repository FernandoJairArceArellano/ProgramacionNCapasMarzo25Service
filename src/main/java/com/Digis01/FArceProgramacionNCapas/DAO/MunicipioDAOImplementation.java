package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Municipio;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioDAOImplementation implements IMunicipioDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result MunicipioByIdEstadoJPA(int IdEstado) {
        Result result = new Result();
        try {

            TypedQuery<Municipio> queryMunicipio = entityManager.createQuery(
                    "FROM Municipio WHERE Estado.IdEstado = :idEstado ORDER BY IdMunicipio",
                    Municipio.class);
            queryMunicipio.setParameter("idEstado", IdEstado);

            result.object = queryMunicipio.getResultList();

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
