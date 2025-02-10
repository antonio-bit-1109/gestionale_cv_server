package org.example.progetto_gestionale_cv_server.USER.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistrazioneUtenteDTO {

    @NotBlank(message = "nome obbligatorio.")
    private String nome;
    @NotBlank(message = "cognome obbligatorio")
    private String cognome;
    @NotBlank(message = "email obbligatoria")
    private String email;

    @NotBlank(message = "telefono obbligatorio")
    @Size(max = 15, message = "lunghezza massima 15 caratteri")
    private String telefono;

    @NotBlank(message = "password obbligatoria")
    private String password;

    @NotNull(message = "il valore non può essere false")
    private boolean consensoTrattamentoDati;

    public void setConsensoTrattamentoDati(boolean consensoTrattamentoDati) {
        this.consensoTrattamentoDati = consensoTrattamentoDati;
    }

    public boolean getConsensoTrattamentoDati() {
        return consensoTrattamentoDati;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
