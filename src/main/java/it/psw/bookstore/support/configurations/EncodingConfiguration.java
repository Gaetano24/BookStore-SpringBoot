package it.psw.bookstore.support.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.nio.charset.StandardCharsets;

@Configuration
public class EncodingConfiguration {

    /*
    Imposta la codifica predefinita dei messaggi JSON generati dall'applicazione su UTF-8,
    garantendo che le risposte HTTP contenenti dati JSON siano correttamente codificate in UTF-8.
    Tale configurazione è importante per gestire caratteri speciali e assicurare la compatibilità
    con una vasta gamma di client e sistemi.
     */

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
        return jsonConverter;
    }

}
