package org.example.progetto_gestionale_cv_server.USER.DTOs.req;

import jakarta.validation.constraints.*;
import org.example.progetto_gestionale_cv_server.utility.customExceptions.CustomPatternException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class RegistrazioneUtenteDTO {

    @NotBlank(message = "nome obbligatorio.")
    @NotNull(message = "il campo nome non può essere null")
    @Size(min = 2, max = 100, message = "il campo nome deve essere lungo minimo 2, massimo 100 caratteri")
    private String nome;

    @NotBlank(message = "cognome obbligatorio.")
    @NotNull(message = "il campo cognome non può essere null")
    @Size(min = 2, max = 100, message = "il campo cognome deve essere lungo minimo 2, massimo 100 caratteri")
    private String cognome;

    @NotBlank(message = "email obbligatoria")
    @NotNull(message = "emaill non può essere null")
    @Email(message = "il campo email deve essere nel formato valido")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(gmail|aruba|libero|yahoo|outlook|hotmail|virgilio)\\.(com|it)$",
            message = "La mail deve essere nel formato: example@gmail.com or example@aruba.it"
    )
    private String email;

    @NotBlank(message = "telefono obbligatorio")
    @NotNull(message = "il campo telefono non può essere null.")
    @Size(min = 15, max = 15, message = "The phone number must be exactly 15 characters")
    @Pattern(regexp = "^\\+39 \\d{3} \\d{7}$", message = " il telefono deve essere nel formato +39 XXX XXXXXXX")
    private String telefono;

    @NotBlank(message = "password obbligatoria")
    @NotNull(message = "Field password can not be null")
    @Size(min = 5, max = 20, message = "The password must be between 5 and 15 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*.()\\-+])[A-Za-z\\d!@#$.%^&*()\\-+]{5,20}$",
            message = "The password must contain at least one uppercase letter, one lowercase letter, one number " +
                    "and one special character between !@#$%^&*()-+"
    )
    private String password;

    @NotNull(message = "il campo non può essere vuoto")
    private boolean consensoTrattamentoDati;

    public void setConsensoTrattamentoDati(boolean consensoTrattamentoDati) {

        if (!consensoTrattamentoDati) {
            throw new CustomPatternException("il trattamento dei dati personali deve essere spuntato", "consensoTrattamentoDati");
        }
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
