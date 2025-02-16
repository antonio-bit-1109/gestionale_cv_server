package org.example.progetto_gestionale_cv_server.USER.service;

import org.example.progetto_gestionale_cv_server.USER.DTOs.req.CambioImgProfilo_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_List_utenti_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_Utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    boolean registrazioneUtente(RegistrazioneUtenteDTO datiUtente);

    boolean cambioImgProfilo(MultipartFile fileImg, Long idUser) throws IOException;

    String doLogin(LoginDTO datiLogin);

    Users returnUserIfExist(Long id_utente);

    Get_Utente_DTO GetUtenteSingolo(Long id_utente);

    List<Get_Utente_DTO> getListaUtenti();

    boolean handleStatus(Long id_utente);
}
