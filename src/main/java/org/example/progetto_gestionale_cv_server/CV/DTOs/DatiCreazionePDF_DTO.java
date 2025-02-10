package org.example.progetto_gestionale_cv_server.CV.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DatiCreazionePDF_DTO {


    @NotNull(message = "specificare id utente.")
    private Long idUtente;
    @NotNull(message = "titolo obbligatorio.")
    private String titolo;
    @NotNull(message = "nome candidato obbligatorio.")
    private String nomeCandidato;
    @NotNull(message = "cognome candidato obbligatorio.")
    private String cognomeCandidato;
    @NotNull(message = "competenze obbligatorie.")
    private String competenze;
    @NotNull(message = "istruzione obbligatoria.")
    private String istruzione;
    @NotNull(message = "esperienze precedenti obbligatorie.")
    private String esperienzePrecedenti;
    @NotNull(message = "inserire almeno una lingua conosciuta")
    private String lingueConosciute;

    private String descrizioneGenerale;


    public void setLingueConosciute(String lingueConosciute) {
        this.lingueConosciute = lingueConosciute;
    }

    public void setDescrizioneGenerale(String descrizioneGenerale) {
        descrizioneGenerale = descrizioneGenerale;
    }

    public String getLingueConosciute() {
        return lingueConosciute;
    }

    public String getDescrizioneGenerale() {
        return descrizioneGenerale;
    }

    public String getCompetenze() {
        return competenze;
    }

    public void setCompetenze(String competenze) {
        this.competenze = competenze;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getNomeCandidato() {
        return nomeCandidato;
    }

    public void setNomeCandidato(String nomeCandidato) {
        this.nomeCandidato = nomeCandidato;
    }

    public String getCognomeCandidato() {
        return cognomeCandidato;
    }

    public void setCognomeCandidato(String cognomeCandidato) {
        this.cognomeCandidato = cognomeCandidato;
    }

    public String getIstruzione() {
        return istruzione;
    }

    public void setIstruzione(String istruzione) {
        this.istruzione = istruzione;
    }

    public String getEsperienzePrecedenti() {
        return esperienzePrecedenti;
    }

    public void setEsperienzePrecedenti(String esperienzePrecedenti) {
        this.esperienzePrecedenti = esperienzePrecedenti;
    }


}
