package it.psw.bookstore.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfiguration {

    /*
    Il filtro CORS è responsabile di gestire e consentire le richieste HTTP
    provenienti da origini diverse rispetto al server che ospita l'applicazione.
     */

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        //Abilita il supporto per l'invio di credenziali
        //(come cookie o intestazioni di autenticazione) nelle richieste CORS.
        configuration.setAllowCredentials(true);

        //Consente l'accesso da qualsiasi origine
        configuration.addAllowedOrigin("*");

        //Consente l'utilizzo di qualsiasi header nelle richieste CORS
        configuration.addAllowedHeader("*");

        //Queste istruzioni specificano i metodi HTTP consentiti nelle richieste CORS
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");

        /*
        Il filtro CORS personalizzato viene configurato per applicare le regole definite
        nell'istanza di CorsConfiguration a tutte le risorse (indicato da "/**").
        Questo significa che le regole CORS saranno applicate globalmente a tutte le richieste
         */
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

}
