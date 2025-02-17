package org.example.progetto_gestionale_cv_server.USER.DTOs.req;

import jakarta.validation.constraints.Pattern;

public class LoginDTO {

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(gmail|aruba|libero|yahoo|outlook|hotmail|virgilio)\\.(com|it)$",
            message = "La mail deve essere nel formato: example@gmail.com or example@aruba.it"
    )
    private String Email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*.()\\-+])[A-Za-z\\d!@#$.%^&*()\\-+]{5,20}$",
            message = "The password must contain at least one uppercase letter, one lowercase letter, one number " +
                    "and one special character between !@#$%^&*()-+"
    )
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
