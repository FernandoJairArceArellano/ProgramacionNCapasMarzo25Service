package com.Digis01.FArceProgramacionNCapas.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {
    
    @Bean // Declaracion de un bean para que Spring lo administre
    public CorsFilter corsFilter(){
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Fuente para configurar CORS
        CorsConfiguration corsConfig = new CorsConfiguration(); // Objeto que define las reglas de CORS
        
        corsConfig.addAllowedOrigin("*"); // Permite solicitudes desde cualquier origen no se hace en produccion
        // Para mayor seguridad en producción, se recomienda especificar el dominio permitido:
        // corsConfig.addAllowedOrigin("https://tudominio.com");
        
        // Metodos HTTP permitidos
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");
        // También se puede usar corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        
        corsConfig.addAllowedHeader("*"); // Permite cualquier encabezado
        // corsConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With"));
        
        // Permite que las cookies o cabeceras de autenticación se incluyan en la solicitud
        //corsConfig.setAllowCredentials(true); // Importante: esto requiere que los orígenes NO se usa "*"
        
        // Aplica la configuracion a todas las ritas del backend
        source.registerCorsConfiguration("/**", corsConfig); // Se aplica la configuracion CORS  a todas las rutas
        
        return new CorsFilter(source); // Retorno de filtro que aplica la configuracion
    }
}
