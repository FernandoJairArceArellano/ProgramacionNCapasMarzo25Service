package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.UsuarioDireccion;


public interface IDireccionDAO {
        
    // EntityManager
    Result GetByIdJPA(int IdDireccion);
    
    Result DireccionAddJPA(UsuarioDireccion usuarioDireccion);
    
    Result UpdateByIdJPA(UsuarioDireccion usuarioDireccion);
    
    Result DeleteDireccionJPA(int IdDireccion);
}
