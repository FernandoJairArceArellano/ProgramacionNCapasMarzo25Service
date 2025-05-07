package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.Usuario;
import com.Digis01.FArceProgramacionNCapas.JPA.UsuarioDireccion;


public interface IUsuarioDAO {

    // EntityManager
    Result GetAllJPA();

    Result AddJPA(UsuarioDireccion usuarioDireccion);

    Result GetByIdJPA(int IdUsuario);
    
    Result GetUsuarioById(int IdUsuario);

    Result UpdateJPA(Usuario usuario);

    Result DeleteJPA(int IdUsuario);

    Result UpdateStatusJPA(Usuario usuario);

    Result GetAllDinamicoJPA(Usuario usuario);
}
