package org.example.progetto_gestionale_cv_server.CV.DTOs.req;

public class ID_UTENTE_CV_DTO {

    private Long id_utente;
    private Long id_cv;

    public Long getId_utente() {
        return id_utente;
    }

    public void setId_utente(Long id_utente) {
        this.id_utente = id_utente;
    }

    public Long getId_cv() {
        return id_cv;
    }

    public void setId_cv(Long id_cv) {
        this.id_cv = id_cv;
    }
}
