package org.example.progetto_gestionale_cv_server.CV.DTOs.resp;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;

import java.sql.Timestamp;

public class Cv_get_DTO extends BaseDTO {

    private Long id_cv;
    private String titolo;
    private String esperienze_Precedenti;
    private String competenze;
    private String istruzione;
    private String lingueConosciute;
    private String descrizioneGenerale;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String proprietarioCV;
    private String Path_file_System_pdf;
    private String email;
    private String telefono;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setId_cv(Long id_cv) {
        this.id_cv = id_cv;
    }

    public Long getId_cv() {
        return id_cv;
    }

    public String getPath_file_System_pdf() {
        return Path_file_System_pdf;
    }

    public void setPath_file_System_pdf(String path_file_System_pdf) {
        Path_file_System_pdf = path_file_System_pdf;
    }

    public Cv_get_DTO() {
    }

    public Cv_get_DTO(String tit, String esper_prece, String competenze, String istruzione,
                      String lingue, String descriz,
                      String created, String updated, String propretario) {
        this.titolo = tit;
        this.esperienze_Precedenti = esper_prece;
        this.competenze = competenze;
        this.istruzione = istruzione;
        this.lingueConosciute = lingue;
        this.descrizioneGenerale = descriz;
        this.created_at = Timestamp.valueOf(created);
        this.updated_at = Timestamp.valueOf(updated);
        this.proprietarioCV = propretario;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getProprietarioCV() {
        return proprietarioCV;
    }

    public void setProprietarioCV(String proprietarioCV) {
        this.proprietarioCV = proprietarioCV;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getDescrizioneGenerale() {
        return descrizioneGenerale;
    }

    public void setDescrizioneGenerale(String descrizioneGenerale) {
        this.descrizioneGenerale = descrizioneGenerale;
    }

    public String getLingueConosciute() {
        return lingueConosciute;
    }

    public void setLingueConosciute(String lingueConosciute) {
        this.lingueConosciute = lingueConosciute;
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

    public String getEsperienze_Precedenti() {
        return esperienze_Precedenti;
    }

    public void setEsperienze_Precedenti(String esperienze_Precedenti) {
        this.esperienze_Precedenti = esperienze_Precedenti;
    }
}
