package org.example.progetto_gestionale_cv_server.USER.entity;

import jakarta.persistence.*;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;

import java.util.List;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cognome;


    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = false)
    private boolean consensoTrattamentoDati;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CVs> listaCv;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Credenziali credenziali;


    public void setCredenziali(Credenziali credenziali) {
        this.credenziali = credenziali;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Credenziali getCredenziali() {
        return credenziali;
    }

    public void setConsensoTrattamentoDati(boolean consensoTrattamentoDati) {
        this.consensoTrattamentoDati = consensoTrattamentoDati;
    }

    public boolean getConsensoTrattamentoDati() {
        return this.consensoTrattamentoDati;
    }

    public List<CVs> getListaCv() {
        return listaCv;
    }

    public void setListaCv(List<CVs> listaCv) {
        this.listaCv = listaCv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


}
