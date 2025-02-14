package org.example.progetto_gestionale_cv_server.USER.DTOs.req;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class CambioImgProfilo_DTO {

//    @NotNull(message = "id utente obbligatorio.")
//    private Long id_utente;

    @NotNull(message = "il file non può essere vuoto.")
    private MultipartFile imgProfilo;

//    public Long getId_utente() {
//        return id_utente;
//    }

    public MultipartFile getImgProfilo() {
        return imgProfilo;
    }

//    public void setId_utente(Long id_utente) {
//        this.id_utente = id_utente;
//    }

    public void setImgProfilo(MultipartFile imgProfilo) {
        this.imgProfilo = imgProfilo;
    }
}
