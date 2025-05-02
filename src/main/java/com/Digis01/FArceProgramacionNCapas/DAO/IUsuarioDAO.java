package com.Digis01.FArceProgramacionNCapas.DAO;

import com.Digis01.FArceProgramacionNCapas.JPA.Result;
import com.Digis01.FArceProgramacionNCapas.JPA.Usuario;
import com.Digis01.FArceProgramacionNCapas.JPA.UsuarioDireccion;


public interface IUsuarioDAO {

    // JdbcTemplate
//    Result GetAll(); //Metodo Abstracto

//    Result Add(UsuarioDireccion usuarioDireccion);

//    Result direccionesByIdUsuario(int IdUsario);

//    Result GetById(int IdUsaurio);

//    Result Update(Usuario usuario);

//    Result UpdateStatus(Usuario usuario);

//    Result GetAllDinamico(Usuario usuario);

    // EntityManager
    Result GetAllJPA();

    Result AddJPA(UsuarioDireccion usuarioDireccion);

    Result DireccionesByIdUsuarioJPA(int IdUsuario);

    Result GetByIdJPA(int IdUsuario);

    Result UpdateJPA(Usuario usuario);

    Result DeleteJPA(int IdUsuario);

    Result UpdateStatusJPA(Usuario usuario);

    Result GetAllDinamicoJPA(Usuario usuario);
}
