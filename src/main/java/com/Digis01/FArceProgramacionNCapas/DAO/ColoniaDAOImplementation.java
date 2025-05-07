package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Colonia;
import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ColoniaDAOImplementation implements IColoniaDAO {

    @Autowired
    EntityManager entityManager;

    @Override
    public Result ColoniaByIdMunicipioJPA(int IdMunicipio) {
        Result result = new Result();
        try {
            TypedQuery<Colonia> queryColonia = entityManager.createQuery(
                    "FROM Colonia WHERE Municipio.IdMunicipio = :idMunicipio", Colonia.class);
            queryColonia.setParameter("idMunicipio", IdMunicipio);

            result.object = queryColonia.getResultList();
            
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
