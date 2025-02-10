package org.example.progetto_gestionale_cv_server.CV.entity;

import jakarta.persistence.*;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;

import java.sql.Timestamp;

@Entity
public class CVs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = true)
    private Timestamp created_at;
    @Column(nullable = false)
    private String titolo;
    @Column(nullable = true)
    private Timestamp updated_at;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String esperienze_Precedenti;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String competenze;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String istruzione;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    @Column(nullable = true)
    private String nome_file_pdf;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String lingueConosciute;
    @Column(nullable = true)
    private String profileImage;
    @Column(nullable = true)
    private String DescrizioneGenerale;


    public void setDescrizioneGenerale(String descrizioneGenerale) {
        DescrizioneGenerale = descrizioneGenerale;
    }

    public String getDescrizioneGenerale() {
        return DescrizioneGenerale;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setNome_file_pdf(String nome_file_pdf) {
        this.nome_file_pdf = nome_file_pdf;
    }

    public String getNome_file_pdf() {
        return nome_file_pdf;
    }

    public void setLingueConosciute(String lingueConosciute) {
        this.lingueConosciute = lingueConosciute;
    }

    public String getLingueConosciute() {
        return lingueConosciute;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTitolo() {
        return titolo;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getEsperienze_Precedenti() {
        return esperienze_Precedenti;
    }

    public void setEsperienze_Precedenti(String esperienze_Precedenti) {
        this.esperienze_Precedenti = esperienze_Precedenti;
    }

    public String getCompetenze() {
        return competenze;
    }

    public void setCompetenze(String competenze) {
        this.competenze = competenze;
    }

    public String getIstruzione() {
        return istruzione;
    }

    public void setIstruzione(String istruzione) {
        this.istruzione = istruzione;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
