package org.example.progetto_gestionale_cv_server.CV.DTOs.req;

import jakarta.validation.constraints.NotNull;

public class DatiModifica_cv_DTO {
    @NotNull(message = "specificare id utente.")
    private Long idUtente;
    @NotNull(message = "specificare cv da modificare")
    private Long idCv;
    @NotNull(message = "titolo obbligatorio.")
    private String titolo;
    @NotNull(message = "competenze obbligatorie.")
    private String competenze;
    @NotNull(message = "istruzione obbligatoria.")
    private String istruzione;
    @NotNull(message = "esperienze precedenti obbligatorie.")
    private String esperienzePrecedenti;
    @NotNull(message = "inserire almeno una lingua conosciuta")
    private String lingueConosciute;

    private String descrizioneGenerale;

    public String getDescrizioneGenerale() {
        return descrizioneGenerale;
    }

    public void setDescrizioneGenerale(String descrizioneGenerale) {
        this.descrizioneGenerale = descrizioneGenerale;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public String getLingueConosciute() {
        return lingueConosciute;
    }

    public void setLingueConosciute(String lingueConosciute) {
        this.lingueConosciute = lingueConosciute;
    }

    public String getEsperienzePrecedenti() {
        return esperienzePrecedenti;
    }

    public void setEsperienzePrecedenti(String esperienzePrecedenti) {
        this.esperienzePrecedenti = esperienzePrecedenti;
    }

    public String getIstruzione() {
        return istruzione;
    }

    public void setIstruzione(String istruzione) {
        this.istruzione = istruzione;
    }

    public String getCompetenze() {
        return competenze;
    }

    public void setCompetenze(String competenze) {
        this.competenze = competenze;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Long getIdCv() {
        return idCv;
    }

    public void setIdCv(Long idCv) {
        this.idCv = idCv;
    }
}
