package org.example.progetto_gestionale_cv_server.USER.service;

import org.example.progetto_gestionale_cv_server.USER.DTOs.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.RegistrazioneUtenteDTO;

public interface IUserService {
    void registrazioneUtente(RegistrazioneUtenteDTO datiUtente);

    String doLogin(LoginDTO datiLogin);
}
