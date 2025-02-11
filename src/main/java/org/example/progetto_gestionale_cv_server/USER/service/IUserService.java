package org.example.progetto_gestionale_cv_server.USER.service;

import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;

public interface IUserService {
    void registrazioneUtente(RegistrazioneUtenteDTO datiUtente);

    String doLogin(LoginDTO datiLogin);
}
