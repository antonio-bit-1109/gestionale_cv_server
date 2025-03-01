package org.example.progetto_gestionale_cv_server.utility.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// questa classe serve a prendere dinamicamente
// i dati dall application properties ed utilizzarli dove mi serve
// 1- nella registrazione di un nuovo utente,
// voglio che l'url da cui prendere immagine di default sia dinamico a seconda di
// dove questo server ha fatto deploy e quale sia la porta da cui resta in ascolto
@Component
public class ConfigurationConnectionProp {

    @Value("${server.port}")
    private String ServerPort;

    @Value("${server.address}")
    private String ServerAddress;

    public String getServerPort() {
        return ServerPort;
    }

    public String getServerAddress() {
        return ServerAddress;
    }
}
