package org.example.progetto_gestionale_cv_server.USER.DTOs;

public class LoginDTO {

    private String Email;
    private String Password;

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }
}
